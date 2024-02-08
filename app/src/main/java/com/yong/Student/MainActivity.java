package com.yong.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.os.Bundle;
import android.view.MenuInflater;


public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

}
