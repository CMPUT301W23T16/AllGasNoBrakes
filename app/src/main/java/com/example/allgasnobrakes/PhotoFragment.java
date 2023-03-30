package com.example.allgasnobrakes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Handles the camera fragment for taking a photo.
 * @author theresag
 * @version 1.0
 */

public class PhotoFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 246;
    private ImageView imgCamera;
    private ProgressDialog progressDialog;
    private Bitmap img;

    private String downloadURL;

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
        //final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.take_photo, container, false);

        Button btnCamera = root.findViewById(R.id.taking_photo);
        imgCamera = root.findViewById(R.id.captured_image);

        progressDialog = new ProgressDialog(getActivity());

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                someActivityResultLauncher.launch(iCamera);
            }
        });
        return root;
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        img = (Bitmap)(data.getExtras().get("data"));
                        imgCamera.setImageBitmap(img);
                        compressImages();
                    }
                }
            });

    private void compressImages() {
        progressDialog.setMessage("Images Uploading...");
        progressDialog.show();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        byte[] imgByte = stream.toByteArray();
        uploadImages(imgByte);
    }

    private void uploadImages(byte[] imgByte){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("images")
                .child("images"+System.currentTimeMillis()+"jpg");
        storageReference.putBytes(imgByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.cancel();
                Toast.makeText(getActivity(), "Image uploaded", Toast.LENGTH_SHORT).show();

                downloadURL = storageReference.getDownloadUrl().toString();
                Log.d("Upload", "Download URL: "+downloadURL);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}