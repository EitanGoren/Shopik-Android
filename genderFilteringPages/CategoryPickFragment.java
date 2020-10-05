package com.eitan.shopik.genderFilteringPages;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.eitan.shopik.Macros;
import com.eitan.shopik.R;
import com.eitan.shopik.adapters.ItemsCategoriesListAdapter;
import com.eitan.shopik.items.Category;
import com.eitan.shopik.items.RecyclerItem;
import com.eitan.shopik.viewModels.GenderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryPickFragment extends Fragment {

    private ItemsCategoriesListAdapter adapter;
    private ArrayList<Category> categories;
    private String gender, imageUrl;
    private ArrayList<RecyclerItem> sub_jackets,sub_shoes,sub_bags,sub_dress,sub_jeans,
                                   sub_jewellery,sub_shirts,sub_glasses,sub_watches,sub_swim,
                                   sub_accessories,sub_lingerie;
    private GenderModel model;
    private FirebaseUser user;
    private Category shoes;
    private Category jeans;
    private Category shirts;
    private Category watches;
    private Category sunglasses;
    private Category jackets;
    private Category jewellery;
    private Category swimwear;
    private Category lingerie;
    private Category accessories;
    private androidx.lifecycle.Observer<String> observer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(GenderModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_e2, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        if(model.getImageUrl().getValue() == null){
            assert user != null;
            FirebaseFirestore.getInstance().
                    collection(Macros.CUSTOMERS).
                    document(user.getUid()).get().
                    addOnSuccessListener(documentSnapshot ->
                            imageUrl = documentSnapshot.get("imageUrl") != null ?
                    Objects.requireNonNull(documentSnapshot.get("imageUrl")).toString() : null
            );
        }
        else
            imageUrl = model.getImageUrl().getValue();

        if(gender != null){
            setCategories();
        }

        adapter = new ItemsCategoriesListAdapter(categories);
        adapter.notifyDataSetChanged();

        ExpandableListView listContainer = requireView().findViewById(R.id.e2_list);

        listContainer.setAdapter(adapter);
        listContainer.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        observer = s -> {
            if(!gender.equals(s)) {
                gender = s;
                setCategories();
                adapter.collapseAll(listContainer);
            }
            adapter.notifyDataSetChanged();
        };
        model.getGender().observe(getViewLifecycleOwner(), observer);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        model.getGender().removeObserver(observer);
    }

    private void init() {

        user = FirebaseAuth.getInstance().getCurrentUser();
        categories = new ArrayList<>();

        gender = model.getGender().getValue();

        sub_bags = new ArrayList<>();
        sub_dress = new ArrayList<>();
        sub_jackets = new ArrayList<>();
        sub_shoes = new ArrayList<>();
        sub_jeans = new ArrayList<>();
        sub_jewellery = new ArrayList<>();
        sub_lingerie = new ArrayList<>();
        sub_accessories = new ArrayList<>();
    }

    private void setCategories(){

        clearCategories();
        initCategories();

        if(gender.equals(Macros.CustomerMacros.MEN)){

            addAccessoriesSubCategory("face masks", Macros.Items.MEN_FACE_MASK, Macros.CustomerMacros.MEN);
            addAccessoriesSubCategory("belts", Macros.Items.MEN_BELTS, Macros.CustomerMacros.MEN);
            addAccessoriesSubCategory("hats", Macros.Items.MEN_HATS, Macros.CustomerMacros.MEN);
            addAccessoriesSubCategory("ties", Macros.Items.MEN_TIES, Macros.CustomerMacros.MEN);
            addAccessoriesSubCategory("scarves", Macros.Items.MEN_SCARVES, Macros.CustomerMacros.MEN);

            addLingerieSubCategory("underwear", Macros.Items.MEN_UNDERWEAR, Macros.CustomerMacros.MEN);
            addLingerieSubCategory("socks", Macros.Items.MEN_SOCKS, Macros.CustomerMacros.MEN);

            addJacketsSubCategory("overcoat", Macros.Items.MEN_OVERCOAT_JACKETS_RES, Macros.CustomerMacros.MEN);
            addJacketsSubCategory("biker", Macros.Items.MEN_BIKER_JACKETS_RES, Macros.CustomerMacros.MEN);
            addJacketsSubCategory("leather", Macros.Items.MEN_LEATHER_JACKETS_RES, Macros.CustomerMacros.MEN);
            addJacketsSubCategory("denim", Macros.Items.MEN_DENIM_JACKETS_RES, Macros.CustomerMacros.MEN);
            addJacketsSubCategory("trench", Macros.Items.MEN_TRENCH_JACKETS_RES, Macros.CustomerMacros.MEN);
            addJacketsSubCategory("puffer", Macros.Items.MEN_PUFFER_JACKETS_RES, Macros.CustomerMacros.MEN);

            addJeansSubCategory("slim", Macros.Items.MEN_SLIM_JEANS_RES, Macros.CustomerMacros.MEN);
            addJeansSubCategory("straight", Macros.Items.MEN_STRAIGHT_JEANS_RES, Macros.CustomerMacros.MEN);
            addJeansSubCategory("skinny", Macros.Items.MEN_SKINNY_JEANS_RES, Macros.CustomerMacros.MEN);

            addJewellerySubCategory("ring", Macros.Items.MEN_RING_RES, Macros.CustomerMacros.MEN);
            addJewellerySubCategory("bracelet", Macros.Items.MEN_BRACELET_RES, Macros.CustomerMacros.MEN);
            addJewellerySubCategory("necklace", Macros.Items.MEN_NECKLACE_RES, Macros.CustomerMacros.MEN);
            addJewellerySubCategory("earrings", Macros.Items.MEN_EARRING_RES, Macros.CustomerMacros.MEN);

            addShoesSubCategory("loafers",Macros.Items.MEN_LOAFERS_RES,Macros.CustomerMacros.MEN);
            addShoesSubCategory("boots",Macros.Items.MEN_BOOTS_RES,Macros.CustomerMacros.MEN);
            addShoesSubCategory("elegant",Macros.Items.MEN_PUFFER_JACKETS_RES,Macros.CustomerMacros.MEN);
            addShoesSubCategory("trainers",Macros.Items.MEN_TRAINERS_RES,Macros.CustomerMacros.MEN);
            addShoesSubCategory("sandals",Macros.Items.MEN_SANDALS_RES,Macros.CustomerMacros.MEN);
            addShoesSubCategory("sliders",Macros.Items.MEN_SLIDERS_RES,Macros.CustomerMacros.MEN);

            addShirtsSubCategory("denim", Macros.Items.MEN_DENIM_RES,Macros.CustomerMacros.MEN);
            addShirtsSubCategory("check", Macros.Items.MEN_CHECK_RES,Macros.CustomerMacros.MEN);
            addShirtsSubCategory("t-shirt", Macros.Items.MEN_TSHIRT_RES,Macros.CustomerMacros.MEN);
            addShirtsSubCategory("oxford", Macros.Items.MEN_OXFORD_RES,Macros.CustomerMacros.MEN);
            addShirtsSubCategory("slim-fit", Macros.Items.MEN_SLIM_RES, Macros.CustomerMacros.MEN);
            addShirtsSubCategory("tank", Macros.Items.MEN_TANK_RES, Macros.CustomerMacros.MEN);

            addWatchesSubCategory("smart",Macros.Items.MEN_SMART_WATCH_RES,Macros.CustomerMacros.MEN);
            addWatchesSubCategory("digital",Macros.Items.MEN_DIGITAL_RES,Macros.CustomerMacros.MEN);
            addWatchesSubCategory("stylish",Macros.Items.MEN_WATCHES_RES,Macros.CustomerMacros.MEN);

            addSwimwearSubCategory("shorts", Macros.Items.MEN_SHORTS_SWIM_RES,Macros.CustomerMacros.MEN);
            addSwimwearSubCategory("co-ord", Macros.Items.MEN_CO_ORD_SWIM_RES,Macros.CustomerMacros.MEN);
            addSwimwearSubCategory("oversized", Macros.Items.MEN_PLUS_SWIM_RES,Macros.CustomerMacros.MEN);

            addGlassesSubCategory("round", Macros.Items.MEN_ROUND_GLASSES_RES, Macros.CustomerMacros.MEN);
            addGlassesSubCategory("square", Macros.Items.MEN_SQUARE_GLASSES_RES, Macros.CustomerMacros.MEN);
        }
        else {

            addAccessoriesSubCategory("face masks", Macros.Items.WOMEN_FACE_MASK, Macros.CustomerMacros.WOMEN);
            addAccessoriesSubCategory("belts", Macros.Items.WOMEN_BELTS, Macros.CustomerMacros.WOMEN);
            addAccessoriesSubCategory("scarves", Macros.Items.WOMEN_SCARVES, Macros.CustomerMacros.WOMEN);
            addAccessoriesSubCategory("hats", Macros.Items.WOMEN_HATS, Macros.CustomerMacros.WOMEN);
            addAccessoriesSubCategory("sport", Macros.Items.WOMEN_SPORT, Macros.CustomerMacros.WOMEN);

            addLingerieSubCategory("underwear", Macros.Items.WOMEN_UNDERWEAR, Macros.CustomerMacros.WOMEN);
            addLingerieSubCategory("socks", Macros.Items.WOMEN_SOCKS, Macros.CustomerMacros.WOMEN);
            addLingerieSubCategory("bras", Macros.Items.WOMEN_BRAS, Macros.CustomerMacros.WOMEN);
            addLingerieSubCategory("shapewear", Macros.Items.WOMEN_SHAPEWEAR, Macros.CustomerMacros.WOMEN);
            addLingerieSubCategory("pyjamas", Macros.Items.WOMEN_PYJAMAS, Macros.CustomerMacros.WOMEN);
            addLingerieSubCategory("bra & panty", Macros.Items.WOMEN_PYJAMAS, Macros.CustomerMacros.WOMEN);

            addBagsSubCategory("bum",Macros.Items.BUM_RES);
            addBagsSubCategory("clutch",Macros.Items.CLUTCH_RES);
            addBagsSubCategory("backpack",Macros.Items.BACKPACK_RES);
            addBagsSubCategory("cross body",Macros.Items.CROSS_RES);
            addBagsSubCategory("tote",Macros.Items.TOTE_RES);
            addBagsSubCategory("wallets",Macros.Items.WALLETS_RES);

            addDressSubCategory("jumper",Macros.Items.JUMPER_DRESS_RES);
            addDressSubCategory("midi",Macros.Items.MIDI_DRESS_RES);
            addDressSubCategory("wedding",Macros.Items.WEDDING_DRESS_RES);
            addDressSubCategory("party",Macros.Items.PARTY_DRESS_RES);
            addDressSubCategory("maxi",Macros.Items.MAXI_DRESS_RES);
            addDressSubCategory("evening",Macros.Items.EVENING_DRESS_RES);
            addDressSubCategory("mini",Macros.Items.MINI_DRESSES_RES);

            addJacketsSubCategory("leather",Macros.Items.WOMEN_LEATHER_JACKETS_RES,Macros.CustomerMacros.WOMEN);
            addJacketsSubCategory("coat",Macros.Items.WOMEN_COAT_JACKETS_RES,Macros.CustomerMacros.WOMEN);
            addJacketsSubCategory("teddy",Macros.Items.WOMEN_TEDDY_JACKETS_RES,Macros.CustomerMacros.WOMEN);
            addJacketsSubCategory("puffer",Macros.Items.WOMEN_PUFFER_JACKETS_RES,Macros.CustomerMacros.WOMEN);
            addJacketsSubCategory("jacket",Macros.Items.WOMEN_JACKET_JACKETS_RES,Macros.CustomerMacros.WOMEN);

            addShoesSubCategory("sandals",Macros.Items.WOMEN_SANDALS_RES,Macros.CustomerMacros.WOMEN);
            addShoesSubCategory("boots",Macros.Items.WOMEN_BOOTS_RES,Macros.CustomerMacros.WOMEN);
            addShoesSubCategory("heels",Macros.Items.WOMEN_HEELS_RES,Macros.CustomerMacros.WOMEN);
            addShoesSubCategory("sliders",Macros.Items.WOMEN_SLIDERS_RES,Macros.CustomerMacros.WOMEN);
            addShoesSubCategory("slippers",Macros.Items.WOMEN_SLIPPERS_RES,Macros.CustomerMacros.WOMEN);
            addShoesSubCategory("flat shoes",Macros.Items.FLAT_SHOES_RES,Macros.CustomerMacros.WOMEN);
            addShoesSubCategory("trainers",Macros.Items.WOMEN_TRAINERS_RES,Macros.CustomerMacros.WOMEN);

            addJeansSubCategory("slim", Macros.Items.WOMEN_SLIM_JEANS_RES, Macros.CustomerMacros.WOMEN);
            addJeansSubCategory("straight", Macros.Items.WOMEN_STRAIGHT_JEANS_RES, Macros.CustomerMacros.WOMEN);
            addJeansSubCategory("skinny", Macros.Items.WOMEN_SKINNY_JEANS_RES, Macros.CustomerMacros.WOMEN);
            addJeansSubCategory("ripped", Macros.Items.WOMEN_RIPPED_JEANS_RES, Macros.CustomerMacros.WOMEN);
            addJeansSubCategory("jeggings",Macros.Items.WOMEN_JEGGINGS, Macros.CustomerMacros.WOMEN);
            addJeansSubCategory("high-waist",Macros.Items.WOMEN_HIGH_WAIST, Macros.CustomerMacros.WOMEN);

            addJewellerySubCategory("ring", Macros.Items.WOMEN_RING_RES, Macros.CustomerMacros.WOMEN);
            addJewellerySubCategory("bracelet", Macros.Items.WOMEN_BRACELET_RES, Macros.CustomerMacros.WOMEN);
            addJewellerySubCategory("necklace", Macros.Items.WOMEN_NECKLACE_RES, Macros.CustomerMacros.WOMEN);
            addJewellerySubCategory("earrings", Macros.Items.WOMEN_EARRING_RES, Macros.CustomerMacros.WOMEN);
            addJewellerySubCategory("anklet", Macros.Items.WOMEN_ANKLET_RES, Macros.CustomerMacros.WOMEN);

            addGlassesSubCategory("round", Macros.Items.WOMEN_ROUND_GLASSES_RES, Macros.CustomerMacros.WOMEN);
            addGlassesSubCategory("square", Macros.Items.WOMEN_SQUARE_GLASSES_RES, Macros.CustomerMacros.WOMEN);
            addGlassesSubCategory("cat-eye", Macros.Items.WOMEN_CATEYE_GLASSES_RES, Macros.CustomerMacros.WOMEN);
            addGlassesSubCategory("aviator", Macros.Items.WOMEN_AVIATOR_GLASSES_RES, Macros.CustomerMacros.WOMEN);

            addShirtsSubCategory("hoodies", Macros.Items.WOMEN_HOODIES_RES,Macros.CustomerMacros.WOMEN);
            addShirtsSubCategory("sweatshirts", Macros.Items.WOMEN_SWEATSHIRTS_RES,Macros.CustomerMacros.WOMEN);
            addShirtsSubCategory("t-shirts", Macros.Items.WOMEN_TSHIRT_RES,Macros.CustomerMacros.WOMEN);
            addShirtsSubCategory("blouses", Macros.Items.WOMEN_BLOUSES_RES,Macros.CustomerMacros.WOMEN);
            addShirtsSubCategory("camis", Macros.Items.WOMEN_CAMIS_RES,Macros.CustomerMacros.WOMEN);
            addShirtsSubCategory("summer-top", Macros.Items.WOMEN_SUMMER_TOP_RES,Macros.CustomerMacros.WOMEN);
            addShirtsSubCategory("evening", Macros.Items.WOMEN_EVENING_RES,Macros.CustomerMacros.WOMEN);
            addShirtsSubCategory("tanks", Macros.Items.WOMEN_TANKS_RES,Macros.CustomerMacros.WOMEN);
            addShirtsSubCategory("leotards", Macros.Items.WOMEN_LEOTARDS_RES,Macros.CustomerMacros.WOMEN);

            addWatchesSubCategory("stylish",Macros.Items.WOMEN_WATCHES_RES,Macros.CustomerMacros.WOMEN);
            addWatchesSubCategory("smart",Macros.Items.WOMEN_SMART_RES,Macros.CustomerMacros.WOMEN);
            addWatchesSubCategory("digital",Macros.Items.WOMEN_DIGITAL_RES,Macros.CustomerMacros.WOMEN);

            addSwimwearSubCategory("bikini", Macros.Items.WOMEN_BIKINI_RES,Macros.CustomerMacros.WOMEN);
            addSwimwearSubCategory("one-piece", Macros.Items.WOMEN_ONE_PIECE_RES,Macros.CustomerMacros.WOMEN);
            addSwimwearSubCategory("swimwear", Macros.Items.WOMEN_SWIMMWEAR_RES,Macros.CustomerMacros.WOMEN);

            Category bags = new Category(Macros.BAG, gender, sub_bags);
            Category dresses = new Category(Macros.DRESS, gender, sub_dress);

            categories.add(bags);
            categories.add(dresses);
        }

        jackets = new Category(Macros.JACKETS,gender,sub_jackets);
        shoes = new Category(Macros.SHOES,gender,sub_shoes);
        jeans = new Category(Macros.JEANS,gender,sub_jeans);
        shirts = new Category(Macros.SHIRT,gender,sub_shirts);
        watches = new Category(Macros.WATCH,gender,sub_watches);
        sunglasses = new Category(Macros.SUNGLASSES,gender,sub_glasses);
        jewellery = new Category(Macros.JEWELLERY,gender,sub_jewellery);
        swimwear = new Category(Macros.SWIMWEAR,gender,sub_swim);
        lingerie = new Category(Macros.LINGERIE,gender,sub_lingerie);
        accessories = new Category(Macros.ACCESSORIES,gender,sub_accessories);

       fillCategories();

    }
    private void initCategories(){
        sub_shirts = new ArrayList<>();
        sub_swim = new ArrayList<>();
        sub_glasses = new ArrayList<>();
        sub_watches = new ArrayList<>();
        sub_jewellery = new ArrayList<>();
        sub_dress = new ArrayList<>();
        sub_shoes = new ArrayList<>();
        sub_bags = new ArrayList<>();
        sub_jeans = new ArrayList<>();
        sub_jackets = new ArrayList<>();
        sub_accessories = new ArrayList<>();
        sub_lingerie = new ArrayList<>();
    }
    private void fillCategories() {
        categories.add(shoes);
        categories.add(shirts);
        categories.add(jeans);
        categories.add(watches);
        categories.add(sunglasses);
        categories.add(jackets);
        categories.add(jewellery);
        categories.add(swimwear);
        categories.add(accessories);
        categories.add(lingerie);
    }
    private void clearCategories() {

        categories.clear();
        sub_jackets.clear();
        sub_bags.clear();
        sub_dress.clear();
        sub_shoes.clear();
        sub_jeans.clear();
        sub_jewellery.clear();
        sub_lingerie.clear();
        sub_accessories.clear();
    }

    private void addJacketsSubCategory(String name, String res_url, String _gender) {
        RecyclerItem new_item = new RecyclerItem(name,null, null);
        new_item.setImage_resource(res_url);
        new_item.setType(Macros.JACKETS);
        new_item.setItem_sub_category(name);
        new_item.setGender(_gender);

        String _name = String.valueOf(name.charAt(0)).toUpperCase();
        _name += name.substring(1);
        new_item.setText(_name);

        new_item.setUserImageUrl(imageUrl);
        sub_jackets.add(new_item);
    }
    private void addJeansSubCategory(String name,String res_url,String _gender) {
        RecyclerItem new_item = new RecyclerItem(name,null, null);
        new_item.setImage_resource(res_url);
        new_item.setType(Macros.JEANS);
        new_item.setItem_sub_category(name);
        new_item.setGender(_gender);

        String _name = String.valueOf(name.charAt(0)).toUpperCase();
        _name += name.substring(1);
        new_item.setText(_name);

        new_item.setUserImageUrl(imageUrl);
        sub_jeans.add(new_item);
    }
    private void addBagsSubCategory(String name,String res_url) {
        RecyclerItem new_item = new RecyclerItem(name,null, null);
        new_item.setImage_resource(res_url);
        new_item.setType(Macros.BAG);
        new_item.setItem_sub_category(name);
        new_item.setGender(Macros.CustomerMacros.WOMEN);

        String _name = String.valueOf(name.charAt(0)).toUpperCase();
        _name += name.substring(1);
        new_item.setText(_name);

        new_item.setUserImageUrl(imageUrl);
        sub_bags.add(new_item);
        //sub_bags.add(bags);
    }
    private void addShoesSubCategory(String name,String res_url,String _gender) {
        RecyclerItem new_item = new RecyclerItem(name,null, null);
        new_item.setImage_resource(res_url);
        new_item.setType(Macros.SHOES);
        new_item.setItem_sub_category(name);
        new_item.setGender(_gender);

        String _name = String.valueOf(name.charAt(0)).toUpperCase();
        _name += name.substring(1);
        new_item.setText(_name);

        new_item.setUserImageUrl(imageUrl);
        sub_shoes.add(new_item);
    }
    private void addDressSubCategory(String name,String res_url) {
        RecyclerItem new_item = new RecyclerItem(name,null, null);
        new_item.setImage_resource(res_url);
        new_item.setType(Macros.DRESS);
        new_item.setItem_sub_category(name);
        new_item.setGender(Macros.CustomerMacros.WOMEN);

        String _name = String.valueOf(name.charAt(0)).toUpperCase();
        _name += name.substring(1);
        new_item.setText(_name);

        new_item.setUserImageUrl(imageUrl);
        sub_dress.add(new_item);
    }
    private void addJewellerySubCategory(String name,String res_url,String _gender) {
        RecyclerItem new_item = new RecyclerItem(name,null, null);
        new_item.setImage_resource(res_url);
        new_item.setType(Macros.JEWELLERY);
        new_item.setItem_sub_category(name);
        new_item.setGender(_gender);

        String _name = String.valueOf(name.charAt(0)).toUpperCase();
        _name += name.substring(1);
        new_item.setText(_name);

        new_item.setUserImageUrl(imageUrl);
        sub_jewellery.add(new_item);
    }
    private void addWatchesSubCategory(String name,String res_url,String _gender) {
        RecyclerItem new_item = new RecyclerItem(name,null, null);
        new_item.setImage_resource(res_url);
        new_item.setType(Macros.WATCH);
        new_item.setItem_sub_category(name);
        new_item.setGender(_gender);

        String _name = String.valueOf(name.charAt(0)).toUpperCase();
        _name += name.substring(1);
        new_item.setText(_name);

        new_item.setUserImageUrl(imageUrl);
        sub_watches.add(new_item);
    }
    private void addGlassesSubCategory(String name, String res_url,String _gender) {
        RecyclerItem new_item = new RecyclerItem(name,null, null);
        new_item.setImage_resource(res_url);
        new_item.setType(Macros.SUNGLASSES);
        new_item.setItem_sub_category(name);
        new_item.setGender(_gender);

        String _name = String.valueOf(name.charAt(0)).toUpperCase();
        _name += name.substring(1);
        new_item.setText(_name);

        new_item.setUserImageUrl(imageUrl);
        sub_glasses.add(new_item);
    }
    private void addSwimwearSubCategory(String name,String res_url,String _gender) {
        RecyclerItem new_item = new RecyclerItem(name,null, null);
        new_item.setImage_resource(res_url);
        new_item.setType(Macros.SWIMWEAR);
        new_item.setItem_sub_category(name);
        new_item.setGender(_gender);

        String _name = String.valueOf(name.charAt(0)).toUpperCase();
        _name += name.substring(1);
        new_item.setText(_name);

        new_item.setUserImageUrl(imageUrl);
        sub_swim.add(new_item);
    }
    private void addShirtsSubCategory(String name,String res_url,String _gender) {
        RecyclerItem new_item = new RecyclerItem(name,null, null);
        new_item.setImage_resource(res_url);
        new_item.setType(Macros.SHIRT);
        new_item.setItem_sub_category(name);
        new_item.setGender(_gender);

        String _name = String.valueOf(name.charAt(0)).toUpperCase();
        _name += name.substring(1);
        new_item.setText(_name);

        new_item.setUserImageUrl(imageUrl);
        sub_shirts.add(new_item);
    }
    private void addLingerieSubCategory(String name,String res_url,String _gender) {
        RecyclerItem new_item = new RecyclerItem(name,null, null);
        new_item.setImage_resource(res_url);
        new_item.setType(Macros.LINGERIE);
        new_item.setItem_sub_category(name);
        new_item.setGender(_gender);

        String _name = String.valueOf(name.charAt(0)).toUpperCase();
        _name += name.substring(1);
        new_item.setText(_name);

        new_item.setUserImageUrl(imageUrl);
        sub_lingerie.add(new_item);
    }
    private void addAccessoriesSubCategory(String name,String res_url,String _gender) {
        RecyclerItem new_item = new RecyclerItem(name,null, null);
        new_item.setImage_resource(res_url);
        new_item.setType(Macros.ACCESSORIES);
        new_item.setItem_sub_category(name);
        new_item.setGender(_gender);

        String _name = String.valueOf(name.charAt(0)).toUpperCase();
        _name += name.substring(1);
        new_item.setText(_name);

        new_item.setUserImageUrl(imageUrl);
        sub_accessories.add(new_item);
    }
}
