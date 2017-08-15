package rafael.barbosa.escalonamento.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rafael.barbosa.escalonamento.Model.ItemTimeLine;
import rafael.barbosa.escalonamento.Model.Processo;

/**
 * Created by rafael on 14/08/17.
 */

public class Algoritimos {

    public static List<ItemTimeLine> FIFO(List<Processo> processosList){
        List<ItemTimeLine> itemTimeLines = new ArrayList<>();

        int position = 1;
        Collections.sort(processosList);
        int t_chegada_aux= processosList.get(0).getT_chegada();

        for (Processo p : processosList){

            ItemTimeLine itemTimeLine = new ItemTimeLine();
            itemTimeLine.setPosition(position);
            itemTimeLine.setTempo_chegada(t_chegada_aux);
            itemTimeLine.setTempo(p.getT_execucao());

            itemTimeLines.add(itemTimeLine);
            t_chegada_aux = t_chegada_aux + p.getT_execucao();
            position++;
        }


        return itemTimeLines;
    }
}
