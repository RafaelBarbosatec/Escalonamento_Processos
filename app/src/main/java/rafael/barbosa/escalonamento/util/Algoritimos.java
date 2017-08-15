package rafael.barbosa.escalonamento.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rafael.barbosa.escalonamento.Cadastro.CadastroActivity;
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
                t_chegada_aux = p.getT_chegada();
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
                t_chegada_aux = p.getT_chegada();
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

    public static RespAlgoritimo ROUND_ROBIN(List<Processo> processosList){

        List<Processo> mProcessoList = new ArrayList<>(processosList);

        RespAlgoritimo respAlgoritimo = new RespAlgoritimo();

        List<ItemTimeLine> itemTimeLines = new ArrayList<>();

        Collections.sort(mProcessoList);

        int soma_times_turnaround = 0;

        int aux_processo = 0;
        int itens_rodada = calcularQtdItens(mProcessoList);
        Log.i("LOG","itens_rodada: "+itens_rodada);

        int t_chegada_aux= mProcessoList.get(0).getT_chegada();

        for (int i = 0; i < itens_rodada; i ++){

            Processo p = mProcessoList.get(aux_processo);
            ItemTimeLine itemTimeLine = new ItemTimeLine();
            itemTimeLine.setPosition(p.getPosition());
            itemTimeLine.setTempo_chegada(p.getT_chegada());

            //define inicio do item
            if (t_chegada_aux > p.getT_chegada()) {
                itemTimeLine.setTempo_inicio(t_chegada_aux);
            }else {
                t_chegada_aux = p.getT_chegada();
                itemTimeLine.setTempo_inicio(p.getT_chegada());
            }

            int time_executado;

            //Defini tempo de execução do item
            if (p.getT_execucao() > CadastroActivity.QUANTUM) {
                time_executado = CadastroActivity.QUANTUM;
                itemTimeLine.setTempo(CadastroActivity.QUANTUM);
                p.setT_execucao(p.getT_execucao() - CadastroActivity.QUANTUM);
            }else {
                time_executado = p.getT_execucao();
                itemTimeLine.setTempo(p.getT_execucao());
                p.setT_execucao(0);
                soma_times_turnaround +=((t_chegada_aux - p.getT_chegada())+time_executado);
            }

            mProcessoList.set(aux_processo,p);

            //Calcula o incio do próximo item e a existencia de sobrecarga
            if (p.getT_execucao() > 0) {
                itemTimeLine.setSobrecarga(CadastroActivity.SOBRECARGA);
                t_chegada_aux = t_chegada_aux + CadastroActivity.SOBRECARGA + time_executado;
            }else {
                t_chegada_aux = t_chegada_aux + time_executado;
            }

            itemTimeLines.add(itemTimeLine);
            aux_processo = (aux_processo+1)%mProcessoList.size();

            if (mProcessoList.get(aux_processo).getT_chegada() > t_chegada_aux){

                aux_processo = (aux_processo-1) % mProcessoList.size();

            }else if (mProcessoList.get(aux_processo).getT_execucao() == 0){

                int prox = aux_processo+1;
                aux_processo = (prox) % mProcessoList.size();

            }

        }

        respAlgoritimo.setItemTimeLines(itemTimeLines);
        double turnaround = (double)soma_times_turnaround/processosList.size();
        respAlgoritimo.setTurnaround(turnaround);

        return respAlgoritimo;
    }

    private static int calcularQtdItens(List<Processo> processosList) {
        int soma = 0;

        for (Processo p : processosList){
            soma += (int)Math.ceil( (double) p.getT_execucao() / CadastroActivity.QUANTUM);
        }

        Log.i("LOG","SOMA: "+soma);
        return soma;
    }
}
