package rafael.barbosa.escalonamento.Cadastro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import rafael.barbosa.escalonamento.MainActivity;
import rafael.barbosa.escalonamento.Model.ItemTimeLine;
import rafael.barbosa.escalonamento.Model.Processo;
import rafael.barbosa.escalonamento.Model.RespAlgoritimo;
import rafael.barbosa.escalonamento.R;
import rafael.barbosa.escalonamento.adapters.ProcessoAdapter;
import rafael.barbosa.escalonamento.util.Algoritimos;
import rafael.barbosa.escalonamento.util.Funcoes;

public class CadastroActivity extends AppCompatActivity implements ProcessoAdapter.ProcessoClickListern {

    public static int QUANTUM = 3;
    public static int SOBRECARGA = 1;
    private Toolbar toolbar_cadastro;
    private Button bt_iniciar;
    private FloatingActionButton fb_add;
    private RecyclerView recycler_processos;
    private EditText ed_quantum, ed_sobrecarga;
    private Spinner spinner_algoritimo;
    private ProcessoAdapter processoAdapter;
    private LinearLayout ll_conf;
    private TextView tv_conf, tv_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        iniciarViews();
    }

    private void iniciarViews() {

        tv_empty = (TextView) findViewById(R.id.tv_empty);
        ll_conf = (LinearLayout) findViewById(R.id.ll_conf);
        tv_conf = (TextView) findViewById(R.id.tv_conf);

        List<String> list = new ArrayList<String>();
        list.add("FIFO");
        list.add("SJF");
        list.add("ROUND ROBIN");
        list.add("PRIORIDADE");
        list.add("EDF");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.simple_spinner_item_custom, list);

        spinner_algoritimo = (Spinner) findViewById(R.id.spinner_algoritimo);
        spinner_algoritimo.setAdapter(dataAdapter);
        spinner_algoritimo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 || i ==1){
                    ll_conf.setVisibility(View.GONE);
                    tv_conf.setVisibility(View.GONE);
                }else {
                    ll_conf.setVisibility(View.VISIBLE);
                    tv_conf.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ed_quantum = (EditText) findViewById(R.id.ed_quantum);
        ed_sobrecarga = (EditText) findViewById(R.id.ed_sobrecarga);
        fb_add = (FloatingActionButton) findViewById(R.id.fb_add);
        fb_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddProcesso();
            }
        });

        bt_iniciar = (Button) findViewById(R.id.bt_iniciar);
        bt_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (processoAdapter.getItemCount() <= 10) {
                    String quantum = ed_quantum.getText().toString().trim();
                    if (!quantum.equals(""))
                        QUANTUM = Integer.parseInt(quantum);

                    String sobrecarga = ed_sobrecarga.getText().toString().trim();
                    if (!sobrecarga.equals(""))
                        SOBRECARGA = Integer.parseInt(sobrecarga);

                    iniciarAlgoritimo();
                }else {
                    Toast.makeText(CadastroActivity.this,"Limite de 10 processos alcançados",Toast.LENGTH_SHORT).show();
                }
            }
        });

        recycler_processos = (RecyclerView) findViewById(R.id.recycler_processos);
        recycler_processos.setLayoutManager(new LinearLayoutManager(this));
        processoAdapter = new ProcessoAdapter(this, new ArrayList<Processo>(),this);
        recycler_processos.setAdapter(processoAdapter);

    }

    private void iniciarAlgoritimo() {

        RespAlgoritimo respAlgoritimo = new RespAlgoritimo();

        List<Processo> processoListAux = new LinkedList<>(processoAdapter.getList());

        String nomeAlgoritimo = "";

        if (processoAdapter.getItemCount() > 0) {

            switch (spinner_algoritimo.getSelectedItemPosition()){
                case 0: respAlgoritimo = Algoritimos.FIFO(processoListAux); nomeAlgoritimo = "FIFO"; break;
                case 1: respAlgoritimo = Algoritimos.SJF(processoListAux); nomeAlgoritimo = "SJF"; break;
                case 2: respAlgoritimo = Algoritimos.ROUND_ROBIN(processoListAux); nomeAlgoritimo = "ROUND ROBIN"; break;
                case 3: respAlgoritimo = Algoritimos.PRIORIDADE(processoListAux); nomeAlgoritimo = "PRIORIDADE"; break;
                default: return;
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.putParcelableArrayListExtra("ITENS", (ArrayList<? extends Parcelable>) respAlgoritimo.getItemTimeLines());
            intent.putExtra("QTD", processoAdapter.getItemCount());
            intent.putExtra("TURNAROUND", respAlgoritimo.getTurnaround());
            intent.putExtra("ALGORITIMO", nomeAlgoritimo);
            startActivity(intent);
        }
    }

    private void dialogAddProcesso(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        Button bt_adicionar = dialog.findViewById(R.id.bt_adicionar);
        final EditText ed_t_chegada = dialog.findViewById(R.id.ed_t_chegada);
        final EditText ed_t_exe = dialog.findViewById(R.id.ed_t_exe);
        final EditText ed_deadline = dialog.findViewById(R.id.ed_deadline);
        final EditText ed_prioridade = dialog.findViewById(R.id.ed_prioridade);

        bt_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String t_chegada = ed_t_chegada.getText().toString().trim();
                if (t_chegada.equals("")){
                    ed_t_chegada.setError("Campo Obrigatório");
                    return;
                }

                String t_exe = ed_t_exe.getText().toString().trim();
                if (t_exe.equals("")){
                    ed_t_exe.setError("Campo Obrigatório");
                    return;
                }

                Processo processo = new Processo();
                processo.setT_chegada(Integer.parseInt(t_chegada));
                processo.setT_execucao(Integer.parseInt(t_exe));

                String deadline = ed_deadline.getText().toString().trim();
                if (!deadline.equals(""))
                    processo.setDeadline(Integer.parseInt(deadline));

                String prioridade = ed_prioridade.getText().toString().trim();
                if (!prioridade.equals(""))
                    processo.setPrioridade(Integer.parseInt(prioridade));

                processo.setNome(Funcoes.generateNome(processoAdapter.getItemCount()));
                processo.setPosition(processoAdapter.getItemCount()+1);
                processoAdapter.addListaItem(processo,processoAdapter.getItemCount());
                tv_empty.setVisibility(View.INVISIBLE);
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    @Override
    public void processoClicked(final int position) {
       /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Deseja excluir processo : "+processoAdapter.getItem(position).getNome());
        alertDialogBuilder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                processoAdapter.removeListItem(position);
                if (processoAdapter.getItemCount() == 0){
                    tv_empty.setVisibility(View.VISIBLE);
                }
            }
        });
        alertDialogBuilder.setNegativeButton("NÂO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.show();*/
        dialogEditarProcesso(position);
    }

    private void dialogEditarProcesso(final int position){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        Button bt_adicionar = dialog.findViewById(R.id.bt_adicionar);
        bt_adicionar.setText("EDITAR");

        Button bt_excluir = dialog.findViewById(R.id.bt_excluir);
        bt_excluir.setVisibility(View.VISIBLE);

        final EditText ed_t_chegada = dialog.findViewById(R.id.ed_t_chegada);
        final EditText ed_t_exe = dialog.findViewById(R.id.ed_t_exe);
        final EditText ed_deadline = dialog.findViewById(R.id.ed_deadline);
        final EditText ed_prioridade = dialog.findViewById(R.id.ed_prioridade);

        ed_t_chegada.setText(processoAdapter.getItem(position).getT_chegada()+"");
        ed_t_exe.setText(processoAdapter.getItem(position).getT_execucao()+"");
        ed_deadline.setText(processoAdapter.getItem(position).getDeadline()+"");
        ed_prioridade.setText(processoAdapter.getItem(position).getPrioridade()+"");

        bt_excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processoAdapter.removeListItem(position);
                if (processoAdapter.getItemCount() == 0){
                    tv_empty.setVisibility(View.VISIBLE);
                }

                dialog.dismiss();
            }
        });

        bt_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String t_chegada = ed_t_chegada.getText().toString().trim();
                if (t_chegada.equals("")){
                    ed_t_chegada.setError("Campo Obrigatório");
                    return;
                }

                String t_exe = ed_t_exe.getText().toString().trim();
                if (t_exe.equals("")){
                    ed_t_exe.setError("Campo Obrigatório");
                    return;
                }

                Processo processo = new Processo();
                processo.setT_chegada(Integer.parseInt(t_chegada));
                processo.setT_execucao(Integer.parseInt(t_exe));

                String deadline = ed_deadline.getText().toString().trim();
                if (!deadline.equals(""))
                    processo.setDeadline(Integer.parseInt(deadline));

                String prioridade = ed_prioridade.getText().toString().trim();
                if (!prioridade.equals(""))
                    processo.setPrioridade(Integer.parseInt(prioridade));

                processo.setNome(processoAdapter.getItem(position).getNome());
                processoAdapter.editProcesso(processo,position);
                tv_empty.setVisibility(View.INVISIBLE);
                dialog.dismiss();

            }
        });

        dialog.show();
    }
}
