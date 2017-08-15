package rafael.barbosa.escalonamento.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dev on 02/08/17.
 */

public class ItemTimeLine implements Parcelable {
    private int position;
    private int tempo_chegada;
    private int tempo;
    private int sobrecarga = 0;

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.position);
        dest.writeInt(this.tempo_chegada);
        dest.writeInt(this.tempo);
        dest.writeInt(this.sobrecarga);
    }

    public ItemTimeLine() {
    }

    protected ItemTimeLine(Parcel in) {
        this.position = in.readInt();
        this.tempo_chegada = in.readInt();
        this.tempo = in.readInt();
        this.sobrecarga = in.readInt();
    }

    public static final Parcelable.Creator<ItemTimeLine> CREATOR = new Parcelable.Creator<ItemTimeLine>() {
        @Override
        public ItemTimeLine createFromParcel(Parcel source) {
            return new ItemTimeLine(source);
        }

        @Override
        public ItemTimeLine[] newArray(int size) {
            return new ItemTimeLine[size];
        }
    };
}
