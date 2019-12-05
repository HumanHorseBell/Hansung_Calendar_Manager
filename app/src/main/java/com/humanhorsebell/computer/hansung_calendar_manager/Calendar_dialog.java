package com.humanhorsebell.computer.hansung_calendar_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class Calendar_dialog extends AppCompatActivity {
Button btnAddCal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_dialog);
        btnAddCal = (Button) findViewById(R.id.add_calendar);
    }
}
