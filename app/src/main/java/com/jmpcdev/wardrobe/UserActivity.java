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
import androidx.core.content.ContextCompat;

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

    User user = new User();

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
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (createUser(currentUser)){
                                Log.w(TAG, "createUser:success", task.getException());
                                updateUI(currentUser);
                            } else {
                                Toast.makeText(UserActivity.this, "Create user failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

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


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {

            FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            User inneruser = dataSnapshot.getValue(User.class);

                            if(inneruser.getEmail().equals(currentUser.getEmail())){
                                if(isCompleteUser(inneruser)){
                                    startActivity(new Intent(UserActivity.this, MainActivity.class));
                                }else{
                                    startActivity(new Intent(UserActivity.this, AccountActivity.class));
                                }

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


    private boolean isCompleteUser(User user){
        return (user.getName() != null && user.getBirthDate() != null && user.getCity() != null &&
                user.getCountry() != null && user.getGender() != null && user.getState() != null && user.getZipCode() != null);
    }


    @SuppressLint("ResourceAsColor")
    private void activateButton(){
        if (edtPass2.getText().toString().equals(edtPass.getText().toString())){
            edtPass2.setBackgroundColor(ContextCompat.getColor(UserActivity.this, R.color.colorAcept));
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
            edtPass2.setBackgroundColor(ContextCompat.getColor(UserActivity.this, R.color.colorError));
        }
    }

    private boolean createUser(FirebaseUser currentUser){
        final boolean[] resultado = {true};
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mDataBaseUsersUserEmail = mDataBase.child("users").child(currentUser.getUid());
        mDataBaseUsersUserEmail.child("email").setValue(currentUser.getEmail()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });
        mDataBaseUsersUserEmail.child("name").setValue(null).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });
        mDataBaseUsersUserEmail.child("birthDate").setValue(null).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });
        mDataBaseUsersUserEmail.child("gender").setValue(null).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });
        mDataBaseUsersUserEmail.child("country").setValue(null).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });
        mDataBaseUsersUserEmail.child("state").setValue(null).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });
        mDataBaseUsersUserEmail.child("city").setValue(null).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });
        mDataBaseUsersUserEmail.child("zipCode").setValue(null).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });

        return resultado[0];
    }


}
