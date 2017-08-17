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

        List<Integer> timesToTurnaround = new ArrayList<>();

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
            //timesToTurnaround.add(total_time);

            itemTimeLines.add(itemTimeLine);
            t_chegada_aux = t_chegada_aux + p.getT_execucao();

        }

        double turnaround = calcularTurnaround(timesToTurnaround,processosList.size());

        respAlgoritimo.setItemTimeLines(itemTimeLines);
        respAlgoritimo.setTurnaround((double) total_time/processosList.size());
        return respAlgoritimo;

    }

    public static RespAlgoritimo SJF(List<Processo> processosList){

        RespAlgoritimo respAlgoritimo = new RespAlgoritimo();

        List<ItemTimeLine> itemTimeLines = new ArrayList<>();

        List<Integer> timesToTurnaround = new ArrayList<>();

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


        Collections.sort(processosList);

        int total_time = 0;

        int aux_processo = 0;

        int itens_rodada = processosList.size();

        int t_chegada_aux= processosList.get(0).getT_chegada();

        for (int i = 0; i < itens_rodada; i ++){

            Processo p = processosList.get(aux_processo);
            ItemTimeLine itemTimeLine = new ItemTimeLine();
            itemTimeLine.setPosition(p.getPosition());

            if (t_chegada_aux > p.getT_chegada()) {
                itemTimeLine.setTempo_inicio(t_chegada_aux);
            }else {
                t_chegada_aux = p.getT_chegada();
                itemTimeLine.setTempo_inicio(p.getT_chegada());
            }

            int tempo_executado = p.getT_execucao();

            itemTimeLine.setTempo_chegada(p.getT_chegada());
            itemTimeLine.setTempo(p.getT_execucao());

            p.setT_execucao(0);
            processosList.set(aux_processo,p);

            total_time +=((t_chegada_aux - p.getT_chegada())+tempo_executado);
            //timesToTurnaround.add(total_time);

            itemTimeLines.add(itemTimeLine);
            t_chegada_aux = t_chegada_aux + tempo_executado;

            aux_processo = (aux_processo + 1) % processosList.size();

            int max_pri = aux_processo;

            int aux_prox = aux_processo;

            for(int j = 0; j< processosList.size(); j++) {

                if (processosList.get(aux_prox).getT_chegada() <= t_chegada_aux
                        && processosList.get(aux_prox).getT_execucao() <
                        processosList.get(max_pri).getT_execucao()
                        && processosList.get(aux_prox).getT_execucao() != 0
                        || processosList.get(max_pri).getT_execucao() == 0){

                    max_pri = aux_prox;

                }

                aux_prox = (aux_prox + 1) % processosList.size();

            }

            aux_processo = max_pri;

            Log.i("LOG","AUX: "+aux_processo);

        }

       /* for (Processo p : processosList) {

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
            //timesToTurnaround.add(total_time);

            itemTimeLines.add(itemTimeLine);
            t_chegada_aux = t_chegada_aux + p.getT_execucao();

        }*/

        double turnaround = calcularTurnaround(timesToTurnaround,processosList.size());

        respAlgoritimo.setItemTimeLines(itemTimeLines);
        respAlgoritimo.setTurnaround((double) total_time/processosList.size());

        return respAlgoritimo;
    }

    public static RespAlgoritimo ROUND_ROBIN(List<Processo> processosList){

        //Log.i("LOG","LOG: "+processosList.size());
        RespAlgoritimo respAlgoritimo = new RespAlgoritimo();

        List<ItemTimeLine> itemTimeLines = new ArrayList<>();

        List<Integer> timesToTurnaround = new ArrayList<>();

        Collections.sort(processosList);

        int soma_times_turnaround = 0;

        int aux_processo = 0;
        int itens_rodada = calcularQtdItens(processosList);

        int t_chegada_aux= processosList.get(0).getT_chegada();

        for (int i = 0; i < itens_rodada; i ++){

            Processo p = processosList.get(aux_processo);
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
            }

            processosList.set(aux_processo,p);

            //Calcula o incio do próximo item e a existencia de sobrecarga
            if (p.getT_execucao() > 0) {
                itemTimeLine.setSobrecarga(CadastroActivity.SOBRECARGA);
                t_chegada_aux = t_chegada_aux + CadastroActivity.SOBRECARGA + time_executado;
            }else {
                t_chegada_aux = t_chegada_aux + time_executado;
            }

            // Soma dos tempos ao terminar processo
            if (p.getT_execucao() == 0){
                soma_times_turnaround +=(t_chegada_aux - p.getT_chegada());
               // timesToTurnaround.add(soma_times_turnaround);
            }

            itemTimeLines.add(itemTimeLine);

            aux_processo = (aux_processo+1) % processosList.size();

            /**
             * Verifica se tem processo a executar no proximo tempo, se não tiver repete proncesso
             * Caso contrário verifica se o pŕoximo ja terminou se sim procura o próximo
             */
            if (processosList.get(aux_processo).getT_chegada() > t_chegada_aux){

                if (p.getT_execucao() != 0) {

                    aux_processo = (aux_processo - 1) % processosList.size();

                }else {

                    for(int j = 0; j< processosList.size(); j++) {

                        if (processosList.get(aux_processo).getT_chegada() > t_chegada_aux || processosList.get(aux_processo).getT_execucao() == 0){
                            aux_processo = (aux_processo -1 ) % processosList.size();
                        }else{
                            break;
                        }
                    }

                }

            }else if (processosList.get(aux_processo).getT_execucao() == 0){

                aux_processo = (aux_processo+1) % processosList.size();

                for(int j = 0; j< processosList.size(); j++) {

                    if (processosList.get(aux_processo).getT_execucao() == 0){
                        aux_processo = (aux_processo + 1) % processosList.size();
                    }else{
                        break;
                    }
                }

            }

        }

        respAlgoritimo.setItemTimeLines(itemTimeLines);
        double turnaround = calcularTurnaround(timesToTurnaround,processosList.size());
        respAlgoritimo.setTurnaround((double)soma_times_turnaround/processosList.size());

        return respAlgoritimo;
    }

    public static RespAlgoritimo PRIORIDADE(List<Processo> processosList){

        //Log.i("LOG","LOG: "+processosList.size());
        RespAlgoritimo respAlgoritimo = new RespAlgoritimo();

        List<ItemTimeLine> itemTimeLines = new ArrayList<>();

        List<Integer> timesToTurnaround = new ArrayList<>();

        Collections.sort(processosList, new Comparator<Processo>() {
            @Override
            public int compare(Processo processo, Processo t1) {
                if (processo.getPrioridade() < t1.getPrioridade()) {
                    return 1;
                }
                else if (processo.getPrioridade() >  t1.getPrioridade()) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });

        Collections.sort(processosList);

        int soma_times_turnaround = 0;

        int aux_processo = 0;
        int itens_rodada = calcularQtdItens(processosList);

        int t_chegada_aux= processosList.get(0).getT_chegada();

        for (int i = 0; i < itens_rodada; i ++){

            Processo p = processosList.get(aux_processo);
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
            }

            processosList.set(aux_processo,p);

            //Calcula o incio do próximo item e a existencia de sobrecarga
            if (p.getT_execucao() > 0) {
                itemTimeLine.setSobrecarga(CadastroActivity.SOBRECARGA);
                t_chegada_aux = t_chegada_aux + CadastroActivity.SOBRECARGA + time_executado;
            }else {
                t_chegada_aux = t_chegada_aux + time_executado;
            }

            // Soma dos tempos ao terminar processo
            if (p.getT_execucao() == 0){
                soma_times_turnaround +=(t_chegada_aux - p.getT_chegada());
                //timesToTurnaround.add(soma_times_turnaround);
            }

            itemTimeLines.add(itemTimeLine);

            aux_processo = (aux_processo+1) % processosList.size();

            /**
             * Verifica se tem processo a executar no proximo tempo, se não tiver repete proncesso
             * Caso contrário verifica se o pŕoximo ja terminou se sim procura o próximo com mairo prioridade
             */
            if (processosList.get(aux_processo).getT_chegada() > t_chegada_aux){

                if (p.getT_execucao() != 0 ) {

                    aux_processo = (aux_processo - 1) % processosList.size();
                    if (aux_processo == -1)
                        aux_processo = processosList.size() -1;

                }else {

                    for(int j = 0; j< processosList.size(); j++) {

                        if (processosList.get(aux_processo).getT_chegada() > t_chegada_aux
                                || processosList.get(aux_processo).getT_execucao() == 0){

                            aux_processo = (aux_processo - 1 ) % processosList.size();
                            if (aux_processo == -1)
                                aux_processo = processosList.size() -1;

                        }else{
                            break;
                        }
                    }

                }

            }else if (processosList.get(aux_processo).getT_execucao() == 0){

                int max_pri = aux_processo;

                for(int j = 0; j< processosList.size(); j++) {

                    if (processosList.get(aux_processo).getT_chegada() <= t_chegada_aux
                            && processosList.get(aux_processo).getPrioridade() >
                            processosList.get(max_pri).getPrioridade()
                            && processosList.get(aux_processo).getT_execucao() != 0
                            || processosList.get(max_pri).getT_execucao() == 0){

                        max_pri = aux_processo;

                    }

                    aux_processo = (aux_processo + 1) % processosList.size();

                }

                aux_processo = max_pri;

            }else {

                int max_pri = aux_processo;
                for(int j = 0; j< processosList.size(); j++) {

                    if (processosList.get(aux_processo).getT_chegada() <= t_chegada_aux
                            && processosList.get(aux_processo).getPrioridade() >
                            processosList.get(max_pri).getPrioridade()
                            && processosList.get(aux_processo).getT_execucao() != 0){

                        max_pri = aux_processo;

                    }
                    aux_processo = (aux_processo + 1) % processosList.size();

                }

                aux_processo = max_pri;

            }

        }

        respAlgoritimo.setItemTimeLines(itemTimeLines);
        double turnaround = calcularTurnaround(timesToTurnaround,processosList.size());
        respAlgoritimo.setTurnaround((double)soma_times_turnaround/processosList.size());

        return respAlgoritimo;
    }

    public static RespAlgoritimo EDF(List<Processo> processosList){

        //Log.i("LOG","LOG: "+processosList.size());
        RespAlgoritimo respAlgoritimo = new RespAlgoritimo();

        List<ItemTimeLine> itemTimeLines = new ArrayList<>();

        List<Integer> timesToTurnaround = new ArrayList<>();

        Collections.sort(processosList, new Comparator<Processo>() {
            @Override
            public int compare(Processo processo, Processo t1) {
                if (processo.getDeadline() > t1.getDeadline()) {
                    return 1;
                }
                else if (processo.getDeadline() < t1.getDeadline()) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });

        Collections.sort(processosList);

        int soma_times_turnaround = 0;

        int aux_processo = 0;
        int itens_rodada = calcularQtdItens(processosList);

        int t_chegada_aux= processosList.get(0).getT_chegada();

        for (int i = 0; i < itens_rodada; i ++){

            Processo p = processosList.get(aux_processo);
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
            }

            processosList.set(aux_processo,p);

            //Calcula o incio do próximo item e a existencia de sobrecarga
            if (p.getT_execucao() > 0) {
                itemTimeLine.setSobrecarga(CadastroActivity.SOBRECARGA);
                t_chegada_aux = t_chegada_aux + CadastroActivity.SOBRECARGA + time_executado;
            }else {
                t_chegada_aux = t_chegada_aux + time_executado;
            }

            // Soma dos tempos ao terminar processo
            if (p.getT_execucao() == 0){
                soma_times_turnaround +=(t_chegada_aux - p.getT_chegada());
               // timesToTurnaround.add(soma_times_turnaround);
            }

            itemTimeLines.add(itemTimeLine);

            aux_processo = (aux_processo+1) % processosList.size();

            /**
             * Verifica se tem processo a executar no proximo tempo, se não tiver repete proncesso
             * Caso contrário verifica se o pŕoximo ja terminou se sim procura o próximo com mairo prioridade
             */
            if (processosList.get(aux_processo).getT_chegada() > t_chegada_aux){

                if (p.getT_execucao() != 0 ) {

                    aux_processo = (aux_processo - 1) % processosList.size();
                    if (aux_processo == -1)
                        aux_processo = processosList.size() -1;

                }else {

                    for(int j = 0; j< processosList.size(); j++) {

                        if (processosList.get(aux_processo).getT_chegada() > t_chegada_aux
                                || processosList.get(aux_processo).getT_execucao() == 0){

                            aux_processo = (aux_processo - 1 ) % processosList.size();
                            if (aux_processo == -1)
                                aux_processo = processosList.size() -1;

                        }else{
                            break;
                        }
                    }

                }

            }else if (processosList.get(aux_processo).getT_execucao() == 0){

                int min_pri = aux_processo;

                for(int j = 0; j< processosList.size(); j++) {

                    if (processosList.get(aux_processo).getT_chegada() <= t_chegada_aux
                            && processosList.get(aux_processo).getDeadline() <
                            processosList.get(min_pri).getDeadline()
                            && processosList.get(aux_processo).getT_execucao() != 0
                            || processosList.get(min_pri).getT_execucao() == 0){

                        min_pri = aux_processo;

                    }

                    aux_processo = (aux_processo + 1) % processosList.size();

                }

                aux_processo = min_pri;

            }else {

                int max_pri = aux_processo;
                for(int j = 0; j< processosList.size(); j++) {

                    if (processosList.get(aux_processo).getT_chegada() <= t_chegada_aux
                            && processosList.get(aux_processo).getDeadline() <
                            processosList.get(max_pri).getDeadline()
                            && processosList.get(aux_processo).getT_execucao() != 0){

                        max_pri = aux_processo;

                    }
                    aux_processo = (aux_processo + 1) % processosList.size();

                }

                aux_processo = max_pri;

            }

        }

        respAlgoritimo.setItemTimeLines(itemTimeLines);
        double turnaround = calcularTurnaround(timesToTurnaround,processosList.size());
        respAlgoritimo.setTurnaround((double) soma_times_turnaround / processosList.size());

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

    private static double calcularTurnaround(List<Integer> integerLis,int countProc){

        double total = 0;

        for (int i : integerLis){
            total += i;
        }
        Log.i("LOG","total: "+total+"/"+countProc);
        return total/countProc;
    }
}
