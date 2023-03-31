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
 * Java Observer TextView for total QR score
 * @author zhaoyu4
 * @version 1.0
 */
public class ScoreView extends androidx.appcompat.widget.AppCompatTextView implements Observer {
    public ScoreView(@NonNull Context context) {
        super(context);
    }

    public ScoreView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScoreView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Updates the view with the new score
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers}
     *                 method.
     */
    @Override
    public void update(Observable o, Object arg) {
        setText(String.format(Locale.CANADA, "%d", ((PlayerProfile) o).getProfileSummary().getTotalScore()));
        Log.d("ScoreUpdate", String.format(Locale.CANADA, "%d", ((PlayerProfile) o).getProfileSummary().getTotalScore()));
    }
}
