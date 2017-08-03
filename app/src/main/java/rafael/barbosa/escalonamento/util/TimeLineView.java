package rafael.barbosa.escalonamento.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import rafael.barbosa.escalonamento.Model.Processo;
import rafael.barbosa.escalonamento.R;

/**
 * Created by dev on 03/08/17.
 */

public class TimeLineView extends LinearLayout {

    private FrameLayout frame_layout;
    private HorizontalScrollView horizontal_scroll;
    private LinearLayout ll_linhas;
    private View linha;
    private LayoutInflater inflater;
    private TextView tv_tempo, tv_sobrecarga;
    private FrameLayout.LayoutParams param;
    int marginaux = 0;
    int countLine = 1;
    int countToLines = 0;
    int aux_sequencia = 0;
    private List<Processo> processoList;

    public TimeLineView(Context context) {
        super(context);
    }

    public TimeLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public TimeLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    private void initControl(Context context, AttributeSet attrs) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.timeline_layout, this);
        assignUiElements();
    }

    private void assignUiElements() {
        frame_layout = findViewById(R.id.frame_layout);
        horizontal_scroll =  findViewById(R.id.horizontal_scroll);
        ll_linhas =  findViewById(R.id.ll_linhas);
    }

    public void addProcesso(final Processo processo, final TimeProcessoListern timeProcessoListern){

        int margemTop = processo.getPosition()*60;
        int time = (processo.getTempo()+processo.getSobrecarga())*1000;
        final int timeAntSobrecarga = (time - (processo.getTempo()*1000));

        linha = inflater.inflate(R.layout.item_linha, null);
        final LinearLayout.LayoutParams paramLinha = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        View item = inflater.inflate(R.layout.item_escalonamento, null);
        tv_tempo = item.findViewById(R.id.tv_tempo);
        tv_sobrecarga = item.findViewById(R.id.tv_sobrecarga);

        param = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        marginaux = frame_layout.getWidth();
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
                    linha = inflater.inflate(R.layout.item_linha, null);
                    ((TextView)linha.findViewById(R.id.tv_count)).setText(countLine+"");
                    ll_linhas.addView(linha,paramLinha);
                    countLine++;
                }

                horizontal_scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }

            @Override
            public void onFinish() {

                if (timeProcessoListern != null)
                    timeProcessoListern.finish();

            }
        }.start();
    }

    public void setProcessoList(List<Processo> processoList) {
        this.processoList = processoList;
    }

    public void startSequencia(){
        if (aux_sequencia < processoList.size()){
            Processo p = processoList.get(aux_sequencia);
            aux_sequencia++;
            addProcesso(p, new TimeProcessoListern() {
                @Override
                public void finish() {
                    startSequencia();
                }
            });
        }

    }

    public interface TimeProcessoListern{
        void finish();
    }
}
