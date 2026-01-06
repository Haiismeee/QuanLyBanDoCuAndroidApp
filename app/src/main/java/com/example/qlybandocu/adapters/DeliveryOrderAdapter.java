package com.example.qlybandocu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlybandocu.R;
import com.example.qlybandocu.models.MessageModel;
import com.example.qlybandocu.models.Order;
import com.example.qlybandocu.retrofit.BanDoCuApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryOrderAdapter
        extends RecyclerView.Adapter<DeliveryOrderAdapter.ViewHolder> {

    private List<Order> list;
    private BanDoCuApi api;

    public DeliveryOrderAdapter(List<Order> list, BanDoCuApi api) {
        this.list = list;
        this.api = api;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delivery_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {

        Order o = list.get(position);

        h.tvInfo.setText(
                "Đơn #" + o.getId() +
                        "\nTổng: " + o.getTotal_price() + " đ" +
                        "\nThanh toán: " + o.getPayment_method() +
                        "\nTrạng thái: " + convertStatus(o.getPayment_status())
        );

        // Nhận giao → status = 2
        h.btnShip.setOnClickListener(v ->
                updateStatus(o.getId(), 2)
        );

        // Đã giao → status = 3
        h.btnDone.setOnClickListener(v ->
                updateStatus(o.getId(), 3)
        );
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    // ================= UPDATE STATUS =================

    private void updateStatus(int idorder, int status) {

        api.updateOrderStatus(idorder, status)
                .enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(
                            Call<MessageModel> call,
                            Response<MessageModel> response
                    ) {
                        // demo → không reload
                    }

                    @Override
                    public void onFailure(
                            Call<MessageModel> call,
                            Throwable t
                    ) {
                    }
                });
    }

    // ================= CONVERT STATUS =================

    private String convertStatus(int status) {
        switch (status) {
            case 1:
                return "Đã đặt";
            case 2:
                return "Đang giao";
            case 3:
                return "Đã giao";
            case 4:
                return "Đã hủy";
            default:
                return "Không xác định";
        }
    }


    // ================= VIEW HOLDER =================

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvInfo;
        Button btnShip, btnDone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInfo = itemView.findViewById(R.id.tvInfo);
            btnShip = itemView.findViewById(R.id.btnShip);
            btnDone = itemView.findViewById(R.id.btnDone);
        }
    }
}
