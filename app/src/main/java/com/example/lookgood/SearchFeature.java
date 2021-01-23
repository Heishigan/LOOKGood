package com.example.lookgood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.lookgood.Model.Products;
import com.example.lookgood.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchFeature extends AppCompatActivity {
    private Button searchButton;
    private EditText inputText;
    private RecyclerView recyclerView;
    private String searchInput;
    private String cat = "";
    private static final String TAG = "SearchFeature";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_feature);
        inputText = findViewById(R.id.searchProduct);
        searchButton = findViewById(R.id.searchProductButton);
        recyclerView = findViewById(R.id.search_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchFeature.this));
        cat = Categories.getCategoryType();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = inputText.getText().toString();
                onStart();



            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products").child(cat);
        FirebaseRecyclerOptions<Products> options;
        options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("name").startAt(searchInput), Products.class)
                .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                        productViewHolder.productName.setText(products.getName());
                        productViewHolder.productPrice.setText(products.getPrice());
                        Picasso.get().load(products.getImageURL()).into(productViewHolder.productImage);
                        Log.d(TAG, "onBindViewHolder: "+cat);
                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SearchFeature.this, ProductDetails.class);
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