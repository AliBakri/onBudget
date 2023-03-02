package com.example.onbudget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupPage extends AppCompatActivity {

    EditText firstName, lastName, signupEmail, phoneNumber, signupPassword, confirmPassword;
    CheckBox agreement;
    Button createAccount;
    SQLiteDatabase onBudgetDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        firstName = findViewById(R.id.etFirstName);
        lastName = findViewById(R.id.etLastName);
        signupEmail = findViewById(R.id.etSignupEmail);
        phoneNumber = findViewById(R.id.etPhoneNumber);
        signupPassword = findViewById(R.id.etSignupPassword);
        confirmPassword = findViewById(R.id.etConfirmPassword);
        agreement = findViewById(R.id.cbAgreement);
        createAccount = findViewById(R.id.createAccount);

        onBudgetDB = openOrCreateDatabase("OnBudgetDB", Context.MODE_PRIVATE, null);

        onBudgetDB.execSQL("CREATE TABLE IF NOT EXISTS accounts(firstName VARCHAR, lastName VARCHAR, email VARCHAR, phoneNumber VARCHAR, password VARCHAR)");

        // Inorder to make it easier for the user we made this to move the email and password to the SignupPage and paste them for the user there
        Intent SignupPage = getIntent();
        signupEmail.setText(SignupPage.getStringExtra("SignupEmail"));
        signupPassword.setText(SignupPage.getStringExtra("SignupPassword"));
    }

    public void createAccount(View view) {
        // Checking if all fields are entered
        if (firstName.getText().toString().trim().length() == 0 || lastName.getText().toString().trim().length() == 0 || signupEmail.getText().toString().trim().length() == 0 || phoneNumber.getText().toString().trim().length() == 0 || signupPassword.getText().toString().trim().length() == 0) {
            Toast.makeText(SignupPage.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if email is already found in the database
        Cursor c = onBudgetDB.rawQuery("SELECT * FROM accounts WHERE email='" + signupEmail.getText() + "'", null);
        if (c.moveToFirst()) {
            Toast.makeText(SignupPage.this, "This email is already being used.", Toast.LENGTH_SHORT).show();
            return;
        }

            // Checking if password is less than 8 characters
        if (confirmPassword.getText().toString().length() < 8) {
            Toast.makeText(SignupPage.this, "Please the password should be minimum 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Checking the password is confirmed
        if (confirmPassword.getText().toString().length() == 0) {
            Toast.makeText(SignupPage.this, "Please Confirm Password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Checking if the password confirm matches the password
        if (!confirmPassword.getText().toString().equals(signupPassword.getText().toString())) {
            Toast.makeText(SignupPage.this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!agreement.isChecked()) {
            Toast.makeText(SignupPage.this, "Kindly agree to the terms & conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hashing the password
        String hashedPassword = PasswordHashed.hashPassword(signupPassword.getText().toString());

        // insert into table called accounts the following values
        onBudgetDB.execSQL("INSERT INTO accounts VALUES('" + firstName.getText().toString() + "', '" + lastName.getText().toString() + "', '" + signupEmail.getText().toString() + "', '" + phoneNumber.getText().toString() + "', '" + hashedPassword + "');");

        Toast.makeText(SignupPage.this, "Account Created", Toast.LENGTH_SHORT).show();

        Intent Back = new Intent(SignupPage.this, LoginPage.class);
        startActivity(Back);
    }




}