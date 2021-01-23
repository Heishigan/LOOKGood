package com.example.lookgood.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lookgood.Interface.ItemClickListener;
import com.example.lookgood.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView productName, productPrice, productAvailability,productDescription;
    public ImageView productImage;
    public ItemClickListener itemClickListener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage = (ImageView) itemView.findViewById(R.id.productimageView3);
        productName = (TextView) itemView.findViewById(R.id.productNameTextView);
        productPrice = (TextView) itemView.findViewById(R.id.priceTextView);
        productDescription = (TextView) itemView.findViewById(R.id.productDescription);

    }
    public void setItemClickListener(ItemClickListener listener)
    {
        this.itemClickListener=listener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false );

    }
}
