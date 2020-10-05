package com.eitan.shopik.viewModels;

import android.os.Build;
import android.util.ArraySet;
import android.util.Pair;

import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eitan.shopik.LikedUser;
import com.eitan.shopik.items.PreferredItem;
import com.eitan.shopik.items.ShoppingItem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Keep
public class MainModel extends ViewModel implements Serializable {

    private final MutableLiveData<Map<String,Map<String,Object>>> companies_info;
    private final MutableLiveData<Map<String, LikedUser>> customers_info;
    private final MutableLiveData<CopyOnWriteArrayList<ShoppingItem>> all_items;
    private final Set<String> swipedItems;
    private final MutableLiveData<PreferredItem> preferred;
    private final MutableLiveData<CopyOnWriteArrayList<ShoppingItem>> favorites;
    private final MutableLiveData<Integer> current_page;
    private final MutableLiveData<Pair<Integer,Integer>> currentItem;
    private final MutableLiveData<Integer> totalItems;

    public MainModel(){

        currentItem = new MutableLiveData<>();
        totalItems = new MutableLiveData<>();

        this.preferred = new MutableLiveData<>();
        current_page = new MutableLiveData<>();

        Map<String,Map<String,Object>> companies_info_map = new HashMap<>();
        this.companies_info = new MutableLiveData<>();
        this.companies_info.setValue(companies_info_map);

        Map<String,LikedUser> customers_info_map = new HashMap<>();
        this.customers_info = new MutableLiveData<>();
        this.customers_info.setValue(customers_info_map);

        CopyOnWriteArrayList<ShoppingItem> items = new CopyOnWriteArrayList<>();
        this.all_items = new MutableLiveData<>();
        all_items.setValue(items);

        this.favorites = new MutableLiveData<>();
        CopyOnWriteArrayList<ShoppingItem> favorites_list = new CopyOnWriteArrayList<>();
        this.favorites.setValue(favorites_list);

        swipedItems = new ArraySet<>();
    }

    public LiveData<Map<String, LikedUser>> getCustomers_info() {
        return customers_info;
    }
    public void setCompanies_info(String id,Map<String,Object> value) {
        Objects.requireNonNull(this.companies_info.getValue()).put(id,value);
    }
    public LiveData<Map<String,Map<String,Object>>> getCompanies_info() {
        return companies_info;
    }
    public void setCustomers_info(String id, LikedUser likedUser) {
        Objects.requireNonNull(this.customers_info.getValue()).put(id,likedUser);
    }

    private void markItemAsSeen(String item_id){
        for(ShoppingItem shoppingItem : Objects.requireNonNull(all_items.getValue())){
            if(shoppingItem.getId().equals(item_id)) {
                shoppingItem.setSeen(true);
                return;
            }
        }
    }

    public LiveData<CopyOnWriteArrayList<ShoppingItem>> getAll_items() {
        return all_items;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addItem(ShoppingItem shoppingItem) {
        Objects.requireNonNull(this.all_items.getValue()).add(shoppingItem);
        //update observers every 7 items block
        if(this.all_items.getValue().size() % 15 == 0){
            postAllItems();
        }
    }
    public void postAllItems() {
        CopyOnWriteArrayList<ShoppingItem> koko = this.all_items.getValue();
        all_items.postValue(koko);
    }

    //PAGE NUM
    public LiveData<Integer> getCurrent_page() {
        return current_page;
    }
    public void setCurrent_page(Integer current_page) {
        this.current_page.postValue(current_page);
    }

    //SWIPED
    public void addSwipedItemId(String id){
        swipedItems.add(id);
        markItemAsSeen(id);
    }
    public boolean isSwiped(String id){
        return swipedItems.contains(id);
    }

    //PREFERRED
    public LiveData<PreferredItem> getPreferred(){
        return preferred;
    }
    public void setPreferred(PreferredItem preferredItem){
        this.preferred.postValue(preferredItem);
    }

    //FAVORITES
    public LiveData<CopyOnWriteArrayList<ShoppingItem>> getFavorite() {
        return favorites;
    }
    public void addFavorite(ShoppingItem shoppingItem){
        Objects.requireNonNull(this.favorites.getValue()).add(shoppingItem );
        CopyOnWriteArrayList<ShoppingItem> koko = this.favorites.getValue();
        favorites.postValue(koko);
    }

    //CURRENT ITEM FETCH
    public MutableLiveData<Pair<Integer,Integer>> getCurrentItem() {
        return currentItem;
    }

    //TOTAL ITEMS NUM
    public MutableLiveData<Integer> getTotalItems() {
        return totalItems;
    }

}
