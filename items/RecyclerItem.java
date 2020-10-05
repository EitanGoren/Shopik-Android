package com.eitan.shopik.items;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;
@Keep
public class RecyclerItem implements Serializable {

    private final String sellerImageUrl;
    private String text;
    private String type;
    private String seller;
    private String gender;
    private String userImageUrl;
    private String item_sub_category;
    private String id;
    private String link;
    private ArrayList<String> images;
    private long likes;
    private String image_resource;

    public RecyclerItem(String text,String sellerImageUrl, String link){

        this.text = text;
        this.sellerImageUrl = sellerImageUrl;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public long getLikes() {
        return likes;
    }
    public void setLikes(long likes) {
        this.likes = likes;
    }

    public ArrayList<String> getImages() {
        return images;
    }
    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setImage_resource(String image_resource) {
        this.image_resource = image_resource;
    }
    public String getImage_resource() {
        return image_resource;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }
    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getItem_sub_category() {
        return item_sub_category;
    }
    public void setItem_sub_category(String item_sub_category) {
        this.item_sub_category = item_sub_category;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getSellerLogoUrl() {
        return sellerImageUrl;
    }

}
