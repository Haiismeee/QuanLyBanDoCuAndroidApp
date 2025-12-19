package com.example.qlybandocu.Utils;

import com.example.qlybandocu.models.Cart;
import com.example.qlybandocu.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<Cart> cartList = new ArrayList<>();
    public static final String BASE_URL = "http://192.168.1.6/banhang/";
    public static UserModel user_current = new UserModel();
}
}
