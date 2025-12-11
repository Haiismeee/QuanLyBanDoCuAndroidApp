package com.example.qlybandocu.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qlybandocu.models.CategoryModel;
import com.example.qlybandocu.models.ProductModel;
import com.example.qlybandocu.repository.CategoryRepository;
import com.example.qlybandocu.repository.ProductRepository;

public class HomeViewModel extends ViewModel {
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    public HomeViewModel() {
        categoryRepository = new CategoryRepository();
        productRepository = new ProductRepository();
    }
    public MutableLiveData<CategoryModel> categoryModelMutableLiveData(){
        return categoryRepository.getCategory();
    }
    public MutableLiveData<ProductModel> productModelMutableLiveData(int idcate){
        return productRepository.getProducts(idcate);
    }
}
