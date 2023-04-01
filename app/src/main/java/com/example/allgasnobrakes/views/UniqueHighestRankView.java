package com.example.allgasnobrakes.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

public class UniqueHighestRankView extends androidx.appcompat.widget.AppCompatTextView implements PropertyChangeListener {
    public UniqueHighestRankView(@NonNull Context context) {
        super(context);
    }

    public UniqueHighestRankView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UniqueHighestRankView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Number rank = (Number) evt.getNewValue();
        String rankText = String.format(Locale.CANADA,
                "No. %d in The One and Only", rank.intValue());
        Log.d("unique highest", rankText);
        setText(rankText);
    }
}
