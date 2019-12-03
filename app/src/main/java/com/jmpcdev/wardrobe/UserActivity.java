package com.jmpcdev.wardrobe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class UserActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";

    private EditText edtEmail, edtPass, edtPass2;
    private Button btnUser;

    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;

    private String idToken = null;

    private TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            activateButton();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        edtEmail = findViewById(R.id.edtNewUser);
        edtPass = findViewById(R.id.edtNewUserPass);
        edtPass2 = findViewById(R.id.edtNewUserPass2);
        btnUser = findViewById(R.id.btnNewUser);
        //FirebaseAuth.getInstance().signOut();
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        edtPass.addTextChangedListener(tw);
        edtPass2.addTextChangedListener(tw);
    }


    public void createUser(){

        mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (writeUser(user)){
                                Log.w(TAG, "createUser:success", task.getException());
                            }
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(UserActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(final FirebaseUser currentUser) {
        if (currentUser != null) {
            FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null){
                        startActivity(new Intent(UserActivity.this, AccountActivity.class));
                    } else {
                        startActivity(new Intent(UserActivity.this, MainActivity.class));
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }


    @SuppressLint("ResourceAsColor")
    private void activateButton(){
        if (edtPass2.getText().toString().equals(edtPass.getText().toString())){
            edtPass2.setBackgroundColor(R.color.colorAcept);
            btnUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createUser();
                }
            });
        } else {
            btnUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            edtPass2.setBackgroundColor(R.color.colorError);
        }
    }
    private boolean writeUser(FirebaseUser user){
        final boolean[] result = {true};
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mDataBaseUsersUserId = mDataBase.child("users").child(user.getUid());
        mDataBaseUsersUserId.child("email").setValue(user.getEmail()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result[0] = false;
            }
        });

        return result[0];
    }



}
