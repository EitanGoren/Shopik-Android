package com.eitan.shopik.viewModels;

import android.app.Application;

import androidx.annotation.Keep;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.Serializable;

@Keep
public class GenderModel extends AndroidViewModel implements Serializable {

    private final MutableLiveData<String> gender;
    private final MutableLiveData<String> type;
    private final MutableLiveData<String> imageUrl;
    private final MutableLiveData<String> name;
    private final MutableLiveData<String> sub_category;

    public GenderModel(Application application){
        super(application);
        this.gender = new MutableLiveData<>();
        this.type = new MutableLiveData<>();
        this.name = new MutableLiveData<>();
        this.imageUrl = new MutableLiveData<>();
        this.sub_category = new MutableLiveData<>();
    }

    public LiveData<String> getGender(){
        return gender;
    }

    public LiveData<String> getType(){
        return type;
    }

    public LiveData<String> getImageUrl(){
        return imageUrl;
    }

    public LiveData<String> getSub_category() {
        return sub_category;
    }

    public void setGender(String gender){
        this.gender.setValue(gender);
    }

    public void setType(String type){
        this.type.setValue(type);
    }

    public void setName(String name){
        this.name.setValue(name);
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl.setValue(imageUrl);
    }

    public void setSub_category(String sub_category) {
        this.sub_category.setValue(sub_category);
    }
}
