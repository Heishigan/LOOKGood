package com.example.lookgood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText email, password;
    Button login;
    TextView register, forgotPassword;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        email=findViewById(R.id.email);
        auth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        forgotPassword=findViewById(R.id.forgotPassword);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                if (TextUtils.isEmpty(Email)) {
                    email.setError("This field is required");
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    password.setError("This field is required");
                    return;
                }
                if (Password.length() < 6) {
                    password.setError("Password must contain more than 6 characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Login successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Categories.class));
                        }else{
                            Toast.makeText(Login.this, "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });

            }
        });
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText userEmail=new EditText(v.getContext());
                final AlertDialog.Builder resetPasswordDialog= new AlertDialog.Builder(v.getContext());
                resetPasswordDialog.setTitle("Have you forgotten the password?");
                resetPasswordDialog.setMessage("Enter the email associated with your account to reset the password.");
                resetPasswordDialog.setView(userEmail);
                resetPasswordDialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = userEmail.getText().toString();
                        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "The password reset link has been sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "An error has occured while sending the reset link", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                resetPasswordDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                resetPasswordDialog.create().show();

            }
        });

}

}