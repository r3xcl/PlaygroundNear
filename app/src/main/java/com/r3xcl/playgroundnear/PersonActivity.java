package com.r3xcl.playgroundnear;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class PersonActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String SharedPrefName = "auth_shared_pref";

    String phonenum;
    Window window;
    Button exit;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        getSupportActionBar().hide();

        exit = findViewById(R.id.but_exit);

        sharedPreferences = getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);

        phonenum = sharedPreferences.getString("phonenum","");

        window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.dark_peach));

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();
            }
        });

    }

    private void showExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonActivity.this);
        View view = LayoutInflater.from(PersonActivity.this).inflate(R.layout.dialog_layout,
                (ConstraintLayout)findViewById(R.id.dialog_layout_container));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.dialog_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("phonenum","");
                editor.apply();
                finish();
                Intent intent = new Intent(PersonActivity.this,AuthActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.dialog_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }
}