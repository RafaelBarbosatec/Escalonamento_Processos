package rafael.barbosa.escalonamento.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;
import rafael.barbosa.escalonamento.Model.Processo;
import rafael.barbosa.escalonamento.R;

/**
 * Created by rafael on 06/05/16.
 */
public class ProcessoAdapter extends RecyclerView.Adapter<ProcessoAdapter.MyViewHolder> {

    private List<Processo> mlist;
    private LayoutInflater mLayoutInflater;
    private Context c;
    private ProcessoClickListern positionClickListern;


    public ProcessoAdapter(Context c, List<Processo> mlist,ProcessoClickListern positionClickListern) {
        this.mlist=mlist;
        this.positionClickListern = positionClickListern;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.c = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_processo, parent, false);

        MyViewHolder mvh = new MyViewHolder(v , viewType);
        return mvh;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Processo p = mlist.get(position);

        holder.tv_nome_proc.setText(p.getNome());
        holder.tv_t_chegada.setText(p.getT_chegada()+"");
        holder.tv_t_exe.setText(p.getT_execucao()+"");
        holder.tv_deadline.setText(p.getDeadline()+"");
        holder.tv_prioridade.setText(p.getPrioridade()+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionClickListern.processoClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        int n = mlist.size();


        return n;
    }

    public List<Processo> getList(){
        return mlist;
    }

    public void addListaItem(Processo p,int position) {
        mlist.add(p);
        notifyItemInserted(position);

    }

    public void replaceData(List<Processo> playerCampos){
        mlist = playerCampos;
        notifyDataSetChanged();
    }


    public void removeListItem(int position) {
        mlist.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nome_proc, tv_t_chegada, tv_t_exe, tv_deadline, tv_prioridade;

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);

            tv_nome_proc = itemView.findViewById(R.id.tv_nome_proc);
            tv_t_chegada = itemView.findViewById(R.id.tv_t_chegada);
            tv_t_exe = itemView.findViewById(R.id.tv_t_exe);
            tv_deadline = itemView.findViewById(R.id.tv_deadline);
            tv_prioridade = itemView.findViewById(R.id.tv_prioridade);

        }

    }

    public interface ProcessoClickListern{

        void processoClicked(int position);

    }

}
