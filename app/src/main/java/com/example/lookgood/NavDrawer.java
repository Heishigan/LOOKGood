package com.example.lookgood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.lookgood.Model.Products;
import com.example.lookgood.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class NavDrawer extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private DatabaseReference productReferecne;
    RecyclerView.LayoutManager layoutManager;
    public Button addToCartButton;
    private RecyclerView recyclerView;
    private String productID = "";
    private String cat = "";
    private static final String TAG = "NavDrawer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        drawerLayout = findViewById(R.id.drawer_layout);
        ProductDetails productDetails = new ProductDetails();
        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView)findViewById(R.id.shopRecyclerView);
        recyclerView.setHasFixedSize(true);
        addToCartButton= (Button) findViewById(R.id.addToCartButton);
        productID = getIntent().getStringExtra("name");
        cat = Categories.getCategoryType();
        Log.d(TAG, "onCreate: "+cat);
        productReferecne = FirebaseDatabase.getInstance().getReference().child("Products").child(cat);
        RecyclerView.LayoutManager layoutmanager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutmanager);

        Log.d(TAG, "onCreate: "+productReferecne);



    }



    public void ClickSearch(View view)
    {
        redirectActivity(this,SearchFeature.class);
    }



    public void ClickMenu(View view){
        openDrawer(drawerLayout);
        
    }
    public void ClickCart(View view){
        NavDrawer.redirectActivity(this,Cart.class);



    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
        Log.d(TAG, "ClickMenu: " +drawerLayout.toString());
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
        redirectActivity(this,Categories.class);
    }
    public void ClickCategories(View view){
//        redirectActivity(this,);


    }
    public void ClickEditProfile(View view){
        redirectActivity(this, MainActivity.class);


    }
    public void ClickLogOut(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(NavDrawer.this, Login.class);
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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productReferecne, Products.class)
                .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products)
            {
                productViewHolder.productName.setText(products.getName());
                productViewHolder.productPrice.setText(products.getPrice());
                Picasso.get().load(products.getImageURL()).into(productViewHolder.productImage);
                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NavDrawer.this, ProductDetails.class);
                        intent.putExtra("name", products.getName());
                        startActivity(intent);

                    }
                });





            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productslayout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}