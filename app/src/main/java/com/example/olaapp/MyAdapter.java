package com.example.olaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> list;
    SelectListener listener;

    public MyAdapter(Context context, ArrayList<User> list,SelectListener listener) {
        this.context = context;
        this.list = list;
        this.listener=listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.userentry,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user=list.get(position);
        holder.email.setText(user.getEmail());
        holder.phone.setText(user.getPhone());
        holder.number.setText(user.getNumber());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(list.get(position));
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  static  class MyViewHolder extends  RecyclerView.ViewHolder{
            TextView email,phone,number;
            CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cv);
            email=itemView.findViewById(R.id.textEmail);
            phone=itemView.findViewById(R.id.textPhone);
            number=itemView.findViewById(R.id.textNumber);
        }
    }
}
