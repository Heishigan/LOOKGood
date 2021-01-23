package com.example.lookgood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class finalOrder extends AppCompatActivity {
    Button backToShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_order);
        backToShop = (Button) findViewById(R.id.backToShop);
        backToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(finalOrder.this, NavDrawer.class);
                startActivity(intent);
            }
        });
    }
}