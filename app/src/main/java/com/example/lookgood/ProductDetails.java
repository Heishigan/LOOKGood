package com.example.lookgood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lookgood.Model.Comment;
import com.example.lookgood.Model.Products;
import com.example.lookgood.ViewHolder.CommentAdapter;
import com.example.lookgood.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView productImage;
    DrawerLayout drawerLayout;
    private TextView productName, productPrice, productDescription;
    private Button addToCart, addComment;
    private EditText comment;
    String postKey;
    private ImageButton shareButton;
    FirebaseAuth auth;
    FirebaseUser user;
    RecyclerView.LayoutManager layoutManager;
    private String productID = "";
    public String productImageURL;
    public String sizeSelected;
    private static final String TAG = "ProductDetails";
    private String cat = "";
    FirebaseDatabase firebaseDatabase;
    private RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_nav_drawer);
        drawerLayout = findViewById(R.id.drawer_layout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Spinner spinner = findViewById(R.id.sizeSelector);
        auth= FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();
        shareButton = findViewById(R.id.shareButton);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.size, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        addToCart = (Button) findViewById(R.id.addToCartButton);
        productImage = (ImageView) findViewById(R.id.productImageView);
        productName = (TextView) findViewById(R.id.productNameTextView);
        productPrice = (TextView) findViewById(R.id.priceTextView);
        productDescription = (TextView)findViewById(R.id.productDescription);
        comment = (EditText) findViewById(R.id.commentText);
        addComment=(Button) findViewById(R.id.comment);
        cat = Categories.getCategoryType();
        String userIID=user.getUid();
        final String[] nameOfTheUser = {""};
        recyclerView = findViewById(R.id.commentRecycler);
        recyclerView();


        productID = getIntent().getStringExtra("name");
        getProductDetails(productID);
        FirebaseFirestore.getInstance().collection("users").document(userIID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Map<String, Object> fNameMap = document.getData();

                        for (Map.Entry<String, Object> dataEntry : fNameMap.entrySet()) {
                            if (dataEntry.getKey().equals("FullName")) {
                                nameOfTheUser[0] = dataEntry.getValue().toString();
                            }
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.setVisibility(View.INVISIBLE);
                DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(productID).push();
                String commentContent = comment.getText().toString();
                String userID = user.getUid();
                String userName = nameOfTheUser[0];
                Comment commentQuery = new Comment(commentContent, userID, userName);
                commentReference.setValue(commentQuery).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProductDetails.this, "Comment Added Succesfully", Toast.LENGTH_SHORT).show();
                        comment.setText("");
                        comment.setVisibility(View.VISIBLE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductDetails.this, "Failed to add a comment", Toast.LENGTH_SHORT).show();

                    }
                });






            }
        });


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddingToCartList();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "LOOKGood Product Link";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share Using"));

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "recyclerView: "+productID);
        DatabaseReference commentReft = firebaseDatabase.getReference("Comment").child(productID);
        commentReft.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComment = new ArrayList<>();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Comment comment = snapshot1.getValue(Comment.class);

                    listComment.add(comment);

                }
                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                recyclerView.setAdapter(commentAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: "+error);

            }
        });








    }

    private void recyclerView() {


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
        Intent intent = new Intent(ProductDetails.this, Login.class);
        startActivity(intent);
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavDrawer.closeDrawer(drawerLayout);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         sizeSelected = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void adddingToCartList() {
        Products products = new Products();
        String saveCurrentTime, saveCurrentDate;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("name", productID);
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("imageURL", productImageURL);
        cartMap.put("size", sizeSelected);
        cartListRef.child("User View").child(user.getUid())
                .child("Products").child(saveCurrentTime)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProductDetails.this, "Succesfully added to cart", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProductDetails.this, NavDrawer.class);
                            startActivity(intent);


                        }

                    }
                });


    }

    public void getProductDetails(String productID) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(cat);

        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Products products = snapshot.getValue(Products.class);
                    productName.setText(products.getName());
                    productPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImageURL()).into(productImage);
                    productImageURL = products.getImageURL();
                    productDescription.setText(products.getProductDescription());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}