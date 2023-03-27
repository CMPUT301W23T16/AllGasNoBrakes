package com.example.allgasnobrakes;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * Handles the camera fragment for taking a photo.
 * @author theresag
 * @version 1.0
 */

public class PhotoFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 246;

    public PhotoFragment() {
        super(R.layout.take_photo);
    }

    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the created view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.take_photo, container, false);

        Button camera_id = root.findViewById(R.id.taking_photo);

        //https://www.geeksforgeeks.org/how-to-open-camera-through-intent-and-display-captured-image-in-android/
        //How to connect the xml and java file for taking a photo using the camera intent in Android Studio
        camera_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://developer.android.com/training/camera/camera-intents
                //Official android developer page about using camera intents

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                assert activity != null;
                try {
                    activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Log.d("Camera", "onClick: "+e);
                }
            }
        });
        return root;
    }
}