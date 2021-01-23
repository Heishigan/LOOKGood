package com.example.lookgood.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;

import com.example.lookgood.Interface.ItemClickListener;
import com.example.lookgood.Model.Comment;
import com.example.lookgood.Model.Products;
import com.example.lookgood.R;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>
{
    private Context mContext;
    private List<Comment> mData;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mData=mData;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rowcomment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.textCommentName.setText(mData.get(position).getUname());
        holder.textCommentContent.setText(mData.get(position).getContent());
        holder.textTimeStamp.setText(timestampToString((Long) mData.get(position).getTimestamp()));



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder
    {
        TextView textCommentName, textCommentContent, textTimeStamp;
        public CommentViewHolder(View itemView) {
            super(itemView);
            textCommentName=itemView.findViewById(R.id.comment_username);
            textCommentContent=itemView.findViewById(R.id.comment_content);
            textTimeStamp=itemView.findViewById(R.id.timeStamp);

        }
    }
    private String timestampToString(long time)
    {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("hh:mm", calendar).toString();
        return date;
    }
}

