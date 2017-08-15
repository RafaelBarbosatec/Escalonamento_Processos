package rafael.barbosa.escalonamento.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rafael.barbosa.escalonamento.Model.ItemTimeLine;
import rafael.barbosa.escalonamento.R;

/**
 * Created by dev on 03/08/17.
 */

public class TimeLineView extends LinearLayout {

    private HorizontalScrollView horizontal_scroll;
    private TextView tv_tempo, tv_sobrecarga, tv_espaco;
    private FrameLayout.LayoutParams param;
    private FrameLayout.LayoutParams paramLinha;
    private FrameLayout frame_layout;
    private ListView list_nome_processo;
    private ArrayAdapter<String> adapterNomes;
    private TextView timeline;
    private View linha;
    private LayoutInflater inflater;
    int marginaux = 0; // auxilia a saber qual a distacia a esquerda deve ta o item
    int countLine = 1; // conta os segundos da simulação das linhas(topo)
    int countSegundos = 1; // conta os segundos individuais de cada item ao simular
    int aux_sequencia = 0; // controla quantidade de itens na simulação
    private Context context;
    private List<ItemTimeLine> itemTimeLineList;

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
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.timeline_layout, this);
        assignUiElements();
    }

    private void assignUiElements() {
        list_nome_processo = findViewById(R.id.list_nome_processo);
        frame_layout = findViewById(R.id.frame_layout);
        horizontal_scroll =  findViewById(R.id.horizontal_scroll);
        timeline = findViewById(R.id.timeline);
    }


    public void setNomesProcessos(List<String> nomesProcessos) {
        adapterNomes = new ArrayAdapter<String>(context,R.layout.simple_list_item_custom, android.R.id.text1, nomesProcessos);
        list_nome_processo.setAdapter(adapterNomes);
    }

    public void addProcesso(final ItemTimeLine itemTimeLine, final TimeProcessoListern timeProcessoListern){

        int margemTop = itemTimeLine.getPosition()*60;
        final int time = (itemTimeLine.getTempo()+ itemTimeLine.getSobrecarga()+(itemTimeLine.getTempo_inicio() - (countLine-1)))*1000;
        final int timeAntSobrecarga = (time - ((itemTimeLine.getTempo()+(itemTimeLine.getTempo_inicio() - (countLine-1)))*1000));

        linha = inflater.inflate(R.layout.item_linha, null);
        paramLinha = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);

        View item = inflater.inflate(R.layout.item_escalonamento, null);
        param = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        tv_tempo = item.findViewById(R.id.tv_tempo);
        tv_sobrecarga = item.findViewById(R.id.tv_sobrecarga);
        tv_espaco = item.findViewById(R.id.tv_espaco);

        marginaux = frame_layout.getWidth();

        param.leftMargin = marginaux;
        param.topMargin = margemTop;

        frame_layout.addView(item,param);

        new CountDownTimer(time,50){

            @Override
            public void onTick(long l) {

                int timeAtual = countLine-1;
                if (itemTimeLine.getTempo_inicio() <= timeAtual) {
                    if (l >= timeAntSobrecarga) {
                        tv_tempo.setWidth(tv_tempo.getWidth() + 5);
                    } else {
                        tv_sobrecarga.setWidth(tv_sobrecarga.getWidth() + 5);
                    }
                }else {
                    tv_espaco.setWidth(tv_espaco.getWidth() + 5);
                }

                //Atualiza linha timiline acima do gráfico
                timeline.setWidth(timeline.getWidth() + 5);

                //Controca quando adicionar linha vertical
                if (l < (time-(countSegundos*1000))){
                    linha = inflater.inflate(R.layout.item_linha, null);
                    paramLinha = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
                    paramLinha.leftMargin = timeline.getWidth();
                    ((TextView)linha.findViewById(R.id.tv_count)).setText(countLine+"");
                    frame_layout.addView(linha,paramLinha);
                    countLine++;
                    countSegundos++;
                }

                //Força o scroll sempre seguir o progresso a direita
                horizontal_scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }

            @Override
            public void onFinish() {

                countSegundos = 1;

                linha = inflater.inflate(R.layout.item_linha, null);
                paramLinha = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
                paramLinha.leftMargin = timeline.getWidth();
                ((TextView)linha.findViewById(R.id.tv_count)).setText(countLine+"");
                frame_layout.addView(linha,paramLinha);

                countLine++;

                if (timeProcessoListern != null)
                    timeProcessoListern.finish();

            }
        }.start();
    }

    public void setProcessoList(List<ItemTimeLine> itemTimeLineList) {
        this.itemTimeLineList = itemTimeLineList;
    }

    public void startSequencia(){
        if (aux_sequencia < itemTimeLineList.size()){
            ItemTimeLine p = itemTimeLineList.get(aux_sequencia);
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
