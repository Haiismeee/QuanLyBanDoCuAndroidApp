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

    private final List<OrderDetail> list;
    private final DecimalFormat df = new DecimalFormat("###,###,###");

    public OrderDetailAdapter(List<OrderDetail> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder h,
            int position
    ) {
        OrderDetail d = list.get(position);

        h.tvName.setText(d.getProductName());
        h.tvQuantity.setText("Số lượng: " + d.getQuantity());
        h.tvPrice.setText(df.format(d.getPrice()) + " đ");

        // ===== NGƯỜI BÁN (AN TOÀN NULL) =====
        if (d.getSellerName() != null) {
            h.tvSellerName.setText("Người bán: " + d.getSellerName());
        } else {
            h.tvSellerName.setText("Người bán: ---");
        }

        if (d.getSellerPhone() != null) {
            h.tvSellerPhone.setText("SĐT: " + d.getSellerPhone());
        } else {
            h.tvSellerPhone.setText("SĐT: ---");
        }

        Glide.with(h.itemView.getContext())
                .load(d.getProductImage())
                .into(h.imgProduct);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    // ===== TÍNH TỔNG TIỀN (ĐÚNG KIỂU) =====
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
