package com.example.lookgood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editProfile extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText editFullName, editEmail, editPhone;
    Button goBack, save;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser user;
    DrawerLayout drawerLayout;
    private ImageView searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        drawerLayout = findViewById(R.id.drawer_layout);


        Intent data = getIntent();
        String fullName = data.getStringExtra("FullName");
        String email = data.getStringExtra("Email");
        String phone = data.getStringExtra("Phone");
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        searchButton = (ImageView)findViewById(R.id.searchMe);
        searchButton.setVisibility(View.INVISIBLE);
        editFullName=findViewById(R.id.editFullName);
        editEmail=findViewById(R.id.editEmail);
        editPhone=findViewById(R.id.editPhone);
        save= findViewById(R.id.save);
        editFullName.setText(fullName);
        editEmail.setText(email);
        editPhone.setText(phone);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editFullName.getText().toString().isEmpty() || editEmail.getText().toString().isEmpty()|| editPhone.getText().toString().isEmpty()){
                    Toast.makeText(editProfile.this, "One or many fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = editEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference documentReference = db.collection("users").document(user.getUid());
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("Email", editEmail.getText().toString());
                        edited.put("FullName", editFullName.getText().toString());
                        edited.put("Phone", editPhone.getText().toString());
                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(editProfile.this, "Your details have been successfully updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent (getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        });
                        Toast.makeText(editProfile.this, "Your details have been successfully updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editProfile.this, "An error has occured while updating your details", Toast.LENGTH_SHORT).show();
                    }
                });


                
            }
        });


    }
    public void ClickSearch(View view)
    {
        NavDrawer.redirectActivity(this,SearchFeature.class);
    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);

    }
    public void ClickCart(View view){
        NavDrawer.redirectActivity(this,Cart.class);



    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);

    }
    public void ClickLogo(View view){
        closeDrawer(drawerLayout);

    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public void ClickHome(View view){
        redirectActivity(this, Categories.class);

    }
    public void ClickCategories(View view){
//        redirectActivity(this,);


    }
    public void ClickEditProfile(View view){
        redirectActivity(this, MainActivity.class);


    }
    public void ClickLogOut(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(editProfile.this, Login.class);
        startActivity(intent);
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public static void redirectActivity(Activity activity, Class aclass) {
        Intent intent = new Intent(activity, aclass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }


}