package a84934.droidterpreter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import a84934.droidterpreter.GraphView.BlockType;

public class BlockTypeAdapter extends RecyclerView.Adapter<BlockTypeAdapter.VH>{

    BlockTypeAdapter(OnClickListener listener){
        super();
        this.listener = listener;
    }

    private final OnClickListener listener;

    public interface OnClickListener {
        void onBlockTypeClicked(int i);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.cell_block_type, parent, false);
        return new VH(root);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        holder.label.setText(BlockType.values()[position].name());
    }

    @Override
    public int getItemCount() {
        return BlockType.values().length;
    }

    class VH extends RecyclerView.ViewHolder {
        TextView label;
        View root;
        VH(View itemView) {
            super(itemView);
            root = itemView;
            label = itemView.findViewById(R.id.typeTextView);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onBlockTypeClicked(VH.this.getAdapterPosition());
                }
            });
        }
    }

}
