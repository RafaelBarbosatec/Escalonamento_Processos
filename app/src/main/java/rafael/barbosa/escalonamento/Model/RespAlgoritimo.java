package rafael.barbosa.escalonamento.Model;

import java.util.List;

/**
 * Created by rafael on 15/08/17.
 */

public class RespAlgoritimo {

    private List<ItemTimeLine> itemTimeLines;
    private double turnaround;

    public List<ItemTimeLine> getItemTimeLines() {
        return itemTimeLines;
    }

    public void setItemTimeLines(List<ItemTimeLine> itemTimeLines) {
        this.itemTimeLines = itemTimeLines;
    }

    public double getTurnaround() {
        return turnaround;
    }

    public void setTurnaround(double turnaround) {
        this.turnaround = turnaround;
    }
}
