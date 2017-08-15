package rafael.barbosa.escalonamento;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rafael.barbosa.escalonamento.Model.ItemTimeLine;
import rafael.barbosa.escalonamento.util.Funcoes;
import rafael.barbosa.escalonamento.util.TimeLineView;

public class MainActivity extends AppCompatActivity {

    private TimeLineView timeLine;
    private int qtd;
    private double turnaround;
    private RelativeLayout rl_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl_main = (RelativeLayout) findViewById(R.id.rl_main);
        timeLine = (TimeLineView) findViewById(R.id.timeLine);

        List<ItemTimeLine> itemTimeLineList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null){
            Bundle extras = intent.getExtras();
            if (extras != null){
                itemTimeLineList = extras.getParcelableArrayList("ITENS");
                qtd = extras.getInt("QTD",0);
                turnaround = extras.getDouble("TURNAROUND");
            }
        }

        timeLine.setProcessoList(itemTimeLineList);

        List<String> nomes = new ArrayList<>();
        for (int i = 0 ; i < qtd; i ++){
            nomes.add(Funcoes.generateNome(i));
        }

        timeLine.setNomesProcessos(nomes);
        timeLine.startSequencia(new TimeLineView.TimeProcessoListern() {
            @Override
            public void finish() {
                Snackbar.make(rl_main,"Turnaround: "+turnaround,Snackbar.LENGTH_INDEFINITE).show();
            }
        });

    }
}
