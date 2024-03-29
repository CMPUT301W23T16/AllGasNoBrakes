package com.example.allgasnobrakes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allgasnobrakes.R;
import com.example.allgasnobrakes.models.HashedQR;

import java.util.ArrayList;
/**
 * RecycleView adapter
 * @author zhaoyu5
 * @version 1.0
 */
public class QrArrayAdapter extends RecyclerView.Adapter<QrArrayAdapter.ViewHolder> {

    private ArrayList<HashedQR> QR;
    private Context context;
    private ItemClickListener item;

    public QrArrayAdapter(ArrayList<HashedQR> QR, Context context, ItemClickListener item1) {
        this.QR = QR;
        this.context = context;
        this.item = item1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashedQR Qr = QR.get(position);

        holder.Hash.setText(Qr.getName());
        holder.Score.setText(Integer.toString(Qr.getScore()));

        holder.itemView.setOnClickListener(view -> {
            item.onItemClick(QR.get(position));
        });
    }

    public interface ItemClickListener{
        void onItemClick(HashedQR hashedQR);
    }

    @Override
    public int getItemCount() {
        return QR.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView Hash;
        private TextView Score;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Hash = (TextView) itemView.findViewById(R.id.Hash);
            Score = (TextView) itemView.findViewById(R.id.Score);
        }
    }
}