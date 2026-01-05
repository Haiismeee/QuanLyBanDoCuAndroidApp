package com.example.qlybandocu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlybandocu.R;
import com.example.qlybandocu.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    List<Order> list;

    public OrderAdapter(List<Order> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
        Order o = list.get(i);
        h.tvId.setText("Đơn #" + o.getId());
        h.tvPrice.setText("Tổng: " + o.getTotal_price() + " đ");
        h.tvMethod.setText("Thanh toán: " + o.getPayment_method());
        h.tvStatus.setText("Trạng thái: " + o.getPayment_status());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvPrice, tvMethod, tvStatus;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvId = v.findViewById(R.id.tvOrderId);
            tvPrice = v.findViewById(R.id.tvOrderPrice);
            tvMethod = v.findViewById(R.id.tvOrderMethod);
            tvStatus = v.findViewById(R.id.tvOrderStatus);
        }
    }
}
