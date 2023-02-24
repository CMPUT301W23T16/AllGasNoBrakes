package com.example.allgasnobrakes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QrArrayAdapter extends RecyclerView.Adapter<QrArrayAdapter.ViewHolder> {

    public QrArrayAdapter(ArrayList<HashedQR> QR, Context context) {
        this.QR = QR;
        this.context = context;
    }

    private ArrayList<HashedQR> QR;
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashedQR Qr = QR.get(position);

        holder.Hash.setText(Qr.getHashedQR());
        holder.Score.setText(Integer.toString(Qr.getScore()));
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
