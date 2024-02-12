package com.yong.Student;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private TextInputEditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        // Initialize TextInputEditText fields
        usernameEditText = findViewById(R.id.txt_username);
        emailEditText = findViewById(R.id.txt_email);
        passwordEditText = findViewById(R.id.txt_password);
        confirmPasswordEditText = findViewById(R.id.txt_password2);

        // Set up click listener for the sign-up button
        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Perform basic validation
        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            usernameEditText.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters long");
            passwordEditText.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            confirmPasswordEditText.requestFocus();
            return;
        }

        // Register the user with Firebase Authentication
        // Register the user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Registration successful, store additional user data
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Set the display name (username) for the user's Firebase Authentication profile
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(updateProfileTask -> {
                                        if (updateProfileTask.isSuccessful()) {
                                            // Display a success message
                                            Toast.makeText(SignUp.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                            // Proceed to store additional user data in Firebase Realtime Database
                                            String userId = user.getUid();
                                            DatabaseReference usersRef = mDatabase.getReference("users").child(userId);
                                            User newUser = new User(username, email);
                                            usersRef.setValue(newUser);

                                            // You can also navigate to the next activity if needed
                                            // Intent intent = new Intent(SignUp.this, NextActivity.class);
                                            // startActivity(intent);
                                        } else {
                                            // Failed to update the user's profile
                                            Toast.makeText(SignUp.this, "Failed to update profile: " + updateProfileTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Registration failed, display an error message
                        Toast.makeText(SignUp.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}




//package com.yong.Student;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.UserProfileChangeRequest;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class SignUp extends AppCompatActivity {
//
//    private EditText txt_username, txt_email, txt_password, txt_password2;
//    private Button btn_signup;
//
//    private ProgressDialog loadingBar;
//    private FirebaseAuth auth;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//
//        auth = FirebaseAuth.getInstance();
//
//        txt_username = (EditText) findViewById(R.id.txt_username);
//        txt_email = (EditText) findViewById(R.id.txt_email);
//        txt_password = (EditText) findViewById(R.id.txt_password);
//        txt_password2 = (EditText) findViewById(R.id.txt_password2);
//        btn_signup = (Button) findViewById(R.id.btn_signup);
//
//        loadingBar = new ProgressDialog(this);
//
//        btn_signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CreateNewAccount();
//            }
//        });
//    }
//
//    private void CreateNewAccount() {
//        String name = txt_username.getText().toString();
//        String email = txt_email.getText().toString();
//        String password = txt_password.getText().toString();
//        String password2 = txt_password2.getText().toString();
//        if (TextUtils.isEmpty(name)) {
//            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(email)) {
//            Toast.makeText(this, "Please write your email...", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(password)) {
//            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(password2)) {
//            Toast.makeText(this, "Please confirm your password...", Toast.LENGTH_SHORT).show();
//        } else if (!password.equals(password2)) {
//            Toast.makeText(this, "your password do not match with your confirm password !", Toast.LENGTH_SHORT).show();
//        } else {
//
//            loadingBar.setTitle("Creating New Account");
//            loadingBar.setMessage("Please wait, while we are creating your new account...");
//            loadingBar.show();
//            loadingBar.setCanceledOnTouchOutside(true);
//
//            auth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                SendUserToMainActivity();
//                                Toast.makeText(SignUp.this, "you are authenticated successfully...", Toast.LENGTH_SHORT).show();
//                                FirebaseUser firebaseUser = auth.getCurrentUser();
//
//                                //update display name
//                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
//                                firebaseUser.updateProfile(userProfileChangeRequest);
//
//                                UserDetails userDetails = new UserDetails(name);
//
//                                DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Register User");
//                                referenceProfile.child(firebaseUser.getUid()).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                    }
//                                });
//                            } else {
//                                String message = task.getException().getMessage();
//                                Toast.makeText(SignUp.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
//                            }
//                        }
//                    });
//        }
//
//    }
//
//    private void SendUserToMainActivity() {
//        Intent Signintent = new Intent(SignUp.this, Login.class);
//        Signintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(Signintent);
//        finish();
//    }
//
//}
