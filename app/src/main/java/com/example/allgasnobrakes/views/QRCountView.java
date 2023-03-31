package com.example.allgasnobrakes.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.allgasnobrakes.models.PlayerProfile;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

/**
 * Java Observer TextView for QR count
 * @author zhaoyu4
 * @version 1.0
 */
public class QRCountView extends androidx.appcompat.widget.AppCompatTextView implements Observer {
    public QRCountView(@NonNull Context context) {
        super(context);
    }

    public QRCountView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QRCountView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Updates the view with the new QR count
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers}
     *                 method.
     */
    @Override
    public void update(Observable o, Object arg) {
        setText(String.format(Locale.CANADA, "%d", ((PlayerProfile) o).getProfileSummary().getTotalQR()));
        Log.d("CountUpdate", String.format(Locale.CANADA, "%d", ((PlayerProfile) o).getProfileSummary().getTotalQR()));
    }
}
