package com.r3xcl.playgroundnear;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerId;

    private FirebaseAuth firebaseAuth;

    Window window;
    FloatingActionButton fab_next;
    ImageView phone,ver_code;
    EditText input_number,input_code;
    TextView resend;
    String phonenum = "";
    String code = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        getSupportActionBar().hide();

        fab_next = findViewById(R.id.fab_next);
        phone = findViewById(R.id.phone);
        ver_code = findViewById(R.id.ver_code);
        input_number = findViewById(R.id.input_number);
        input_code = findViewById(R.id.input_code);
        resend = findViewById(R.id.resend);

        window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.bar_auth));

        firebaseAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signWithAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String verID, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verID, forceResendingToken);

                mVerId = verID;
                forceResendingToken = token;
            }

        };

        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phonenum = input_number.getText().toString().trim();

                if(phone.getVisibility() == View.VISIBLE && !phonenum.isEmpty()){

                    phone.setVisibility(View.INVISIBLE);
                    input_number.setVisibility(View.INVISIBLE);
                    ver_code.setVisibility(View.VISIBLE);
                    input_code.setVisibility(View.VISIBLE);
                    resend.setVisibility(View.VISIBLE);

                    startPhoneVer(phonenum);
                }
                else if(phonenum.isEmpty()) {

                        Toast.makeText(PersonActivity.this,"Введите номер телефона!",Toast.LENGTH_LONG).show();
                }

                code = input_code.getText().toString().trim();

                if(ver_code.getVisibility() == View.VISIBLE && !code.isEmpty()){

                    verPhoneNum(mVerId,code);
                }
                else if(code.isEmpty()) {

                    Toast.makeText(PersonActivity.this,"Введите код!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phonenum.isEmpty()) {

                    Toast.makeText(PersonActivity.this,"Введите номер телефона!",Toast.LENGTH_SHORT).show();
                }
                else{
                    resendVerCode(phonenum , forceResendingToken);
                }
            }
        });
    }

    private void verPhoneNum(String mVerId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerId , code);
        signWithAuthCredential(credential);
    }

    private void signWithAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(PersonActivity.this,"OK",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void resendVerCode(String phonenum , PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options = PhoneAuthOptions
                .newBuilder(firebaseAuth)
                .setPhoneNumber(phonenum)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .setForceResendingToken(token)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void startPhoneVer(String phonenum) {
        PhoneAuthOptions options = PhoneAuthOptions
                .newBuilder(firebaseAuth)
                .setPhoneNumber(phonenum)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    @Override
    public void onClick(View v) {

    }
}