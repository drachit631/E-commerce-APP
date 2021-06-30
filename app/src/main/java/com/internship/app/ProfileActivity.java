package com.internship.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView userName,userContact,userEmail;
    String email,contact,name;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName=findViewById(R.id.user_profile_name);
        userContact=findViewById(R.id.user_profile_contact);
        userEmail=findViewById(R.id.user_profile_email);

        toolbar=findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        });

        LoginActivity.sharedPreferences=getSharedPreferences(ConstantUrl.PREF,Context.MODE_PRIVATE);
        email= LoginActivity.sharedPreferences.getString(ConstantUrl.EMAIL,null);
        contact= LoginActivity.sharedPreferences.getString(ConstantUrl.CONTACT,null);
        name=LoginActivity.sharedPreferences.getString(ConstantUrl.NAME,null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(email!=null || contact!=null || name!=null){
          userName.setText(name);
          userEmail.setText(email);
          userContact.setText(contact);
        }else{
            userName.setText("Failed to fetch data");
            userEmail.setText("Failed to fetch data");
            userContact.setText("Failed to fetch data");
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        finishAffinity();
    }

}
