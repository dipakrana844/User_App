package com.example.userapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapp.adapter.UserAdapter;
import com.example.userapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btnLogOut;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    RecyclerView recyclerView;
    String checkUsername;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogOut = findViewById(R.id.btnLogout);
        recyclerView = findViewById(R.id.rvUser);
        mAuth = FirebaseAuth.getInstance();
        checkUsername = getIntent().getStringExtra("userName");
        btnLogOut.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {

            reference = FirebaseDatabase.getInstance().getReference("Users");//NPC076tSBsYm9OAryoc
            Query query = reference.child(checkUsername);
            Log.d("Mainactivity", "onStart: " + query);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String myvalue = String.valueOf(snapshot.child(checkUsername).getValue());
                    if (myvalue != null) {
                        List<User> userList = new ArrayList<>();
                        Log.d(TAG, "onDataChange: " + snapshot);
                        if (snapshot.exists()) {
                            String firstName = String.valueOf(snapshot.child("firstName").getValue());
                            String lastName = String.valueOf(snapshot.child("lastName").getValue());
                            String age = String.valueOf(snapshot.child("age").getValue());
                            String email = String.valueOf(snapshot.child("email").getValue());
                            String userName = String.valueOf(snapshot.child("userName").getValue());
                            User user1 = new User(firstName, lastName, age, email, userName);
                            userList.add(user1);
                            UserAdapter adapter = new UserAdapter(userList);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        } else {
                            Toast.makeText(MainActivity.this, "User Data not Found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d(TAG, "onDataChange: NO Data Found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void readData() {
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("user1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {

                        Toast.makeText(MainActivity.this, "Successfully Read", Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        Log.d(TAG, "onComplete: " + dataSnapshot);
                        String firstName = String.valueOf(dataSnapshot.child("firstName").getValue());
                        String lastName = String.valueOf(dataSnapshot.child("lastName").getValue());
                        String age = String.valueOf(dataSnapshot.child("age").getValue());
                        String email = String.valueOf(dataSnapshot.child("email").getValue());
                        Log.d(TAG, "onComplete: " + firstName + "-> " + lastName + "-> " + age + "-> " + email);
//                            binding.tvFirstName.setText(firstName);
//                            binding.tvLastName.setText(lastName);
//                            binding.tvAge.setText(age);
                    } else {

                        Toast.makeText(MainActivity.this, "User Doesn't Exist", Toast.LENGTH_SHORT).show();

                    }


                } else {

                    Toast.makeText(MainActivity.this, "Failed to read", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}