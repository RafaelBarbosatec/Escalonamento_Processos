package rafael.barbosa.escalonamento.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
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

        int position = 1;
        Collections.sort(processosList);
        int t_chegada_aux= processosList.get(0).getT_chegada();

        for (Processo p : processosList){

            ItemTimeLine itemTimeLine = new ItemTimeLine();
            itemTimeLine.setPosition(position);
            itemTimeLine.setTempo_chegada(t_chegada_aux);
            itemTimeLine.setTempo(p.getT_execucao());

            total_time +=((t_chegada_aux - p.getT_chegada())+p.getT_execucao());

            itemTimeLines.add(itemTimeLine);
            t_chegada_aux = t_chegada_aux + p.getT_execucao();
            position++;
        }

        double turnaround = (double)total_time/processosList.size();
        Log.i("LOG","TURNAROUND: "+turnaround);

        respAlgoritimo.setItemTimeLines(itemTimeLines);
        respAlgoritimo.setTurnaround(turnaround);
        return respAlgoritimo;
    }
}
