package com.example.lookgood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText fullName, email, password, phone;
    Button register;
    TextView login;
    FirebaseAuth auth;
    ProgressBar progressBar;
    FirebaseFirestore db;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fullName=findViewById(R.id.fullName);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        phone=findViewById(R.id.phone);
        register=findViewById(R.id.register);
        login=findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();
        if(auth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(), NavDrawer.class));
        }





        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String FullName= fullName.getText().toString();
                String Phone= phone.getText().toString();
                if (TextUtils.isEmpty(Email)){
                    email.setError("This field is required");
                return;
                }
                if (TextUtils.isEmpty(Password)){
                    password.setError("This field is required");
                    return;
                }
                if (Password.length()<6){
                    password.setError("Password must contain more than 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            UID = auth.getCurrentUser().getUid();
                            DocumentReference documentReference = db.collection("users").document(UID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("FullName",FullName);
                            user.put("Email",Email);
                            user.put("Phone", Phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: User profile successfully created for"+UID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: "+e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            Toast.makeText(Register.this, "An error has occured while registering the account", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            
                        }
                    }
                });


            }


        });
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });


    }
}