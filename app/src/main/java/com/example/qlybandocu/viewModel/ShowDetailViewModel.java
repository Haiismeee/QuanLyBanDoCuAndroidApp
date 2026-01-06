package com.example.qlybandocu.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qlybandocu.repository.ProductDetailRepository;

public class ShowDetailViewModel extends ViewModel {
    private ProductDetailRepository productDetailRepository;

    public ShowDetailViewModel(){
        productDetailRepository = new ProductDetailRepository();

    }
    public MutableLiveData<ProductDetailModel> productDetailModelMutableLiveData(int id){
        return productDetailRepository.getProductDetail(id);
    }
}
