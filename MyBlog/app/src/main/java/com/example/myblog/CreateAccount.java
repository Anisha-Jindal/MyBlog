package com.example.myblog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount extends AppCompatActivity {

    private EditText fullname;
    private EditText Email;
    private EditText password;
    private EditText phoneno;
    private EditText college;
    private EditText address;
    private Button createAccount;

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        fullname=findViewById(R.id.fullNameAct);
        Email=findViewById(R.id.emailAct);
        password=findViewById(R.id.passwordAct);
        phoneno=findViewById(R.id.phoneAct);
        college=findViewById(R.id.collegenameAct);
        address=findViewById(R.id.addressAct);
        createAccount=findViewById(R.id.createaccountAct);

        mDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference().child("MUsers");
        mAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createNewAccount();
            }
        });
    }
    private void createNewAccount() {

        final String nameString=fullname.getText().toString().trim();
        final String emailString=Email.getText().toString().trim();
        final String passwordString=password.getText().toString().trim();
        final String phoneString=phoneno.getText().toString().trim();
        final String collegeString=college.getText().toString().trim();
        final String addressString=address.getText().toString().trim();

        if( !TextUtils.isEmpty(nameString) && !TextUtils.isEmpty(emailString) && !TextUtils.isEmpty(passwordString) && !TextUtils.isEmpty(phoneString) &&
                !TextUtils.isEmpty(collegeString) ){
            progressDialog.setMessage("Creating Account...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(emailString,passwordString).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    String userId=mAuth.getCurrentUser().getUid();
                    DatabaseReference currentUSerDb=mDatabaseReference.child(userId);
                    currentUSerDb.child("Fullname").setValue(nameString);
                    currentUSerDb.child("Email").setValue(emailString);
                    currentUSerDb.child("PhoneNo").setValue(phoneString);
                    currentUSerDb.child("CollegeName").setValue(collegeString);
                    currentUSerDb.child("Address").setValue(addressString);
                    currentUSerDb.child("Image").setValue("none");

                    progressDialog.dismiss();
                    Intent it=new Intent(CreateAccount.this, PostListActivity.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(it);


                }
            });
        }


    }
}
