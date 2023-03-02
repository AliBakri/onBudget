package com.example.onbudget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsIntent extends AppCompatActivity {
EditText etTo, etSubject, etMessage;
Button Send;
SQLiteDatabase onBudgetDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_intent);

        etTo = findViewById(R.id.etTo);
        etSubject = findViewById(R.id.etSubject);
        etMessage = findViewById(R.id.etMessage);
        Send = findViewById(R.id.sendEmail);


        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //connecting the form to the gmail and autofilling in gmail through extra parameters
                Intent gmail = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + etTo.getText().toString()));
                gmail.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText().toString());
                gmail.putExtra(Intent.EXTRA_TEXT, etMessage.getText().toString());
                startActivity(gmail);
            }
        });



    }




}