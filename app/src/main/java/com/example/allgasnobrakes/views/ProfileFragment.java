package com.example.allgasnobrakes.views;

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
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.example.allgasnobrakes.MainActivity;
import com.example.allgasnobrakes.models.PlayerProfile;
import com.example.allgasnobrakes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Sets;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Displays fragment for user's profile
 * - allows user to search for other players
 * @author zhaoyu4, zhaoyu5, and theresag
 * @version 2.0
 */

public class ProfileFragment extends Fragment {
    private TextView username;
    private TextView email;
    private Button editing;

    //For searching for other players
    private EditText search_friend;
    private Button searching;
    private FragmentContainerView searchedplayer;

    //Needs firestore to search for players
    private FirebaseFirestore db;

    public ProfileFragment() {
        super(R.layout.profile);
    }

    /**
     * This gets the information the user inputted to create an account and adds it to the database.
     *      - Also checks if the username entered is unique (ie. not in the database).
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return a view for profile.xml
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);

        //For display and editing user's own profile
        username = view.findViewById(R.id.username_text);
        email = view.findViewById(R.id.email_text);
        searchedplayer = view.findViewById(R.id.friend_fragment);
        //For searching for other players
        search_friend = view.findViewById(R.id.search_friends);
        searching = view.findViewById(R.id.search_btn);

        //Get instance of the database
        db = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Sets the text for the username and email
        username.setText(currentUser().getUsername());
        email.setText(currentUser().getEmail());

        //For opening a another profile fragment for the other player
        FragmentManager other = getParentFragmentManager();
        other.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.friend_fragment, FriendFragment.class, new Bundle())
                .commit();

        //Searching for other players
        searching.setOnClickListener(v -> {
            final String friend_name = search_friend.getText().toString();

            if (search_friend.length() > 0) {  //Should I check if the username entered is the same as profile??

                //https://firebase.google.com/docs/firestore/query-data/get-data --> how to get a doc from firestore
                DocumentReference ref = db.collection("Users").document(friend_name);

                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (task.isSuccessful()) {

                            if (document.exists()) {
                                //For app log
                                Log.d("Search", "Username Found");

                                //Opens a fragment to show the searched player
                                searchedplayer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (search_friend != null){
                                            FoundPlayerFragment ADSF1 = new FoundPlayerFragment();
                                            ADSF1.main(friend_name,document.get("Email").toString());
                                            ADSF1.show(getActivity().getSupportFragmentManager(), "Finding");
                                        }
                                    }
                                });

                                //Make bundle of the friend profile
                                Bundle friend_bundle = new Bundle();
                                friend_bundle.putString("Username", document.getId());
                                friend_bundle.putString("Email", document.get("Email").toString());

                                other.beginTransaction()
                                        .setReorderingAllowed(true)
                                        .replace(R.id.friend_fragment, FriendFragment.class, friend_bundle)
                                        .commit();

                            } else {  //if username does not exist
                                //For app log
                                Log.d("Search", "Username does not exist");

                                //Something to show that the username is taken. Maybe a toast??
                                //https://developer.android.com/guide/topics/ui/notifiers/toasts --> How to make a toast

                                Context context = getActivity();
                                CharSequence text = "Cannot find username. Please try again";
                                int duration = Toast.LENGTH_LONG;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }

                        }
                    }
                });
            }
        });
    }

    /**
     * Returns the current user
     * @return The PlayerProfile object of the current user
     */
    private PlayerProfile currentUser() {
        return MainActivity.getCurrentUser();
    }
}
