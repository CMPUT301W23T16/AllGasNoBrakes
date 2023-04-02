package com.example.allgasnobrakes.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

/**
 * Java PropertyChangeListener TextView for QR count
 * @author zhaoyu4
 * @version 2.0
 */
public class QRCountView extends androidx.appcompat.widget.AppCompatTextView implements PropertyChangeListener {
    public QRCountView(@NonNull Context context) {
        super(context);
    }

    public QRCountView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QRCountView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Number score = (Number) evt.getNewValue();
        setText(String.format(Locale.CANADA, "%d", score.intValue()));
        Log.d("CountUpdate", String.format(Locale.CANADA, "%d", score.intValue()));
    }
}
