package com.test.myscrollviewproject;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private int centerIndex=2;
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolder viewHolder=new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder viewHolder, int position) {
        if(position%2==0){
            viewHolder.tv.setBackgroundColor(Color.BLUE);
        }else{
            viewHolder.tv.setBackgroundColor(Color.GRAY);
        }
    }
    @Override
    public int getItemCount() {
        return 20;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.tv);
        }
    }
}
