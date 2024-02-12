package com.yong.Student;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button survey = (Button) findViewById(R.id.btnStudentSurvey);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent surveyIntent = new Intent(MainActivity.this, NoteList.class);
                startActivity(surveyIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile) {
            // Open the profile activity
            startActivity(new Intent(MainActivity.this, Profile.class));
            return true;
        } else if (id == R.id.logout) {
            // Perform logout
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        // Implement logout functionality here
        // For example, sign out the current user from Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Redirect to the login activity or any other activity as needed
        startActivity(new Intent(MainActivity.this, Login.class));
        finish(); // Close this activity
    }

}
