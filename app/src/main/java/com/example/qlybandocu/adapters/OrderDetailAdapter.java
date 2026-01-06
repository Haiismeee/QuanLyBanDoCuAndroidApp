package com.example.qlybandocu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        h.tvName.setText(d.getName());
        h.tvQty.setText("Số lượng: " + d.getQuantity());

        DecimalFormat df = new DecimalFormat("###,###,###");
        h.tvPrice.setText("Giá: " + df.format(d.getPrice()) + " đ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvPrice;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvQty = v.findViewById(R.id.tvQty);
            tvPrice = v.findViewById(R.id.tvPrice);
        }
    }
}
