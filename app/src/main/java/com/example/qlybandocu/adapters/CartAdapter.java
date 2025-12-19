package com.example.qlybandocu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qlybandocu.Utils.Utils;
import com.example.qlybandocu.databinding.ItemCartBinding;
import com.example.qlybandocu.listener.ChangeNumListener;
import com.example.qlybandocu.models.Cart;

import java.text.DecimalFormat;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.internal.Util;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private Context context;
    private List<Cart> cartList;
    private ChangeNumListener changeNumListener;

    public CartAdapter(Context context, List<Cart> cartList, ChangeNumListener changeNumListener) {
        this.context = context;
        this.cartList = cartList;
        this.changeNumListener = changeNumListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding cartBinding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(cartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.binding.txtname.setText(cart.getProductDetail().getProductname());
        Glide.with(context).load(cart.getProductDetail().getStrproductthumb()).into(holder.binding.imageCart);

        // --- PHẦN XỬ LÝ GIÁ TIỀN (VND) ---
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        // 1. Hiển thị đơn giá (Ví dụ: 34,500,000 đ)
        holder.binding.txtprice.setText(decimalFormat.format(cart.getProductDetail().getPrice()) + " đ");

        // 2. Hiển thị tổng tiền của dòng này (Số lượng * Đơn giá)
        double tongTienDong = cart.getAmount() * cart.getProductDetail().getPrice();
        holder.binding.txtprice2.setText(decimalFormat.format(tongTienDong) + " đ");
        // ---------------------------------

        holder.binding.imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(holder.getAdapterPosition());
                notifyDataSetChanged();
                changeNumListener.change();
            }
        });

        holder.binding.imageSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subToCart(holder.getAdapterPosition());
                notifyDataSetChanged();
                changeNumListener.change();
            }
        });

        holder.binding.txtamount.setText(cart.getAmount() + "");
    }

    private void subToCart(int adapterPosition) {
        if (Utils.cartList.get(adapterPosition).getAmount() ==1 ){
            Utils.cartList.remove(adapterPosition);
        }else {
            Utils.cartList.get(adapterPosition).setAmount(Utils.cartList.get(adapterPosition).getAmount() - 1);
        }
        Paper.book().write("cart", Utils.cartList);
    }

    private void addToCart(int adapterPosition) {
        Utils.cartList.get(adapterPosition).setAmount(Utils.cartList.get(adapterPosition).getAmount() + 1);
        Paper.book().write("cart", Utils.cartList);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemCartBinding binding;

        public MyViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
