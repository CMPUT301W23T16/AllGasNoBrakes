package com.example.allgasnobrakes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * HashedQrFragment, displays all information about QRfragment
 * @author zhaoyu5
 * @version 1.0
 */
public class HashedQrFragment extends DialogFragment {
    private HashedQR QR;
    private String comment;

    public void main(HashedQR qr1,String comment) {
        // Get information from the selected city to be edited
        this.QR = qr1;
        this.comment = comment;
    }
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hashedqr, null);
        TextView name = view.findViewById(R.id.name);
        TextView score = view.findViewById(R.id.score);
        TextView face = view.findViewById(R.id.face);
        TextView comment = view.findViewById(R.id.comment);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        name.setText(QR.getName());
        score.setText(Integer.toString(QR.getScore()));
        face.setText(QR.getFace());
        comment.setText(this.comment);
        return builder
                .setView(view)
                .setNegativeButton("Exit", null)
                .create();
    }
}
