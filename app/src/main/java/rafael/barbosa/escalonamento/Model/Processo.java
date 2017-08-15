package rafael.barbosa.escalonamento.Model;

import android.support.annotation.NonNull;

/**
 * Created by rafael on 14/08/17.
 */

public class Processo implements Comparable<Processo>{

    private String nome;
    private int t_chegada = 0;
    private int t_execucao = 0;
    private int deadline = 0;
    private int prioridade = 0;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getT_chegada() {
        return t_chegada;
    }

    public void setT_chegada(int t_chegada) {
        this.t_chegada = t_chegada;
    }

    public int getT_execucao() {
        return t_execucao;
    }

    public void setT_execucao(int t_execucao) {
        this.t_execucao = t_execucao;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    @Override
    public int compareTo(@NonNull Processo processo) {

        if (t_chegada > processo.getT_chegada()) {
            return 1;
        }
        else if (t_chegada <  processo.getT_chegada()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
