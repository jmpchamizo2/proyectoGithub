package com.jmpcdev.wardrobe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    private EditText edtNameUser, edtEmailUser, edtBirthDate, edtCountry, edtState, edtCity, edtZipCode;
    private RadioGroup rdgGender;
    private Button btnUser;
    private ImageView imvUserHome;


    private String idToken;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private static final String TAG = "AccountActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().hide();

        edtNameUser = findViewById(R.id.edtNameUser);
        edtEmailUser = findViewById(R.id.edtEmailUser);
        edtBirthDate = findViewById(R.id.edtBirthDate);
        edtCountry = findViewById(R.id.edtCountry);
        edtState = findViewById(R.id.edtState);
        edtCity = findViewById(R.id.edtCity);
        edtZipCode = findViewById(R.id.edtZipcode);
        rdgGender = findViewById(R.id.rdgGender);
        btnUser = findViewById(R.id.btnUser);
        imvUserHome = findViewById(R.id.imvUserHome);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        goLoginIfUserNotExits(currentUser);

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();

                User user = new User(currentUser.getUid(),
                                     currentUser.getEmail(),
                                     edtNameUser.getText().toString(),
                                     edtBirthDate.getText().toString(),
                                     userGender(),
                                     edtCountry.getText().toString(),
                                     edtState.getText().toString(),
                                     edtCity.getText().toString(),
                                     edtZipCode.getText().toString());
                if (writeUser(user)){
                    System.out.println("Usuario insertado correctamente");
                } else {
                    System.out.println("Usuario no insertado");
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUserValues(currentUser);

    }

    private void goLoginIfUserNotExits(FirebaseUser firebaseUser){
        if(firebaseUser == null){
            new Intent(AccountActivity.this, LoginActivity.class);
        }
    }

    private String userGender(){
        switch (rdgGender.getCheckedRadioButtonId()){
            case(R.id.rdbFemale):
                return "female";
            case(R.id.rdbMale):
                return "male";
            case (R.id.rdbOther):
                return "other";
            case (R.id.rdbNotAnswer):
                return "unanswered";
            default:
                return null;
        }
    }


    private boolean writeUser(User user){
        final boolean[] resultado = {true};
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mDataBaseUsersUserEmail = mDataBase.child("users").child(user.getId());
        mDataBaseUsersUserEmail.child("name").setValue(user.getName()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });
        mDataBaseUsersUserEmail.child("birthDate").setValue(user.getBirthDate()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });
        mDataBaseUsersUserEmail.child("gender").setValue(user.getGender()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });
        mDataBaseUsersUserEmail.child("country").setValue(user.getCountry()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });
        mDataBaseUsersUserEmail.child("state").setValue(user.getState()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });
        mDataBaseUsersUserEmail.child("city").setValue(user.getCity()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });
        mDataBaseUsersUserEmail.child("zipCode").setValue(user.getZipCode()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultado[0] = false;
            }
        });

        return resultado[0];
    }


    private void updateUserValues(FirebaseUser user){
        if (user != null) {
           FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid())
                   .addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           for(DataSnapshot ds : dataSnapshot.getChildren()) {
                               edtEmailUser.setText(ds.child("email").getValue(String.class));
                           }


                       }
                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
        }
    }

}
