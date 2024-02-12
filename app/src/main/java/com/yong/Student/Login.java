package com.yong.Student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yong.Student.MainActivity;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        // Authenticate user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Redirect to MainActivity
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Finish current activity to prevent user from going back to login screen
                        // You can navigate to the next activity or perform any required action here
                        Toast.makeText(Login.this, "Login successful.", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}








//package com.yong.Student;
//
//import static android.widget.Toast.LENGTH_SHORT;
//import static com.yong.Student.R.id.login_email;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class Login extends AppCompatActivity {
//
//    private Button btn_login, buttonSignUp;
//    private EditText lg_email, login_password;
//    private ProgressDialog loadingBar;
//    private FirebaseAuth auth;
//
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        auth = FirebaseAuth.getInstance();
//
//        lg_email = (EditText) findViewById(login_email);
//        login_password = (EditText) findViewById(R.id.login_password);
//        btn_login = findViewById(R.id.login);
//        buttonSignUp = findViewById(R.id.register);
//
//        loadingBar = new ProgressDialog(this);
//        buttonSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Launch the sign-up activity
//                TosignUp();
//            }
//        });
//
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userLogin();
//            }
//        });
//    }
//
//    private void TosignUp() {
//        Intent SignupIntent = new Intent(Login.this, SignUp.class);
//        startActivity(SignupIntent);
//    }
//
//    private void userLogin() {
//        String email = lg_email.getText().toString();
//        String password = login_password.getText().toString();
//        if (TextUtils.isEmpty(email)) {
//            Toast.makeText(this, "Please write your email...", LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(password)) {
//            Toast.makeText(this, "Please confirm your password...", Toast.LENGTH_SHORT).show();
//        } else {
//            loadingBar.setTitle("Login");
//            loadingBar.setMessage("Please wait, while we are allowing you to login into account...");
//            loadingBar.show();
//            loadingBar.setCanceledOnTouchOutside(true);
//
//            // Use signInWithEmailAndPassword instead of createUserWithEmailAndPassword
//            auth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                sendUserToMainMenuActivity();
//                                Toast.makeText(Login.this, "You are logged in successfully...", Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
//
//                            } else {
//                                String message = task.getException().getMessage();
//                                Toast.makeText(Login.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
//                            }
//                        }
//                    });
//        }
//    }
//
//
//    private void sendUserToMainMenuActivity() {
//        Intent loginIntent = new Intent(Login.this, MainActivity.class);
//        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(loginIntent);
//        finish();
//    }
//
//}
//
