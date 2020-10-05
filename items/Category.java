package com.eitan.shopik.items;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;
@Keep
public class Category implements Serializable {

    private final String name;
    private final String gender;
    private final ArrayList<RecyclerItem> recyclerItems;

    public Category(String name ,String gender,ArrayList<RecyclerItem> recyclerItems ){
        this.name = name;
        this.gender = gender;
        this.recyclerItems = recyclerItems;

    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public ArrayList<RecyclerItem> getRecyclerItems() {
        return recyclerItems;
    }

    public RecyclerItem getRecyclerItem(int position){
        if(recyclerItems != null){
            if( position >= 0 && position <= getRecyclerItemsSize())
                return recyclerItems.get(position);
        }
        return null;
    }

    public int getRecyclerItemsSize(){
        return recyclerItems != null ? recyclerItems.size() : 0;
    }
}
