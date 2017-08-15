package rafael.barbosa.escalonamento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rafael.barbosa.escalonamento.Model.ItemTimeLine;
import rafael.barbosa.escalonamento.util.Funcoes;
import rafael.barbosa.escalonamento.util.TimeLineView;

public class MainActivity extends AppCompatActivity {

    private TimeLineView timeLine;
    private int qtd;
    private double turnaround;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        /*ItemTimeLine itemTimeLine = new ItemTimeLine();
        itemTimeLine.setPosition(1);
        itemTimeLine.setTempo(2);
        itemTimeLine.setTempo_chegada(1);
        itemTimeLine.setSobrecarga(1);
        itemTimeLineList.add(itemTimeLine);

        itemTimeLine = new ItemTimeLine();
        itemTimeLine.setPosition(3);
        itemTimeLine.setTempo(2);
        itemTimeLine.setTempo_chegada(6);
        itemTimeLine.setSobrecarga(0);
        itemTimeLineList.add(itemTimeLine);

        itemTimeLine = new ItemTimeLine();
        itemTimeLine.setPosition(2);
        itemTimeLine.setTempo(1);
        itemTimeLine.setTempo_chegada(9);
        itemTimeLine.setSobrecarga(1);
        itemTimeLineList.add(itemTimeLine);*/

        timeLine.setProcessoList(itemTimeLineList);

        List<String> nomes = new ArrayList<>();
        for (int i = 0 ; i < qtd; i ++){
            nomes.add(Funcoes.generateNome(i));
        }

        timeLine.setNomesProcessos(nomes);
        timeLine.startSequencia();

       /* frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
        horizontal_scroll = (HorizontalScrollView) findViewById(R.id.horizontal_scroll);
        bt_start = (Button) findViewById(R.id.bt_start);
        ll_linhas = (LinearLayout) findViewById(R.id.ll_linhas);

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemTimeLine itemTimeLine = new ItemTimeLine();
                itemTimeLine.setPosition(aux);
                itemTimeLine.setTempo(2);
                itemTimeLine.setSobrecarga(1);
                showProcesso(itemTimeLine, new TimeProcessoListern() {
                    @Override
                    public void finish() {

                            aux++;

                    }
                });
            }
        });
*/
    }


    /*private void showProcesso(final ItemTimeLine processo, final TimeProcessoListern timeProcessoListern){

        final int[] count = {0};
        int margemTop = processo.getPosition()*60;
        int time = (processo.getTempo()+processo.getSobrecarga())*1000;
        final int timeAntSobrecarga = (time - (processo.getTempo()*1000));

        linha = getLayoutInflater().inflate(R.layout.item_linha, null);
        final LinearLayout.LayoutParams paramLinha = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

        View item = getLayoutInflater().inflate(R.layout.item_escalonamento, null);

        tv_tempo = item.findViewById(R.id.tv_tempo);
        tv_sobrecarga = item.findViewById(R.id.tv_sobrecarga);

        param = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        marginaux = frame_layout.getWidth();
        Log.i("LOG","Left: "+marginaux);
        param.leftMargin = marginaux;
        param.topMargin = margemTop;
        frame_layout.addView(item,param);

        new CountDownTimer(time,50){

            @Override
            public void onTick(long l) {
                if (l >= timeAntSobrecarga) {
                    tv_tempo.setWidth(tv_tempo.getWidth()+5);
                }else{
                    tv_sobrecarga.setWidth(tv_sobrecarga.getWidth()+5);
                }

                countToLines++;

                if (countToLines%18 == 0){
                    linha = getLayoutInflater().inflate(R.layout.item_linha, null);
                    ((TextView)linha.findViewById(R.id.tv_count)).setText(countLine+"");
                    ll_linhas.addView(linha,paramLinha);
                    countLine++;
                }

                horizontal_scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }

            @Override
            public void onFinish() {

                Log.i("LOG","COUNT: "+count[0]);
                if (timeProcessoListern != null)
                timeProcessoListern.finish();

            }
        }.start();
    }*/
}
