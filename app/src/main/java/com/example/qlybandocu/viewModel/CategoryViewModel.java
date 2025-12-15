package com.example.qlybandocu.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qlybandocu.models.ProductModel;
import com.example.qlybandocu.repository.ProductRepository;

public class CategoryViewModel extends ViewModel {
    private ProductRepository productRepository;

    public CategoryViewModel() {
        productRepository = new ProductRepository();
    }
    public MutableLiveData<ProductModel> productModelMutableLiveData(int idcate){
        return productRepository.getProducts(idcate);
    }
}
