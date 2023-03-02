package com.example.onbudget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {
    Button signup, next;
    EditText loginEmail, loginPassword;
    SQLiteDatabase onBudgetDB;
    TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override //textwatcher to check on input change and set enabled disabled and color of continue background
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String emailInput = loginEmail.getText().toString().trim();
            String passwordInput = loginPassword.getText().toString().trim();

            next.setEnabled(!emailInput.isEmpty() && !passwordInput.isEmpty());

            if (!emailInput.isEmpty() && !passwordInput.isEmpty()) {
                next.setBackgroundColor(getColor(R.color.LightGreen));

            } else {
                next.setBackgroundColor(getColor(R.color.Gray));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        loginEmail = findViewById(R.id.etLoginEmail);
        loginPassword = findViewById(R.id.etLoginPassword);
        next = findViewById(R.id.Continuebtn);
        signup = findViewById(R.id.Signupbtn);


        loginEmail.addTextChangedListener(loginTextWatcher);
        loginPassword.addTextChangedListener(loginTextWatcher);

        onBudgetDB = openOrCreateDatabase("OnBudgetDB", Context.MODE_PRIVATE, null); //open connection with database
        onBudgetDB.execSQL("CREATE TABLE IF NOT EXISTS accounts(firstName VARCHAR, lastName VARCHAR, email VARCHAR, phoneNumber VARCHAR, password VARCHAR)");
    }

    public void Verify(View view) {
        //create the hash for the password and storing it and comparing it with the database, and checking for empty fields or account existence
        Cursor select_email_password = onBudgetDB.rawQuery("SELECT email, password FROM accounts WHERE email='" + loginEmail.getText() + "'", null);
        if (select_email_password.moveToFirst()) {
            String temp_email = select_email_password.getString(0);
            String temp_password = select_email_password.getString(1);
            // Hashing the password
            String hashedPassword = PasswordHashed.hashPassword(loginPassword.getText().toString());
            if (!loginEmail.getText().toString().equals(temp_email) || !hashedPassword.equals(temp_password)){
                Toast.makeText(LoginPage.this, "The email or password are not correct.", Toast.LENGTH_SHORT).show();
                return;
            }
            Cursor select_first_last_name = onBudgetDB.rawQuery("SELECT firstName, lastName, email FROM accounts WHERE email='" + loginEmail.getText() + "'", null);
            if (select_first_last_name.moveToFirst()) {
                String temp_fName = select_first_last_name.getString(0);
                String temp_lName = select_first_last_name.getString(1);
                Intent homepage = new Intent(LoginPage.this, HomePage.class);
                homepage.putExtra("FirstName",temp_fName);
                homepage.putExtra("LastName", temp_lName);
                homepage.putExtra("Email",temp_email);
                startActivity(homepage);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You don't have an account yet, would you like to create one now?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent SignupPage = new Intent(LoginPage.this, SignupPage.class);
                    SignupPage.putExtra("SignupEmail", loginEmail.getText().toString());
                    SignupPage.putExtra("SignupPassword", loginPassword.getText().toString());
                    startActivity(SignupPage);
                }
            });
            builder.setNegativeButton("No", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

        public void SignupPage (View view){
            Intent SignupPage = new Intent(LoginPage.this, SignupPage.class);
            startActivity(SignupPage);
        }


    public void Delete(View view) { //account deletion after email and hashed password verification
        if (loginEmail.getText().toString().trim().length() == 0 || loginPassword.getText().toString().trim().length() == 0) {
            Toast.makeText(LoginPage.this, "Please fill all fields!!", Toast.LENGTH_SHORT).show();
            return;
        }
        Cursor c = onBudgetDB.rawQuery("SELECT email, password FROM accounts WHERE email='" + loginEmail.getText() + "'", null);
        if (c.moveToFirst()) {
            String temp_email = c.getString(0);
            String temp_password = c.getString(1);
            // Hashing the password
            String hashedPassword = PasswordHashed.hashPassword(loginPassword.getText().toString());
            if (loginEmail.getText().toString().equals(temp_email) & hashedPassword.equals(temp_password)) {
                onBudgetDB.execSQL("DELETE FROM accounts WHERE email='"+loginEmail.getText()+"'");
                Toast.makeText(LoginPage.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                clear();
            }
        } else {
            Toast.makeText(LoginPage.this, "Email Not Found !!!", Toast.LENGTH_SHORT).show();
        }
    }
    public void clear(){ // to clear the fields after the delete process
        loginEmail.setText("");
        loginPassword.setText("");

    }

    }