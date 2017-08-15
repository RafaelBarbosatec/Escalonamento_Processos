package rafael.barbosa.escalonamento.Cadastro;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private Button bt_iniciar;
    private FloatingActionButton fb_add;
    private RecyclerView recycler_processos;
    private EditText ed_quantum, ed_sobrecarga;
    private Spinner spinner_algoritimo;
    private ProcessoAdapter processoAdapter;
    private LinearLayout ll_conf;
    private TextView tv_conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        iniciarViews();
    }

    private void iniciarViews() {

        ll_conf = (LinearLayout) findViewById(R.id.ll_conf);
        tv_conf = (TextView) findViewById(R.id.tv_conf);

        List<String> list = new ArrayList<String>();
        list.add("FIFO");
        list.add("SJF");
        list.add("ROUND ROBIN");
        list.add("PRIORIDADE");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

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
                String quantum = ed_quantum.getText().toString().trim();
                if (!quantum.equals(""))
                QUANTUM = Integer.parseInt(quantum);

                String sobrecarga = ed_sobrecarga.getText().toString().trim();
                if (!sobrecarga.equals(""))
                    SOBRECARGA = Integer.parseInt(sobrecarga);

                iniciarAlgoritimo();
            }
        });

        recycler_processos = (RecyclerView) findViewById(R.id.recycler_processos);
        recycler_processos.setLayoutManager(new LinearLayoutManager(this));
        processoAdapter = new ProcessoAdapter(this, new ArrayList<Processo>(),this);
        recycler_processos.setAdapter(processoAdapter);

    }

    private void iniciarAlgoritimo() {

        RespAlgoritimo respAlgoritimo = new RespAlgoritimo();

        if (processoAdapter.getItemCount() > 0) {

            switch (spinner_algoritimo.getSelectedItemPosition()){
                case 0: respAlgoritimo = Algoritimos.FIFO(processoAdapter.getList()); break;
                case 1: respAlgoritimo = Algoritimos.SJF(processoAdapter.getList()); break;
                case 2: respAlgoritimo = Algoritimos.ROUND_ROBIN(processoAdapter.getList()); break;
                case 3: Toast.makeText(this,"Descupe, algoritimo ainda não foi implementado",Toast.LENGTH_SHORT).show(); return;
                default: return;
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.putParcelableArrayListExtra("ITENS", (ArrayList<? extends Parcelable>) respAlgoritimo.getItemTimeLines());
            intent.putExtra("QTD", processoAdapter.getItemCount());
            intent.putExtra("TURNAROUND", respAlgoritimo.getTurnaround());
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
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    @Override
    public void processoClicked(int position) {

    }
}
