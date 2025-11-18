package com.example.qlybandocu.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qlybandocu.models.CategoryModel;
import com.example.qlybandocu.repository.CategoryRepository;

public class HomeViewModel extends ViewModel {
    private CategoryRepository categoryRepository;

    public HomeViewModel() {
        categoryRepository = new CategoryRepository();
    }
    public MutableLiveData<CategoryModel> categoryModelMutableLiveData(){
        return categoryRepository.getCategory();
    }
}
