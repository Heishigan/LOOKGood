package com.example.lookgood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Categories extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private DatabaseReference productReferecne;
    LinearLayout linearLayout1, linearLayout2, linearLayout3;
    private static String categoryType = "";
    private ImageView searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        drawerLayout = findViewById(R.id.drawer_layout);
        linearLayout1 = (LinearLayout) findViewById(R.id.men);
        linearLayout2 = (LinearLayout) findViewById(R.id.Women);
        linearLayout3 = (LinearLayout) findViewById(R.id.Kids);
        searchButton = (ImageView)findViewById(R.id.searchMe);
        searchButton.setVisibility(View.INVISIBLE);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryType = "Men";
                Intent intent = new Intent(Categories.this, NavDrawer.class);
                startActivity(intent);



            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryType = "Women";
                Intent intent = new Intent(Categories.this, NavDrawer.class);
                startActivity(intent);

            }
        });
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryType = "Kids";
                Intent intent = new Intent(Categories.this, NavDrawer.class);
                startActivity(intent);

            }
        });


    }
    public void ClickSearch(View view)
    {
        NavDrawer.redirectActivity(this,SearchFeature.class);
    }
    public static String getCategoryType()
    {
        return categoryType;
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
        recreate();

    }
    public void ClickCategories(View view){
//        redirectActivity(this,);


    }
    public void ClickEditProfile(View view){
        redirectActivity(this, MainActivity.class);


    }
    public void ClickLogOut(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Categories.this, Login.class);
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