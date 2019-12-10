package com.jmpcdev.wardrobe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextView txvNewUser, txvForgotPass;
    private Button btnContinue;
    private EditText email, password;

    private FirebaseAuth mAuth;


    private static final String TAG = "LoginActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        txvNewUser = findViewById(R.id.txvNewUser);
        txvForgotPass = findViewById(R.id.txvForgotPass);
        email = findViewById(R.id.edtUser);
        password = findViewById(R.id.edtPass);
        btnContinue = findViewById(R.id.button);
        //FirebaseAuth.getInstance().signOut();
        mAuth = FirebaseAuth.getInstance();



        txvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, UserActivity.class));
            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singinUser();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }







    public void singinUser(){
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            updateUI(currentUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }



    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {

            FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            User inneruser = dataSnapshot.getValue(User.class);

                            if(inneruser.getEmail().equals(currentUser.getEmail())){
                                if(isCompleteUser(inneruser)){
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }else{
                                    startActivity(new Intent(LoginActivity.this, AccountActivity.class));
                                }

                            } else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


        }
    }


    private boolean isCompleteUser(User user){
        return (user.getName() != null && !user.getName().equals("") && user.getBirthDate() != null
                && !user.getBirthDate().equals("") && user.getCity() != null &&
                !user.getCity().equals("") && user.getCountry() != null && !user.getCountry().equals("")
                && user.getGender() != null && user.getState() != null && !user.getState().equals("")
                && user.getZipCode() != null && !user.getZipCode().equals(""));
    }

}
