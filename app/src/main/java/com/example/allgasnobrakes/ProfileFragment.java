package com.example.allgasnobrakes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

/**
 * Displays fragment for user's profile
 * - allows user to search for other players
 * @author zhaoyu4 and theresag
 * @version 2.0
 */

public class ProfileFragment extends Fragment {

    private TextView username;
    private TextView email;
    private Button editing;

    //For searching for other players
    private EditText search_friend;
    private Button searching;

    //Needs firestore to search for players
    private FirebaseFirestore db;

    public ProfileFragment() {
        super(R.layout.profile);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);

        //For display and editing user's own profile
        username = view.findViewById(R.id.username_text);
        email = view.findViewById(R.id.email_text);
        editing = view.findViewById(R.id.edit_btn);

        //For searching for other players
        search_friend = view.findViewById(R.id.search_friends);
        searching = view.findViewById(R.id.search_btn);

        //Get instance of the database
        db = FirebaseFirestore.getInstance();

        //Sets the text for the username and email
        username.setText(requireArguments().getString("Username"));
        email.setText(requireArguments().getString("Email"));

        //Searching for other players
        searching.setOnClickListener(v -> {
            final String friend_name = search_friend.getText().toString();

            if (search_friend.length() > 0) {  //Should it check if the username entered is the same as profile??
                //For opening a another profile fragment for the other player
                FragmentManager other = getParentFragmentManager();

                //https://firebase.google.com/docs/firestore/query-data/get-data --> how to get a doc from firestore
                DocumentReference ref = db.collection("Users").document(friend_name);

                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //For app log
                                Log.d("Search", "Username Found");

                                //Make bundle of the friend profile
                                Bundle friend_bundle = new Bundle();
                                friend_bundle.putString("Username", document.getId());
                                friend_bundle.putString("Email", document.getString("Email"));

                                //Show the profile --> open a fragment of the other player's profile??
                                other.beginTransaction()
                                        .setReorderingAllowed(true)
                                        .replace(R.id.friend_fragment, FriendFragment.class, friend_bundle)
                                        .replace(R.id.menu_bar_container, MenuBarFragment.class, friend_bundle)
                                        .commit();

                            } else {  //if username does not exist
                                //For app log
                                Log.d("Search", "Username does not exist");

                                //Something to show that the username is taken. Maybe a toast??
                                //https://developer.android.com/guide/topics/ui/notifiers/toasts --> How to make a toast

                                Context context = getActivity();
                                CharSequence text = "Cannot find username. Please try again";  //Message for Toast
                                int duration = Toast.LENGTH_LONG;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }

                        }
                    }
                });
            }
        });
        return view;
    }
}
