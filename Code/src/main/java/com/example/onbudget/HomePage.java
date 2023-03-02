package com.example.onbudget;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    TextView name;
    EditText date, reason, amount;
    Spinner type;
    SQLiteDatabase onBudgetDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent homepage = getIntent();

        name = findViewById(R.id.tvName);
        date = findViewById(R.id.etDate);
        reason = findViewById(R.id.etReason);
        amount = findViewById(R.id.etAmount);
        type = findViewById(R.id.spType);
        name.setText("Welcome " + homepage.getStringExtra("FirstName") + " " + homepage.getStringExtra("LastName"));
        onBudgetDB = openOrCreateDatabase("OnBudgetDB", Context.MODE_PRIVATE, null);
        onBudgetDB.execSQL("CREATE TABLE IF NOT EXISTS financial (email VARCHAR, date VARCHAR, reason VARCHAR, amount VARCHAR, type VARCHAR)");




    }

    public void submit(View view) { // states all the available transactions in the database by selecting all the available through select star, and querying using email
        if (date.getText().toString().trim().length() == 0 ||
            amount.getText().toString().trim().length() == 0 || reason.getText().toString().trim().length() == 0 ) {
            Toast.makeText(HomePage.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent SignupPage = getIntent();

        String op_email = SignupPage.getStringExtra("Email");

        onBudgetDB.execSQL("INSERT INTO financial VALUES('" + op_email + "', '" + date.getText().toString() + "', '" + reason.getText().toString() + "', '" + amount.getText().toString() + "', '" + type.getSelectedItem() + "');");

        Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();

        Cursor c = onBudgetDB.rawQuery("SELECT * FROM financial WHERE email='" + op_email + "'", null);

        StringBuffer buffer = new StringBuffer();

        if(c.getCount()>0) {
            while (c.moveToNext()) {
                buffer.append("Email: " + c.getString(0)); //string at index 0 is the ID
                buffer.append("\nDate: " +c.getString(1));
                buffer.append("\nReason: " +c.getString(2));
                buffer.append("\nAmount: " +c.getString(3));
                buffer.append("\nType: " +c.getString(4));
                buffer.append("\n------------------------\n");
            }

        }
        Intent i = new Intent(HomePage.this, Result.class);
        i.putExtra("Amount", buffer.toString());
        startActivity(i);    }


    public void TransHistory(View view){ //button just to view all transactions
        Intent SignupPage = getIntent();

        String op_email = SignupPage.getStringExtra("Email");

        Cursor c = onBudgetDB.rawQuery("SELECT * FROM financial WHERE email='" + op_email + "'", null);

        StringBuffer buffer = new StringBuffer();

        if(c.getCount()>0) {
            while (c.moveToNext()) {
                buffer.append("Email:" + c.getString(0)); //string at index 0 is the ID
                buffer.append("\nDate:" +c.getString(1));
                buffer.append("\nReason:" +c.getString(2));
                buffer.append("\nAmount:" +c.getString(3));
                buffer.append("\nType:" +c.getString(4));
                buffer.append("\n------------------------\n");
            }

        }
        Intent i = new Intent(HomePage.this, Result.class);
        i.putExtra("Amount", buffer.toString());
        startActivity(i);

    }

    public void settings(View view){

        Intent settings = new Intent(HomePage.this, SettingsIntent.class);
        startActivity(settings);

    }

    public void showMessage(String title, String message) { //message popup code
        AlertDialog.Builder b = new AlertDialog.Builder((this));
        b.setCancelable(true);
        b.setTitle(title);
        //b.setIcon(R.drawable.db_icon);
        b.setMessage(message);
        b.show();
    }
}