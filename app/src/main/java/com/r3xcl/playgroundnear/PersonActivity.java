package com.r3xcl.playgroundnear;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class PersonActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String SharedPrefName = "auth_shared_pref";

    String phonenum;
    TextView test_text;
    Window window;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        getSupportActionBar().hide();

        test_text = findViewById(R.id.test_text);

        sharedPreferences = getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);

        phonenum = sharedPreferences.getString("phonenum","");

        test_text.setText(phonenum);

        window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.bar_auth));
    }
}