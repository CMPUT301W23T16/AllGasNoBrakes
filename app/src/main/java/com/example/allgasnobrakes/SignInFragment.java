package com.example.allgasnobrakes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignInFragment extends Fragment {
    private Button registerButton;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEdittext;

    private FirebaseFirestore db;

    public SignInFragment() {
        super(R.layout.register);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register, container, false);

        registerButton = view.findViewById(R.id.registerbutton);
        usernameEditText = view.findViewById(R.id.username_edittext);
        emailEditText = view.findViewById(R.id.email_edittext);
        passwordEdittext = view.findViewById(R.id.password_edittext);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");

        registerButton.setOnClickListener(v -> {
            final String username = usernameEditText.getText().toString();
            final String email = emailEditText.getText().toString();
            final String password = passwordEdittext.getText().toString();

            HashMap<String, String> data = new HashMap<>();

            if (username.length() > 0 && email.length() > 0 && password.length() > 0) {
                data.put("Email", email);
                data.put("Password", password);
            }

            collectionReference
                    .document(username)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // These are a method which gets executed when the task is succeeded
                            Log.d("User", "Data has been added successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // These are a method which gets executed if there’s any problem
                            Log.d("User", "Data could not be added!" + e.toString());
                        }
                    });
        });
        return view;
    }
}
