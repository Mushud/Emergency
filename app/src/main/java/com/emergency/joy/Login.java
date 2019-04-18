package com.emergency.joy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    private EditText mPhoneNumber, mCode, mEmail, mUsername;
    private Button mVerify;

    private ProgressDialog mProg;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mVerify = findViewById(R.id.btnVerify);
        mPhoneNumber = findViewById(R.id.btnPhone);
        mCode = findViewById(R.id.VerificationCode);
        mEmail = findViewById(R.id.btnEmail);
        mUsername= findViewById(R.id.btnUsername);


        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        mProg = new ProgressDialog(this);
        mProg.setMessage("Loading...");

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInwithAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mPhoneNumber.setError("Invalid phone number.");
                    mPhoneNumber.setEnabled(true);
                    mVerify.setVisibility(View.VISIBLE);
                    mProg.hide();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    e.printStackTrace();
                    mProg.hide();
                    Toast.makeText(getApplicationContext(), "Quota Exceeded", Toast.LENGTH_SHORT).show();
                } else {
                    // mProgressBar.setVisibility(View.GONE);
                    mProg.hide();
                    e.printStackTrace();
                    mPhoneNumber.setEnabled(true);
                    mVerify.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mCode.setVisibility(View.VISIBLE);
                mPhoneNumber.setVisibility(View.GONE);
                mVerify.setText("Verify Code");
                mVerify.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mVerify.setTextColor(getResources().getColor(android.R.color.white));
                mProg.hide();

                mVerify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(mCode.getText())){
                            Toast.makeText(Login.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                        }else {
                            verifyPhonenumberWithCode(mVerificationId, mCode.getText().toString());
                        }
                    }
                });

                mVerificationId = s;
            }
        };

        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mPhoneNumber.getText()) &&  !TextUtils.isEmpty(mUsername.getText())){
                    startPhoneVerification(mPhoneNumber.getText().toString());
                }else {
                    if(TextUtils.isEmpty(mPhoneNumber.getText())){
                        mPhoneNumber.setError("Please Enter Phone Number");
                    }else {
                        mUsername.setError("Please Enter you Full Name");
                    }
                }
            }
        });
    }

    private void startPhoneVerification(String PhoneNumber){
        mProg.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(PhoneNumber, 60, TimeUnit.SECONDS, this, mCallbacks );

    }
    private void verifyPhonenumberWithCode(String mVerificationId, String code){
        PhoneAuthCredential credential  = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInwithAuthCredential(credential);
    }

    private void signInwithAuthCredential(PhoneAuthCredential credential){
        // mProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = task.getResult().getUser();
                    Toast.makeText(Login.this, "Signed In", Toast.LENGTH_SHORT).show();
                    Emergency.mySharedPreferenceEditor.putString(Emergency.phoneNumber, mPhoneNumber.getText().toString());
                    Emergency.mySharedPreferenceEditor.putBoolean(Emergency.SignIn, true);
                    Emergency.mySharedPreferenceEditor.apply();
                    finish();
                    startActivity(new Intent(Login.this, MainActivity.class));
                }else{
                    Log.w("TAG", "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    }
                }
            }
        });
    }

}
