package com.eitan.shopik.items;

import androidx.annotation.Keep;

import com.eitan.shopik.LikedUser;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.io.Serializable;
import java.util.ArrayList;
@Keep
public class ShoppingItem implements Serializable {

    private String id;
    private String brand;
    private String price;
    private String seller;
    private String seller_id;
    private String seller_logo_url;
    private String type;
    private String site_link;
    private String gender;
    private String reduced_price;
    private String sub_category;

    private ArrayList<String> name,images;
    private ArrayList<LikedUser> likedUsers,unlikedUsers;

    private boolean on_sale,isAd,isFavorite,isExclusive;
    private int percentage;
    private long likes,unlikes,page_num;
    private transient UnifiedNativeAd nativeAd;

    private boolean isSeen;

    public ShoppingItem(){}

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }

    public String getBrand(){
        return brand;
    }
    public void setBrand(String brand){
        if(brand.equals("")) {
            this.brand = "";
            return;
        }
        String[] pook = brand.split(" ");
        StringBuilder temp = new StringBuilder();
        for (String word : pook) {
            temp.append(word.substring(0, 1).toUpperCase()).append(word.toLowerCase().substring(1)).append(" ");
        }
        this.brand = temp.toString();
    }

    public String getPrice(){
        return price;
    }
    public void setPrice(String price){
        this.price = price;
    }

    public String getSeller(){
        return seller;
    }
    public void setSeller(String seller){
        this.seller = seller;
    }

    public ArrayList<String> getName(){
        return name;
    }
    public void setName( ArrayList<String> name ){
        this.name = name;
    }

    public boolean isOn_sale() {
        return on_sale;
    }
    public void setOn_sale(boolean on_sale) {
        this.on_sale = on_sale;
    }

    public String getSellerId() {
        return seller_id;
    }
    public void setSellerId(String seller_id) {
        this.seller_id = seller_id;
    }

    public void setSellerLogoUrl(String seller_logo_url) {
        this.seller_logo_url = seller_logo_url;
    }
    public String getSellerLogoUrl() {
        return seller_logo_url;
    }

    public int getPercentage() {
        return percentage;
    }
    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getSite_link() {
        return site_link;
    }
    public void setSite_link(String site_link) {
        this.site_link = site_link;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }
    public long getLikes() {
        return likes;
    }

    public void setUnlikes(long unlikes) {
        this.unlikes = unlikes;
    }
    public long getUnlikes() {
        return unlikes;
    }

    public boolean isAd() {
        return isAd;
    }
    public void setAd(boolean ad) {
        isAd = ad;
    }

    public void setNativeAd(UnifiedNativeAd ad) {
        this.nativeAd = ad;
    }
    public UnifiedNativeAd getNativeAd() {
        return nativeAd;
    }
    public void destroyAd(){
        this.nativeAd.destroy();
    }

    public boolean isFavorite() {
        return isFavorite;
    }
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public long getPage_num() {
        return page_num;
    }
    public void setPage_num(long page_num) {
        this.page_num = page_num;
    }

    public ArrayList<LikedUser> getLikedUsers() {
        return likedUsers;
    }
    public void setLikedUsers(ArrayList<LikedUser> likedUsers) {
        this.likedUsers = likedUsers;
    }
    public void addLikedUser(LikedUser likedUser){
        if(this.likedUsers == null)
            this.likedUsers = new ArrayList<>();

        this.likedUsers.add(likedUser);
    }

    public ArrayList<LikedUser> getUnlikedUsers() {
        return unlikedUsers;
    }
    public void setUnlikedUsers(ArrayList<LikedUser> unlikedUsers) {
        this.unlikedUsers = unlikedUsers;
    }
    public void addUnlikedUser(LikedUser unlikedUser){
        if(this.unlikedUsers == null)
            this.unlikedUsers = new ArrayList<>();

        this.unlikedUsers.add(unlikedUser);
    }

    public String getReduced_price() {
        return reduced_price;
    }
    public void setReduced_price(String reduced_price) {
        this.reduced_price = reduced_price;
    }

    public ArrayList<String> getImages() {
        return images;
    }
    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public boolean isExclusive() {
        return isExclusive;
    }
    public void setExclusive(boolean exclusive) {
        isExclusive = exclusive;
    }

    public String getSub_category() {
        return sub_category;
    }
    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public boolean isSeen() {
        return isSeen;
    }
    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
