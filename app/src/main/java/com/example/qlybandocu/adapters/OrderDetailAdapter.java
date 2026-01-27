package com.example.qlybandocu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qlybandocu.R;
import com.example.qlybandocu.models.OrderDetail;

import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailAdapter
        extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    List<OrderDetail> list;

    public OrderDetailAdapter(List<OrderDetail> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {

        OrderDetail d = list.get(i);

        h.tvName.setText(d.getProductName());
        h.tvQuantity.setText("Số lượng: " + d.getQuantity());

        DecimalFormat df = new DecimalFormat("###,###,###");
        h.tvPrice.setText(df.format(d.getPrice()) + " đ");

        h.tvSellerName.setText("Người bán: " + d.getSellerName());
        h.tvSellerPhone.setText("SĐT: " + d.getSellerPhone());

        Glide.with(h.itemView.getContext())
                .load(d.getProductImage())
                .into(h.imgProduct);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public double getTotalPrice() {
        double total = 0;
        for (OrderDetail item : list) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView tvName, tvQuantity, tvPrice;
        TextView tvSellerName, tvSellerPhone;

        public ViewHolder(@NonNull View v) {
            super(v);
            imgProduct = v.findViewById(R.id.imgProduct);
            tvName = v.findViewById(R.id.tvName);
            tvQuantity = v.findViewById(R.id.tvQuantity);
            tvPrice = v.findViewById(R.id.tvPrice);

            tvSellerName = v.findViewById(R.id.tvSellerName);
            tvSellerPhone = v.findViewById(R.id.tvSellerPhone);
        }
    }
}

