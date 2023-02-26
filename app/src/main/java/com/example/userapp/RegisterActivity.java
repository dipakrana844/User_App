package com.example.userapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.userapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText etRegEmail, etRegPassword, etRegFName, etRegLName, etRegAge, etRegUserName;
    TextView tvLoginHere;
    Button btnRegister;
    FirebaseDatabase db;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etRegFName = findViewById(R.id.etRegFName);
        etRegLName = findViewById(R.id.etRegLName);
        etRegAge = findViewById(R.id.etRegAge);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPass);
        etRegUserName = findViewById(R.id.etRegUserName);
        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view -> {
            createUser();
        });

        tvLoginHere.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

    }

    private void createUser() {
        String fName = etRegFName.getText().toString();
        String lName = etRegLName.getText().toString();
        String age = etRegAge.getText().toString();
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        String userName = etRegUserName.getText().toString();


        if (TextUtils.isEmpty(fName)) {
            etRegFName.setError("First Name cannot be empty");
            etRegFName.requestFocus();
        } else if (TextUtils.isEmpty(lName)) {
            etRegLName.setError("Last Name cannot be empty");
            etRegLName.requestFocus();
        } else if (TextUtils.isEmpty(age)) {
            etRegAge.setError("Age cannot be empty");
            etRegAge.requestFocus();
        } else if (TextUtils.isEmpty(age)) {
            etRegUserName.setError("UserName cannot be empty");
            etRegUserName.requestFocus();
        } else if (TextUtils.isEmpty(email)) {
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etRegPassword.setError("Password cannot be empty");
            etRegPassword.requestFocus();
        } else {

            User users = new User(fName, lName, age, email, userName);
            db = FirebaseDatabase.getInstance();
            reference = db.getReference("Users");
//             newUserId = reference.push().getKey();
            reference.child(userName).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    etRegFName.setText("");
                    etRegLName.setText("");
                    etRegAge.setText("");
                    etRegEmail.setText("");
                    etRegPassword.setText("");
                    Toast.makeText(RegisterActivity.this, "Successfuly Updated", Toast.LENGTH_SHORT).show();
                }
            });
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("userName", userName);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}