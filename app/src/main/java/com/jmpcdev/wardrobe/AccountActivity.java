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
    private RadioButton rdbMale, rdbFemale, rdbOther, rdbUnans;
    private Button btnUser;
    private ImageView imvUserHome;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private static final String TAG = "AccountActivity";

    private final User user = new User();



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
        rdbFemale = findViewById(R.id.rdbFemale);
        rdbMale = findViewById(R.id.rdbMale);
        rdbOther = findViewById(R.id.rdbOther);
        rdbUnans = findViewById(R.id.rdbNotAnswer);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        goLoginIfUserNotExits(currentUser);

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();

                User user = new User(//currentUser.getUid(),
                                     currentUser.getEmail(),
                                     edtNameUser.getText().toString(),
                                     edtBirthDate.getText().toString(),
                                     userGender(),
                                     edtCountry.getText().toString(),
                                     edtState.getText().toString(),
                                     edtCity.getText().toString(),
                                     edtZipCode.getText().toString());
                writeUser(user);
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
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

    private void goLoginIfUserNotExits(FirebaseUser currentUser){
        if(currentUser == null){
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


    private void writeUser(User user){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mDataBaseUsersUserID = mDataBase.child("users").child(currentUser.getUid());
        if(!(user.getEmail().equals(currentUser.getEmail()))){
            currentUser.updateEmail(user.getEmail());
            mDataBaseUsersUserID.child("email").setValue(currentUser.getEmail());
        }
        mDataBaseUsersUserID.child("name").setValue(user.getName());
        mDataBaseUsersUserID.child("birthDate").setValue(user.getBirthDate());
        mDataBaseUsersUserID.child("gender").setValue(user.getGender());
        mDataBaseUsersUserID.child("country").setValue(user.getCountry());
        mDataBaseUsersUserID.child("state").setValue(user.getState());
        mDataBaseUsersUserID.child("city").setValue(user.getCity());
        mDataBaseUsersUserID.child("zipCode").setValue(user.getZipCode());
    }




    private void updateUserValues(FirebaseUser user){
        if (user != null) {
            FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User innerUSer = dataSnapshot.getValue(User.class);
                            System.out.println(innerUSer);
                            completeEdtUser(innerUSer);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void completeEdtUser(User user){
        edtEmailUser.setText(user.getEmail());
        if(user.getZipCode() != null){
            edtZipCode.setText(user.getZipCode());
        }
        if(user.getState() != null){
            edtState.setText(user.getState());
        }
        if(user.getGender() != null){
            switch (user.getGender()){
                case "male":
                    rdbMale.setChecked(true);
                    rdbFemale.setChecked(false);
                    rdbOther.setChecked(false);
                    rdbUnans.setChecked(false);
                    break;
                case "female":
                    rdbMale.setChecked(false);
                    rdbFemale.setChecked(true);
                    rdbOther.setChecked(false);
                    rdbUnans.setChecked(false);
                    break;
                case "other":
                    rdbMale.setChecked(false);
                    rdbFemale.setChecked(false);
                    rdbOther.setChecked(true);
                    rdbUnans.setChecked(false);
                    break;
                case "unanswered":
                    rdbMale.setChecked(false);
                    rdbFemale.setChecked(false);
                    rdbOther.setChecked(false);
                    rdbUnans.setChecked(true);
                    break;
            }
        }
        if(user.getCountry() != null){
            edtCountry.setText(user.getCountry());
        }
        if(user.getCity() != null){
            edtCity.setText(user.getCity());
        }
        if(user.getName() != null){
            edtNameUser.setText(user.getName());
        }
        if(user.getBirthDate() != null){
            edtBirthDate.setText(user.getBirthDate());
        }

    }

}
