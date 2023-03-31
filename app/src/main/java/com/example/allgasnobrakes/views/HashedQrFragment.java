package com.example.allgasnobrakes.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.allgasnobrakes.models.HashedQR;
import com.example.allgasnobrakes.R;

/**
 * HashedQrFragment, displays all information about QRfragment
 * @author zhaoyu5
 * @version 1.0
 */
public class HashedQrFragment extends DialogFragment {
    private HashedQR QR;
    private String comment;
    private String longitude;
    private String latitude;
    public void main(HashedQR qr1,String comment,String longitude,String latitude) {
        // Get information from the selected city to be edited
        this.QR = qr1;
        this.comment = comment;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hashedqr, null);
        TextView name = view.findViewById(R.id.name);
        TextView score = view.findViewById(R.id.score);
        TextView face = view.findViewById(R.id.face);
        TextView comment = view.findViewById(R.id.comment);
        TextView longitude = view.findViewById(R.id.longitude);
        TextView latitude = view.findViewById(R.id.latitude);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        name.setText(QR.getName());
        score.setText(Integer.toString(QR.getScore()));
        face.setText(QR.getFace());
        comment.setText(this.comment);
        longitude.setText("Longitude: "+this.longitude);
        latitude.setText("Latitude: "+this.latitude);
        return builder
                .setView(view)
                .setNegativeButton("Exit", null)
                .create();
    }
}
