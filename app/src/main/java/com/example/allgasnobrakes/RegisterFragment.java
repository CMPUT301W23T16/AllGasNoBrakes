package com.example.allgasnobrakes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.HashMap;

/**
 * Handles first-time user (registration) operations.
 * - Checks that the username is unique before the user account is made
 * @author zhaoyu4 and theresag
 * @version 1.1
 */
//Notes for later: 
public class RegisterFragment extends Fragment {
    private Button registerButton;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEdittext;

    private FirebaseFirestore db;
    private Leaderboard viewModel;

    public RegisterFragment() {
        super(R.layout.register);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(Leaderboard.class);
        View view = inflater.inflate(R.layout.register, container, false);
        String deviceID = requireArguments().getString("deviceID");

        registerButton = view.findViewById(R.id.registerbutton);
        usernameEditText = view.findViewById(R.id.username_edittext);
        emailEditText = view.findViewById(R.id.email_edittext);
        passwordEdittext = view.findViewById(R.id.password_edittext);

        db = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener(v -> {
            final CollectionReference collectionReference = db.collection("Users");
            final CollectionReference deviceReference = db.collection("DeviceID");
            final String username = usernameEditText.getText().toString();
            final String email = emailEditText.getText().toString();
            final String password = passwordEdittext.getText().toString();

            HashMap<String, String> data = new HashMap<>();

            //Checks that user has entered something into all registration fields
            if (username.length() > 0 && email.length() > 0 && password.length() > 0) {
                //https://firebase.google.com/docs/firestore/query-data/get-data --> how to get a doc from firestore
                DocumentReference ref = db.collection("Users").document(username);

                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //For app log
                                Log.d("User", "Username already exists");

                                //Something to show that the username is taken. Maybe a toast??
                                //https://developer.android.com/guide/topics/ui/notifiers/toasts --> How to make a toast

                                Context context = getActivity();
                                CharSequence text = "Username already exists. Please choose another one.";
                                int duration = Toast.LENGTH_LONG;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();

                            }else{ //Username is unique, so account can be created

                                //Puts email and password information into data. Creates the new player account
                                data.put("Email", email);
                                data.put("Password", password);
                                PlayerProfile playerProfile = new PlayerProfile(username, email, password);
                                viewModel.selectPlayer(playerProfile);

                                //Saves the DeviceID so app can automatically sign-in user from now on
                                deviceReference
                                        .document(deviceID)
                                        .set(new HashMap<String, String>() {{
                                            put("LastUser", "/Users/" + username);
                                        }})
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // These are a method which gets executed when the task is succeeded
                                                Log.d("User", "Device has been added successfully!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // These are a method which gets executed if there’s any problem
                                                Log.d("User", "Device could not be added!" + e);
                                            }
                                        });

                                //Saves doc for the new user in firestore
                                collectionReference
                                        .document(username)
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // These are a method which gets executed when the task is succeeded
                                                Log.d("User", "User has been added successfully!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // These are a method which gets executed if there’s any problem
                                                Log.d("User", "User could not be added!" + e);
                                            }
                                        });
                            }
                        }
                    }
                });

            }
        });
        return view;
    }

}