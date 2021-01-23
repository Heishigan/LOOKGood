package com.example.lookgood.ViewHolder;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lookgood.Interface.ItemClickListener;
import com.example.lookgood.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtSize;
    public ImageView imgProductPic;
    public ImageButton deleteProduct;
    private static final String TAG = "CartViewHolder";
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        Log.d(TAG, "onStart: "+itemView.toString());
        txtProductName=itemView.findViewById(R.id.CartProductNameTextView);
        txtProductPrice=itemView.findViewById(R.id.productTotalPriceTextView);
        imgProductPic = itemView.findViewById(R.id.productImageView);
        txtSize=itemView.findViewById(R.id.productSize);
        deleteProduct=itemView.findViewById(R.id.deleteProductButton);




    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
