package com.yong.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    private TextView textName;
    private TextView textEmail;
    private ProgressBar processB;

    private FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }


        textName = findViewById(R.id.textview_name);
        textEmail = findViewById(R.id.textview_email);
        processB = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null){
            // Handle the case where user is not logged in
            Toast.makeText(Profile.this, "User not logged in!", Toast.LENGTH_SHORT).show();
        } else {
            processB.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // This is the ID for the back button in the action bar/toolbar
            // Navigate back to MainActivity
            onBackPressed(); // This will simulate the back button press
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered User");
        referenceProfile.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                    if (userDetails != null) {
                        String userName = userDetails.getName();
//                        String email = userDetails.getEmail();

                        textName.setText(userName);
//                        textEmail.setText(email);
                    }
                } else {
                    Toast.makeText(Profile.this, "User data not found!", Toast.LENGTH_SHORT).show();
                }
                processB.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                processB.setVisibility(View.GONE);
            }
        });
    }
}
