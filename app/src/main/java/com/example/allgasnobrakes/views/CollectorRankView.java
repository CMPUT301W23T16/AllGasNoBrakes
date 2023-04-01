package com.example.allgasnobrakes.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.allgasnobrakes.R;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

public class CollectorRankView extends androidx.appcompat.widget.AppCompatTextView implements PropertyChangeListener {
    public CollectorRankView(@NonNull Context context) {
        super(context);
    }

    public CollectorRankView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CollectorRankView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Number rank = (Number) evt.getNewValue();
        if (rank.intValue() <= 0) {
            setText(R.string.not_on_collector_message);
            Log.d("collector", "not updated");
        } else {
            String rankText = String.format(Locale.CANADA,
                    "No. %d in The Hardcore Collectors", rank.intValue());
            Log.d("collector", rankText);
            setText(rankText);
        }

    }
}
