package rafael.barbosa.escalonamento.Model;

/**
 * Created by dev on 02/08/17.
 */

public class ItemTimeLine {
    private int position;
    private int tempo_chegada;
    private int tempo;
    private int sobrecarga;

    public int getTempo_chegada() {
        return tempo_chegada;
    }

    public void setTempo_chegada(int tempo_chegada) {
        this.tempo_chegada = tempo_chegada;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getSobrecarga() {
        return sobrecarga;
    }

    public void setSobrecarga(int sobrecarga) {
        this.sobrecarga = sobrecarga;
    }
}
