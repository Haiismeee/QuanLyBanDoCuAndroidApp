package com.example.qlybandocu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qlybandocu.databinding.ItemProductBinding;
import com.example.qlybandocu.listener.EventClickListener;
import com.example.qlybandocu.models.Products;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private List<Products> productsList;
    private EventClickListener listener;

    public ProductAdapter(List<Products> productsList, EventClickListener listener) {
        this.productsList = productsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setBinding(productsList.get(position));
        Glide.with(holder.itemView).load(productsList.get(position).getStrProductThumb()).into(holder.binding.image);
        
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ItemProductBinding binding;

        public MyViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        private void setBinding(Products products){
            binding.setProductitem(products);
            binding.executePendingBindings();

        }
    }
}
