package com.example.qlybandocu.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlybandocu.R;
import com.example.qlybandocu.activities.OrderDetailActivity;
import com.example.qlybandocu.models.Order;

import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    List<Order> list;

    public OrderAdapter(List<Order> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        Order order = list.get(position);

        holder.tvId.setText("Đơn #" + order.getId());

        DecimalFormat df = new DecimalFormat("###,###,###");
        holder.tvPrice.setText(
                "Tổng: " + df.format(order.getTotal_price()) + " đ"
        );

        holder.tvMethod.setText(
                "Thanh toán: " + order.getPayment_method()
        );

        // ===== HIỂN THỊ TRẠNG THÁI =====
        String statusText;

        switch (order.getPayment_status()) {
            case 1:
                statusText = "Đã đặt";
                break;
            case 2:
                statusText = "Đang giao";
                break;
            case 3:
                statusText = "Hoàn thành";
                break;
            case 4:
                statusText = "Đã hủy";
                break;
            default:
                statusText = "Không xác định";
        }

        holder.tvStatus.setText("Trạng thái: " + statusText);

        // ===== CLICK → CHI TIẾT ĐƠN =====
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    OrderDetailActivity.class
            );

            intent.putExtra("idorder", order.getId());

            // ⭐ TRUYỀN STATUS SANG OrderDetailActivity
            intent.putExtra(
                    "status",
                    order.getPayment_status()
            );

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvPrice, tvMethod, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvOrderId);
            tvPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvMethod = itemView.findViewById(R.id.tvOrderMethod);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }
}
