package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import javax.microedition.khronos.egl.EGLDisplay;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private EditText Name, Email, Password, Phone;
    private TextView Register, Create;
    private Button Registerbutton;
    private ProgressBar progressBar2;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            Name = (EditText) findViewById(R.id.Name);
            Email = (EditText) findViewById(R.id.Email);
            Password = (EditText) findViewById(R.id.Password);
            Phone = (EditText) findViewById(R.id.Phone);

            Registerbutton = (Button) findViewById(R.id.Registerbtn);
//            Registerbutton.setOnClickListener(this);

//            progressBar2.findViewById(R.id.progressbar2);
            mAuth = FirebaseAuth.getInstance();


        }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Registerbtn:
                registerUser();

    }
}

    private void registerUser() {
        String fullName = Name.getText().toString().trim();
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        String phone = Phone.getText().toString().trim();

        if(fullName.isEmpty()){
            Name.setError("Full Name is required");
            Name.requestFocus();
            return;
        }

        if(email.isEmpty()){
            Email.setError("Email is required");
            Email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Please provide valid email");
            Email.requestFocus();
            return;
        }

        if(password.isEmpty()){
            Password.setError("Password is required");
            Password.requestFocus();
            return;
        }

        if(password.length()<6){
            Password.setError("Minimum length for password is 6");
            Password.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            Phone.setError("Phone number is required");
            Phone.requestFocus();
            return;
        }

        if(!Patterns.PHONE.matcher(phone).matches()){
            Phone.setError("Provide a valid phone number");
            Phone.requestFocus();
            return;
        }

        progressBar2.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            User user = new User(fullName, email, phone);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        progressBar2.setVisibility(View.VISIBLE);

                                        //redirect to login page
                                    } else {
                                        Toast.makeText(Register.this, "failed to register! Try again", Toast.LENGTH_LONG).show();
                                        progressBar2.setVisibility(View.GONE);
                                    }

                                }
                            });
                        }else {
                            Toast.makeText(Register.this,"failed to register! Try again", Toast.LENGTH_LONG).show();
                            progressBar2.setVisibility(View.GONE);

                        }
                    }
                });
    }
}