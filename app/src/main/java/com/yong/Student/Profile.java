package com.yong.Student;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    private EditText textName, textEmail, textPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Note List");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        // Initialize views
        textName = findViewById(R.id.textview_name);
        textEmail = findViewById(R.id.textview_email);
        textPassword = findViewById(R.id.textview_password);
        progressBar = findViewById(R.id.progressBar);

        // Fetch and display user profile data
        fetchUserProfile();
    }

    // Inside fetchUserProfile() method in Profile activity
    private void fetchUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Fetch user profile data from Firebase or any other source
            String name = user.getDisplayName(); // Retrieve the display name (username)
            String email = user.getEmail();

            // Set the retrieved data to EditText fields
            textName.setText(name); // Set the username to the EditText field for name
            textEmail.setText(email);
            // You may choose not to display password for security reasons
            textPassword.setText("********"); // Dummy password placeholder

            // Hide progress bar once data is loaded
            progressBar.setVisibility(View.GONE);
        } else {
            // Handle case when user is not logged in
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Close this activity if user is not logged in
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


}
