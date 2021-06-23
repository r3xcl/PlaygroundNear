package com.r3xcl.playgroundnear;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerId;

    private FirebaseAuth firebaseAuth;

    Window window;
    FloatingActionButton fab_next;
    ImageView phone,ver_code;
    EditText input_number,input_code;
    TextView resend,textviewnumber,textviewcode;
    String phonenum = "";
    String code = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        getSupportActionBar().hide();

        fab_next = findViewById(R.id.fab_next);
        phone = findViewById(R.id.phone);
        ver_code = findViewById(R.id.ver_code);
        input_number = findViewById(R.id.input_number);
        input_code = findViewById(R.id.input_code);
        resend = findViewById(R.id.resend);
        textviewnumber = findViewById(R.id.textviewnumber);
        textviewcode = findViewById(R.id.textviewcode);

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
                    textviewnumber.setVisibility(View.INVISIBLE);
                    ver_code.setVisibility(View.VISIBLE);
                    input_code.setVisibility(View.VISIBLE);
                    resend.setVisibility(View.VISIBLE);
                    textviewcode.setVisibility(View.VISIBLE);

                    startPhoneVer(phonenum);
                }
                else if(phonenum.isEmpty()) {

                        Toast.makeText(AuthActivity.this,"Введите номер телефона!",Toast.LENGTH_LONG).show();
                }

                code = input_code.getText().toString().trim();

                if(ver_code.getVisibility() == View.VISIBLE && !code.isEmpty()){

                    verPhoneNum(mVerId,code);
                }

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phonenum.isEmpty()) {

                    Toast.makeText(AuthActivity.this,"Введите номер телефона!",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AuthActivity.this,"Вход выполнен успешно!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AuthActivity.this,PersonActivity.class);
                finish();
                startActivityForResult(intent,1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AuthActivity.this,"Неверный код!",Toast.LENGTH_SHORT).show();
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

        Toast.makeText(AuthActivity.this,"Код отправлен!",Toast.LENGTH_SHORT).show();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
            hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }
}