package com.r3xcl.playgroundnear;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {

    Window window;
    FloatingActionButton fab_next;
    ImageView phone,ver_code;
    EditText input_number,input_code;

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

        window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.bar_auth));

        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.setVisibility(View.INVISIBLE);
                input_number.setVisibility(View.INVISIBLE);
                ver_code.setVisibility(View.VISIBLE);
                input_code.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}