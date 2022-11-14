package com.example.myapplicationapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class CodeSend extends AppCompatActivity {

    EditText enterNumber,enterPassword;
    Button buttonOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_send);
        getSupportActionBar().setTitle("                          Transport App");

        enterNumber = findViewById(R.id.enter_number);
        enterPassword = findViewById(R.id.enter_password);
        buttonOtp = findViewById(R.id.buttonOtp);

        buttonOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             String username = enterNumber.getText().toString();
             String password = enterPassword.getText().toString();
             if (username.equals("user") && (password.equals("123456"))){
                 Toast.makeText(CodeSend.this, "User Login", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(getApplicationContext(),UserForm.class);
                 startActivity(intent);
             }else{
                 Toast.makeText(CodeSend.this, "plz input correct username and password!!", Toast.LENGTH_SHORT).show();
             }
            }
        });
    }
}