package com.yong.Student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Profile extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private EditText textName, textEmail, textPassword, new_password, confirm_new_password;
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
        textPassword = findViewById(R.id.old_password);
        new_password = findViewById(R.id.new_password);
        confirm_new_password = findViewById(R.id.confirm_new_password);
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
            textName.setText(name);
            textEmail.setText(email);

            // Hide progress bar once data is loaded
            progressBar.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Close this activity if user is not logged in
        }
    }

    // Update user's display name (username) and password
    public void updateUserProfile(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String newUsername = textName.getText().toString();
            String newEmail = textEmail.getText().toString();
            String oldPassword = textPassword.getText().toString(); // Old password from EditText
            String newPassword = new_password.getText().toString();
            String confirmNewPassword = confirm_new_password.getText().toString();

            // Check if old password is empty
            if (oldPassword.isEmpty()) {
                Toast.makeText(this, "Please enter the old password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if new password and confirm new password match
            if (!newPassword.equals(confirmNewPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Reauthenticate the user with their current password
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
            user.reauthenticate(credential)
                    .addOnCompleteListener(reauthTask -> {
                        if (reauthTask.isSuccessful()) {
                            Log.d(TAG, "User reauthenticated successfully");

                            // Update the user's email address
                            user.updateEmail(newEmail)
                                    .addOnCompleteListener(emailTask -> {
                                        if (emailTask.isSuccessful()) {
                                            Log.d(TAG, "User email updated successfully");

                                            // Update the user's display name (username)
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(newUsername)
                                                    .build();
                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(profileTask -> {
                                                        if (profileTask.isSuccessful()) {
                                                            Log.d(TAG, "User profile updated successfully");

                                                            // Update the user's password
                                                            if (!newPassword.isEmpty()) {
                                                                user.updatePassword(newPassword)
                                                                        .addOnCompleteListener(passwordTask -> {
                                                                            if (passwordTask.isSuccessful()) {
                                                                                Toast.makeText(Profile.this, "Profile and password updated successfully", Toast.LENGTH_SHORT).show();
                                                                                // Redirect to MainActivity
                                                                                startActivity(new Intent(Profile.this, MainActivity.class));
                                                                                finish();
                                                                            } else {
                                                                                Toast.makeText(Profile.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            } else {
                                                                Toast.makeText(Profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                                                // Redirect to MainActivity
                                                                startActivity(new Intent(Profile.this, MainActivity.class));
                                                                finish();
                                                            }
                                                        } else {
                                                            Toast.makeText(Profile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            Log.e(TAG, "Failed to update email", emailTask.getException());
                                            Toast.makeText(Profile.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Log.e(TAG, "Authentication failed. Please enter your current password correctly", reauthTask.getException());
                            Toast.makeText(Profile.this, "Authentication failed. Please enter your current password correctly", Toast.LENGTH_SHORT).show();
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
