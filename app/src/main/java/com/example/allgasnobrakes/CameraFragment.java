package com.example.allgasnobrakes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

public class CameraFragment extends DialogFragment {

    private CameraFragment listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.camera, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        CodeScanner mCodeScanner = new CodeScanner(getContext(), scannerView);
        mCodeScanner.startPreview();
        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);
        mCodeScanner.setAutoFocusMode(AutoFocusMode.CONTINUOUS);
        TextView t = view.findViewById(R.id.tv_textView);
        t.setText("test");
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        t.setText(result.getText());
                    }
                });
            }
        });
        return builder
                .setView(view)
    .create();
    }


}

