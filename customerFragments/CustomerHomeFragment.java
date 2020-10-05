package com.eitan.shopik.customerFragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eitan.shopik.LikedUser;
import com.eitan.shopik.Macros;
import com.eitan.shopik.R;
import com.eitan.shopik.ShopikApplicationActivity;
import com.eitan.shopik.adapters.CardsAdapter;
import com.eitan.shopik.items.ShoppingItem;
import com.eitan.shopik.viewModels.GenderModel;
import com.eitan.shopik.viewModels.MainModel;
import com.eitan.shopik.viewModels.SwipesModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class CustomerHomeFragment extends Fragment {

    private CardsAdapter arrayAdapter;
    private String item_type;
    private String item_gender;
    private String item_sub_category;
    private SwipesModel swipesModel;
    private Dialog dialog;
    private static final int DELAY_MILLIS = 2000;
    private SwipeFlingAdapterView flingContainer;
    private MainModel mainModel;
    private boolean isSwiped;
    private TextView percentage,total;
    private Observer<CopyOnWriteArrayList<ShoppingItem>> items_observer;
    private Observer<Pair<Integer,Integer>> current_items_observer;
    private final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private SwipeFlingAdapterView.onFlingListener onFlingListener;
    private Observer<Integer> total_items_observer;
    private int total_items_num = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainModel = new ViewModelProvider(requireActivity()).get(MainModel.class);
        GenderModel genderModel = new ViewModelProvider(requireActivity()).get(GenderModel.class);
        item_gender = genderModel.getGender().getValue();
        item_type = genderModel.getType().getValue();
        item_sub_category = genderModel.getSub_category().getValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_home, container,false);

        flingContainer = view.findViewById(R.id.frame);

        swipesModel = new ViewModelProvider(requireActivity()).get(SwipesModel.class);
        arrayAdapter = new CardsAdapter(requireActivity(), R.layout.swipe_item,
                swipesModel.getItems().getValue());

        onFlingListener = new SwipeFlingAdapterView.onFlingListener() {

            @Override
            public void removeFirstObjectInAdapter() {
                isSwiped = true;
                arrayAdapter.remove(arrayAdapter.getItem(0));
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                onItemUnliked(dataObject);
            }

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onRightCardExit(final Object dataObject) {
                onItemLiked(dataObject);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if (itemsInAdapter == 0 && isSwiped) {
                    isSwiped = false;
                    updateCurrentPage();
                }
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                if (view != null) {
                    view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                    view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                }
            }
        };
        flingContainer.setFlingListener(onFlingListener);
        arrayAdapter.setFlingContainer(flingContainer);
        flingContainer.setAdapter(arrayAdapter);

        percentage = view.findViewById(R.id.percentage);
        total = view.findViewById(R.id.total);
        percentage.setVisibility(View.GONE);

        items_observer = shoppingItems -> {

            swipesModel.clearAllItems();
            long size = mainModel.getCurrent_page().getValue() == null ? 1 : mainModel.getCurrent_page().getValue();
            for( ShoppingItem shoppingItem : shoppingItems ) {
                if(shoppingItem.getPage_num() == size && !shoppingItem.isSeen()) {
                    swipesModel.addToItems(shoppingItem);
                    arrayAdapter.notifyDataSetChanged();
                }
                if ((Objects.requireNonNull(swipesModel.getItems().getValue()).size() % Macros.SWIPES_TO_AD == 0)
                        && swipesModel.getItems().getValue().size() > 0) {
                    ShoppingItem shoppingItemAd = (ShoppingItem) ShopikApplicationActivity.getNextAd();
                    if (shoppingItemAd != null) {
                        ShoppingItem adItem = new ShoppingItem();
                        adItem.setNativeAd(shoppingItemAd.getNativeAd());
                        adItem.setAd(true);
                        swipesModel.addToItems(shoppingItemAd);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
            flingContainer.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
        };

        current_items_observer = pair -> {
            percentage.setVisibility(View.VISIBLE);
            String text = pair.first + " /";
            percentage.setText(text);

            if( pair.first > 1 && pair.first.equals(total_items_num) || pair.first > total_items_num ){
                percentage.setVisibility(View.GONE);
                total.setVisibility(View.GONE);
            }
        };

       total_items_observer = integer -> {
           total.setVisibility(View.VISIBLE);
           total.setText(String.valueOf(integer));
           total_items_num = integer;
       };

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isSwiped = false;
        mainModel.getTotalItems().observe(requireActivity(),total_items_observer);
        mainModel.getAll_items().observe(requireActivity(), items_observer);
        mainModel.getCurrentItem().observe(requireActivity(), current_items_observer);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainModel.getAll_items().removeObserver(items_observer);
        mainModel.getCurrentItem().removeObserver(current_items_observer);
        mainModel.getTotalItems().removeObserver(total_items_observer);
        flingContainer = null;
        onFlingListener = null;
        items_observer = null;
        total_items_observer = null;
        total_items_num = 0;
    }

    private void updateBadge() {

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.bottom_favorites);

        if (badgeDrawable.hasNumber()) {
            int num = badgeDrawable.getNumber();
            badgeDrawable.setNumber(num + 1);
        }
        else
            badgeDrawable.setNumber(1);

        badgeDrawable.setVisible(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void onItemLiked(Object dataObject) {

        final ShoppingItem shoppingItem = (ShoppingItem) dataObject;
        if (!shoppingItem.isAd()) {
            String item_id = shoppingItem.getId();
            String link = shoppingItem.getSite_link();
            String imageUrl = shoppingItem.getImages().get(0);

            if (item_id != null) {

                String seller = shoppingItem.getSeller();
                boolean isFavorite = arrayAdapter.isFavorite();

                shoppingItem.setFavorite(isFavorite);
                String action;

                if (isFavorite)
                    action = Macros.CustomerMacros.FAVOURITE;
                else
                    action = Macros.CustomerMacros.LIKED;

                mainModel.addSwipedItemId(item_id);

                updateLikes(shoppingItem);
                updateBadge();

                new updateDatabase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        item_type,item_gender,item_id,action,seller,link,imageUrl,item_sub_category);

                //TODO add parameters like color,brand,price,etc..

                for (String attr : shoppingItem.getName()) {
                    increasePreferredFieldByOneRTDB(attr, item_type, item_gender, item_sub_category);
                }
                if (mainModel.getPreferred().getValue() != null) {
                    int match_per = Objects.requireNonNull(mainModel.getPreferred().getValue()).
                            calculateMatchingPercentage(shoppingItem);
                    if(match_per >= 95){
                        dialog = new Dialog(requireContext());
                        showMatchDialog(imageUrl,match_per);
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void increasePreferredFieldByOneRTDB(String attribute, final String type,
                                                final String gender, final String sub_category) {

        if (attribute.equals("")
                || sub_category.contains(attribute)
                || Arrays.asList(Macros.Items.shit_words).contains(attribute)) {

            return;
        }

        final String attr = attribute.
                replace(".","").
                replace("$","").
                replace("#","").
                replace("[","").
                replace("]","").
                replace(":","");

        Map<String,Object> map = new HashMap<>();

        FirebaseDatabase.getInstance().getReference().
                child(Macros.CUSTOMERS).
                child(userId).
                child(gender).
                child(Macros.CustomerMacros.PREFERRED_ITEMS).
                child(type).
                child(sub_category).
                child(attr).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long num;
                        if(dataSnapshot.exists() && attr.equals(dataSnapshot.getKey())){
                            num = (long) dataSnapshot.getValue();
                            num++;
                            map.put(attr,num);

                            FirebaseDatabase.getInstance().getReference().
                                    child(Macros.CUSTOMERS).
                                    child(userId).
                                    child(gender).
                                    child(Macros.CustomerMacros.PREFERRED_ITEMS).
                                    child(type).
                                    child(sub_category).
                                    updateChildren(map);
                        }
                        else if(attr.equals(dataSnapshot.getKey())){
                            FirebaseDatabase.getInstance().getReference().
                                    child(Macros.CUSTOMERS).
                                    child(userId).
                                    child(gender).
                                    child(Macros.CustomerMacros.PREFERRED_ITEMS).
                                    child(type).
                                    child(sub_category).
                                    child(attr).
                                    setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
    }

    private void onItemUnliked(Object dataObject) {

        final ShoppingItem shoppingItem = (ShoppingItem) dataObject;
        if (!shoppingItem.isAd()) {

            String item_id = shoppingItem.getId();
            String seller = shoppingItem.getSeller();
            String link = shoppingItem.getSite_link();
            String imageUrl = shoppingItem.getImages().get(0);

            if (item_id != null) {

                mainModel.addSwipedItemId(item_id);

                new updateDatabase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        item_type,item_gender,item_id,Macros.Items.UNLIKED,seller,
                        link,imageUrl,item_sub_category);

            }
        }
    }

    private void updateLikes(ShoppingItem shoppingItem) {

        Bundle bundle = requireActivity().getIntent().getBundleExtra("bundle");
        assert bundle != null;
        String imageUrl = bundle.getString("imageUrl");
        String[] name = Objects.requireNonNull(Objects.requireNonNull(
                FirebaseAuth.getInstance().getCurrentUser()).getDisplayName()).split(" ");

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LikedUser likedUser = new LikedUser (imageUrl, name[0], name[1] );

        mainModel.setCustomers_info(user_id, likedUser);
        likedUser.setFavorite(shoppingItem.isFavorite());
        shoppingItem.addLikedUser(likedUser);
        shoppingItem.setLikes(shoppingItem.getLikedUsers().size());

        mainModel.addFavorite(shoppingItem);
    }

    private void showMatchDialog(String imageUrl, int match) {

        dialog.setContentView(R.layout.favorite_dialog_layout);

        TextView header = dialog.findViewById(R.id.header);
        TextView footer = dialog.findViewById(R.id.footer);
        ImageView fav_item = dialog.findViewById(R.id.fav_item);

        ImageView fav_ic = dialog.findViewById(R.id.fav_ic);
        fav_ic.setImageDrawable(ContextCompat.getDrawable(dialog.getContext(),R.drawable.ic_baseline_favorite));

        fav_ic.setColorFilter(Color.WHITE);
        String text = "Added to Favorites";
        footer.setText(text);
        footer.setTextSize(18);
        String word = (match >=95 && match<=99) ? "Great Match!" : "WOW! ";
        String header_text = word + System.lineSeparator() + match + " % MATCH";
        header.setText(header_text);
        Macros.Functions.GlidePicture(dialog.getContext(),imageUrl,fav_item);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.FavoriteSwipeAnimation;
        dialog.show();

        final Handler handler = new Handler();
        handler.postDelayed(() -> dialog.dismiss(), DELAY_MILLIS);
    }

    private void updateCurrentPage(){

        long new_page = mainModel.getCurrent_page().getValue() == null ? 1 : mainModel.getCurrent_page().getValue() + 1;

        FirebaseDatabase.getInstance().getReference().
                child(Macros.CUSTOMERS).
                child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).
                child(item_gender).
                child(Macros.PAGE_NUM).
                child(item_type).
                child(item_sub_category).
                setValue(new_page);
    }

    private static class updateDatabase extends AsyncTask<String,Void,Void> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... strings) {

            String type = strings[0];
            String gender = strings[1];
            String item_id = strings[2];
            String action = strings[3];
            String company = strings[4];
            String link = strings[5];
            String image = strings[6];
            String sub_category = strings[7];

            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            String temp_action = action;
            if(action.equals(Macros.CustomerMacros.FAVOURITE))
                temp_action = Macros.CustomerMacros.LIKED;

            FirebaseDatabase.getInstance().getReference().
                    child(Macros.ITEMS).
                    child(gender).
                    child(type).
                    child(company + "-" + item_id).
                    child(temp_action).
                    child(userId).
                    setValue(action);

            Map<String,Object> info = new HashMap<>();
            info.put("link", link);
            info.put("image", image);

            FirebaseDatabase.getInstance().getReference().
                    child(Macros.ITEMS).
                    child(gender).
                    child(type).
                    child(company + "-" + item_id).
                    child("Info").
                    setValue(info);

            Map<String,Object> map2 = new HashMap<>();
            map2.put(item_id, action);

            FirebaseDatabase.getInstance().getReference().
                    child(Macros.CUSTOMERS).
                    child(userId).
                    child(gender).
                    child(temp_action).
                    child(company).
                    child(type).
                    child(sub_category).
                    updateChildren(map2);

            return null;
        }
    }
}
