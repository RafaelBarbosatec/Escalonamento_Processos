package rafael.barbosa.escalonamento.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rafael.barbosa.escalonamento.Model.ItemTimeLine;
import rafael.barbosa.escalonamento.Model.Processo;
import rafael.barbosa.escalonamento.Model.RespAlgoritimo;

/**
 * Created by rafael on 14/08/17.
 */

public class Algoritimos {

    public static RespAlgoritimo FIFO(List<Processo> processosList){

        RespAlgoritimo respAlgoritimo = new RespAlgoritimo();

        List<ItemTimeLine> itemTimeLines = new ArrayList<>();

        int total_time = 0;

        Collections.sort(processosList);
        int t_chegada_aux= processosList.get(0).getT_chegada();

        for (Processo p : processosList){

            ItemTimeLine itemTimeLine = new ItemTimeLine();
            itemTimeLine.setPosition(p.getPosition());

            if (t_chegada_aux > p.getT_chegada()) {
                itemTimeLine.setTempo_inicio(t_chegada_aux);
            }else {
                itemTimeLine.setTempo_inicio(p.getT_chegada());
            }

            itemTimeLine.setTempo_chegada(p.getT_chegada());
            itemTimeLine.setTempo(p.getT_execucao());

            total_time +=((t_chegada_aux - p.getT_chegada())+p.getT_execucao());

            itemTimeLines.add(itemTimeLine);
            t_chegada_aux = t_chegada_aux + p.getT_execucao();

        }

        double turnaround = (double)total_time/processosList.size();
        Log.i("LOG","TURNAROUND: "+turnaround);

        respAlgoritimo.setItemTimeLines(itemTimeLines);
        respAlgoritimo.setTurnaround(turnaround);
        return respAlgoritimo;

    }

    public static RespAlgoritimo SJF(List<Processo> processosList){

        RespAlgoritimo respAlgoritimo = new RespAlgoritimo();

        List<ItemTimeLine> itemTimeLines = new ArrayList<>();

        Collections.sort(processosList, new Comparator<Processo>() {
            @Override
            public int compare(Processo processo, Processo t1) {
                if (processo.getT_execucao() > t1.getT_execucao()) {
                    return 1;
                }
                else if (processo.getT_execucao() <  t1.getT_execucao()) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });

        int total_time = 0;

        int t_chegada_aux= processosList.get(0).getT_chegada();

        for (Processo p : processosList) {

            ItemTimeLine itemTimeLine = new ItemTimeLine();
            itemTimeLine.setPosition(p.getPosition());

            if (t_chegada_aux > p.getT_chegada()) {
                itemTimeLine.setTempo_inicio(t_chegada_aux);
            }else {
                itemTimeLine.setTempo_inicio(p.getT_chegada());
            }

            itemTimeLine.setTempo_chegada(p.getT_chegada());
            itemTimeLine.setTempo(p.getT_execucao());

            total_time +=((t_chegada_aux - p.getT_chegada())+p.getT_execucao());

            itemTimeLines.add(itemTimeLine);
            t_chegada_aux = t_chegada_aux + p.getT_execucao();

        }

        double turnaround = (double)total_time/processosList.size();
        Log.i("LOG","TURNAROUND: "+turnaround);

        respAlgoritimo.setItemTimeLines(itemTimeLines);
        respAlgoritimo.setTurnaround(turnaround);

        return respAlgoritimo;
    }
}
