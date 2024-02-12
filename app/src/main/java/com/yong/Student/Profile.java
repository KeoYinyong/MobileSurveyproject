package com.yong.Student;

import android.content.Intent;
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
import com.google.firebase.auth.UserProfileChangeRequest;

public class Profile extends AppCompatActivity {

    private EditText textName, textEmail, textPassword, profileName, new_password, confirm_new_password;
    private ProgressBar progressBar;

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

        // Initialize views
        textName = findViewById(R.id.textview_name);
        textEmail = findViewById(R.id.textview_email);
        textPassword = findViewById(R.id.textview_password);
        profileName = findViewById(R.id.text_name);
        new_password = findViewById(R.id.new_password);
        confirm_new_password =findViewById(R.id.confirm_new_password);
        progressBar = findViewById(R.id.progressBar);

        // Fetch and display user profile data
        fetchUserProfile();
    }

    // Fetch user profile data from Firebase
    private void fetchUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();

            // Set user profile data to EditText fields
            profileName.setText(name);
            textName.setText(name);
            textEmail.setText(email);
            textPassword.setText("********"); // Dummy password placeholder

            // Hide progress bar once data is loaded
            progressBar.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Close this activity if user is not logged in
        }
    }

    // Update user's display name (username)
// Update user's display name (username) and password
    public void updateUserProfile(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String newName = profileName.getText().toString();
            String newPassword = new_password.getText().toString();
            String confirmNewPassword = confirm_new_password.getText().toString();

            // Check if new password and confirm new password match
            if (!newPassword.equals(confirmNewPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Update password only if it's not empty
                            if (!newPassword.isEmpty()) {
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(passwordTask -> {
                                            if (passwordTask.isSuccessful()) {
                                                Toast.makeText(Profile.this, "Profile and password updated successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(Profile.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(Profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Profile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    // Delete user's account
    public void deleteUserAccount(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Profile.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                            // Redirect to login activity or any other activity as needed
                            startActivity(new Intent(Profile.this, Login.class));
                            finish();
                        } else {
                            Toast.makeText(Profile.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
