package com.example.allgasnobrakes;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Handles operations with code scanner
 * @author zhaoyu4
 * @version 1.0
 */
public class ScannerFragment extends Fragment {
    private CodeScanner mCodeScanner;
    public ScannerFragment() {
        super(R.layout.scanner);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.scanner, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.startPreview();
        TextView t = root.findViewById(R.id.tv_textView);
        TextView l = root.findViewById(R.id.tv2_textView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String sha256hex = DigestUtils.sha256Hex(result.getText());
                        t.setText(sha256hex);
                        char starting = sha256hex.charAt(0);
                        String current = "";
                        int total = 0;
                        for (int i = 1; i < sha256hex.length(); i++){
                            if (starting != sha256hex.charAt(i)){
                                starting = sha256hex.charAt(i);
                                if (current.length() != 0){
                                    String hex = String.format("%c",current.charAt(0));
                                    int integer = Integer.parseInt(hex, 16);
                                    total += Math.pow(integer,current.length());
                                }
                                current = "";
                            }
                            else{
                                current = current + starting;
                            }
                        }
                        l.setText(Integer.toString(total));
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        return root;
    }
}
