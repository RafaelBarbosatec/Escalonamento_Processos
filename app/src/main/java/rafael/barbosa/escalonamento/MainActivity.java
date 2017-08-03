package rafael.barbosa.escalonamento;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rafael.barbosa.escalonamento.Model.Processo;
import rafael.barbosa.escalonamento.util.TimeLineView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frame_layout;
    int marginaux = 0;
    int aux = 1;
    int countLine = 1;
    int countToLines = 0;
    private FrameLayout.LayoutParams param;
    private LinearLayout ll_linhas;
    private View linha;
    private TextView tv_tempo, tv_sobrecarga;
    private HorizontalScrollView horizontal_scroll;
    private Button bt_start;
    private TimeLineView timeLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeLine = (TimeLineView) findViewById(R.id.timeLine);

        bt_start = (Button) findViewById(R.id.bt_start);

        List<Processo> processoList = new ArrayList<>();

        Processo processo = new Processo();
        processo.setPosition(1);
        processo.setTempo(1);
        processo.setSobrecarga(0);
        processoList.add(processo);

        processo = new Processo();
        processo.setPosition(2);
        processo.setTempo(2);
        processo.setSobrecarga(1);
        processoList.add(processo);

        processo = new Processo();
        processo.setPosition(3);
        processo.setTempo(2);
        processo.setSobrecarga(0);
        processoList.add(processo);

        processo = new Processo();
        processo.setPosition(1);
        processo.setTempo(2);
        processo.setSobrecarga(1);
        processoList.add(processo);

        timeLine.setProcessoList(processoList);


        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeLine.startSequencia();
            }
        });

       /* frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
        horizontal_scroll = (HorizontalScrollView) findViewById(R.id.horizontal_scroll);
        bt_start = (Button) findViewById(R.id.bt_start);
        ll_linhas = (LinearLayout) findViewById(R.id.ll_linhas);

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Processo processo = new Processo();
                processo.setPosition(aux);
                processo.setTempo(2);
                processo.setSobrecarga(1);
                showProcesso(processo, new TimeProcessoListern() {
                    @Override
                    public void finish() {

                            aux++;

                    }
                });
            }
        });
*/
    }


    private void showProcesso(final Processo processo, final TimeProcessoListern timeProcessoListern){

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
    }
}

interface TimeProcessoListern{
    void finish();
}
