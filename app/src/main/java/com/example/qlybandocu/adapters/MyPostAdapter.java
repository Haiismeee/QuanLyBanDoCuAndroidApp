package com.example.qlybandocu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qlybandocu.R;
import com.example.qlybandocu.models.Products;

import java.util.List;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ViewHolder> {

    Context context;
    List<Products> list;

    public MyPostAdapter(Context context, List<Products> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_my_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
        Products p = list.get(i);

        h.tvName.setText(p.getStrProduct());

        Glide.with(context)
                .load(p.getStrProductThumb())
                .into(h.imgThumb);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumb;
        TextView tvName;

        public ViewHolder(@NonNull View v) {
            super(v);
            imgThumb = v.findViewById(R.id.imgThumb);
            tvName = v.findViewById(R.id.tvName);
        }
    }
}
