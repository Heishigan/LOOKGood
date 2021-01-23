package com.example.lookgood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    TextView fullName, email, phone;
    DrawerLayout drawerLayout;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String UID;
    Button editProfile, resetPassword;
    FirebaseUser user;
    private ImageView searchButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        fullName = findViewById(R.id.name);
        email = findViewById(R.id.mail);
        phone = findViewById(R.id.number);
        searchButton = (ImageView)findViewById(R.id.searchMe);
        searchButton.setVisibility(View.INVISIBLE);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        editProfile = findViewById(R.id.editProfile);
        resetPassword = findViewById(R.id.resetPassword);
        UID = auth.getCurrentUser().getUid();
        user = auth.getCurrentUser();
        DocumentReference documentReference = db.collection("users").document(UID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                email.setText(documentSnapshot.getString("Email"));
                fullName.setText(documentSnapshot.getString("FullName"));
                phone.setText(documentSnapshot.getString("Phone"));
                //debug
                Log.d("myTag", "This is my message"+documentReference+" "+documentSnapshot);



            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), com.example.lookgood.editProfile.class);
                i.putExtra("FullName", fullName.getText().toString());
                i.putExtra("Email", email.getText().toString());
                i.putExtra("Phone", phone.getText().toString());
                startActivity(i);

            }
        });
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText userPassword=new EditText(v.getContext());
                final AlertDialog.Builder resetPasswordDialog= new AlertDialog.Builder(v.getContext());
                resetPasswordDialog.setTitle("Reset Password");
                resetPasswordDialog.setMessage("Enter the new password");
                resetPasswordDialog.setView(userPassword);
                resetPasswordDialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPassword = userPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Your password has been updated successfully updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "An error has occured while resetting the password", Toast.LENGTH_SHORT).show();

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
        Intent intent = new Intent(MainActivity.this, Login.class);
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