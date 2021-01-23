package com.example.lookgood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lookgood.Model.Carts;
import com.example.lookgood.Model.Products;
import com.example.lookgood.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class Cart extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button PlaceOrder;
    private ImageButton deleteProduct;
    private TextView txttotalAmount;
    private int totalAmout=0;
    private int singleProductPrice=0;
    FirebaseAuth auth;
    DrawerLayout drawerLayout;
    FirebaseUser user;
    String productAddedTime;
    private static final String TAG = "Cart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        drawerLayout = findViewById(R.id.drawer_layout);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        auth= FirebaseAuth.getInstance();;
        user = auth.getCurrentUser();
        PlaceOrder= (Button) findViewById(R.id.placeOrderButton);
        txttotalAmount = (TextView) findViewById(R.id.orderTotalTextView);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        DatabaseReference userNameRef = rootRef.child("User View");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    PlaceOrder.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };
        userNameRef.addListenerForSingleValueEvent(eventListener);

        PlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
                cartListRef.child("User View").child(user.getUid())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            finish();
                            Intent intent = new Intent(Cart.this, finalOrder.class);
                            startActivity(intent);


                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Cart.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Carts> options =
                new FirebaseRecyclerOptions.Builder<Carts>()
                .setQuery(cartListRef.child("User View").child(user.getUid()).child("Products"),Carts.class).build();
        Log.d(TAG, "onStart: "+options.toString());
        FirebaseRecyclerAdapter<Carts, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Carts, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Carts carts) {
                Log.d(TAG, "onBindViewHolder: ");

                cartViewHolder.txtProductName.setText(carts.getName());
                Picasso.get().load(carts.getImageURL()).into(cartViewHolder.imgProductPic);
                cartViewHolder.txtSize.setText(carts.getSize());
                cartViewHolder.txtProductPrice.setText(carts.getPrice());
                productAddedTime = carts.getTime();
                singleProductPrice = ((Integer.valueOf(carts.getPrice())));
                totalAmout = singleProductPrice + totalAmout;
                txttotalAmount.setText(String.valueOf(totalAmout));
                Log.d(TAG, "onBindViewHolder: "+totalAmout );





                cartViewHolder.deleteProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cartListRef.child("User View").child(user.getUid()).child("Products").child(carts.getTime())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Cart.this, "Item Removed From Cart", Toast.LENGTH_SHORT).show();
                                    finish();


                                }
                            }
                        });


                    }
                });

            }




            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                Log.d(TAG, "onStart: "+holder);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "onStart: "+adapter);
        adapter.startListening();


    }
    public void ClickMenu(View view){
        NavDrawer.openDrawer(drawerLayout);



    }
    public void ClickCart(View view){
        NavDrawer.redirectActivity(this,Cart.class);



    }

    public void ClickLogo(View view){
        NavDrawer.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view){
        NavDrawer.redirectActivity(this,Categories.class);

    }
    public void ClickEditProfile(View view){
        NavDrawer.redirectActivity(this,MainActivity.class);

    }
    public void ClickLogOut(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Cart.this, Login.class);
        startActivity(intent);
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavDrawer.closeDrawer(drawerLayout);
    }
}