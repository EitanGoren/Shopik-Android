package com.eitan.shopik.customerFragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.eitan.shopik.CustomItemAnimator;
import com.eitan.shopik.Macros;
import com.eitan.shopik.R;
import com.eitan.shopik.ShopikApplicationActivity;
import com.eitan.shopik.adapters.RecyclerGridAdapter;
import com.eitan.shopik.items.ShoppingItem;
import com.eitan.shopik.viewModels.GenderModel;
import com.eitan.shopik.viewModels.MainModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class FavoritesFragment extends Fragment {

    private GenderModel genderModel;
    private MainModel mainModel;
    private FloatingActionButton scroll_Up;
    private FloatingActionButton scroll_Down;
    private RecyclerView.OnScrollListener onScrollListener;
    private RecyclerGridAdapter recyclerGridAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView header;
    private int items_num = 0;
    private CopyOnWriteArrayList<ShoppingItem> fav_list;
    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        mainModel = new ViewModelProvider(requireActivity()).get(MainModel.class);
        genderModel = new ViewModelProvider(requireActivity()).get(GenderModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        header = view.findViewById(R.id.header_text);
        mRecyclerView = view.findViewById(R.id.list_recycler_view);
        scroll_Up = view.findViewById(R.id.scroll_up);
        scroll_Down = view.findViewById(R.id.scroll_down);

        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //NOT MOVING
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    scroll_Down.setVisibility(View.VISIBLE);
                    scroll_Up.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeIn).playOn(scroll_Down);
                    YoYo.with(Techniques.FadeIn).playOn(scroll_Up);
                }
                //MOVING
                else {
                    YoYo.with(Techniques.FadeOut).playOn(scroll_Down);
                    YoYo.with(Techniques.FadeOut).playOn(scroll_Up);
                    scroll_Down.setVisibility(View.GONE);
                    scroll_Up.setVisibility(View.GONE);
                }
            }
        };
        scroll_Up.setOnClickListener(v -> {
            //scroll down
            if(items_num > 100)
                mLayoutManager.scrollToPosition(recyclerGridAdapter.getItemCount() - 1);
            else
                mLayoutManager.smoothScrollToPosition(mRecyclerView,null,recyclerGridAdapter.getItemCount() - 1);
        });
        scroll_Down.setOnClickListener(v -> {
            //scroll up
            if(items_num > 100)
                mLayoutManager.scrollToPosition(0);
            else
                mLayoutManager.smoothScrollToPosition(mRecyclerView,null,0);
        });

        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(0);
        mRecyclerView.addItemDecoration(verticalSpaceItemDecoration);
        mRecyclerView.setItemAnimator(new CustomItemAnimator());

        fav_list = new CopyOnWriteArrayList<>();
        recyclerGridAdapter = new RecyclerGridAdapter(fav_list,"favorites");

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                closeKeyboard();
                recyclerGridAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerGridAdapter.getFilter().filter(newText);
                return true;
            }
        };
        mainModel.getFavorite().observe(requireActivity(), shoppingItems -> {
            fav_list.clear();
            int count_ads = 0;
            for (ShoppingItem shoppingItem : shoppingItems) {
                fav_list.add(shoppingItem);
                if(( fav_list.size() % Macros.FAV_TO_AD == 0 ) && fav_list.size() > 0 ) {
                    ShoppingItem shoppingItemAd = (ShoppingItem) ShopikApplicationActivity.getNextAd();
                    if(shoppingItemAd != null) {
                        count_ads++;
                        fav_list.add(shoppingItemAd);
                    }
                }
            }
            String text;
            if(shoppingItems.size() > 0) {
                String cat = shoppingItems.get(0).getType();
                String sub_cat = shoppingItems.get(0).getSub_category();
                text = cat.toUpperCase() + " | " + sub_cat.toUpperCase() + " | " + (fav_list.size() - count_ads) + " ITEMS";
            }
            else
                text = "NO FAVORITES YET";

            items_num = fav_list.size();
            header.setText(text);
            recyclerGridAdapter.setAllItems(fav_list);
            recyclerGridAdapter.notifyDataSetChanged();
        });
        mRecyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        genderModel.getType().removeObservers(getViewLifecycleOwner());
        genderModel.getSub_category().removeObservers(getViewLifecycleOwner());
        mRecyclerView = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init(){

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireActivity(),
                DividerItemDecoration.VERTICAL);

        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.
                getDrawable(requireContext(),R.drawable.recycler_divider)));

        mLayoutManager = new LinearLayoutManager(requireActivity(),
                LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(recyclerGridAdapter);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setItemAnimator(new CustomItemAnimator());
    }

    public static class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                                   @NonNull RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
            outRect.top = verticalSpaceHeight;
        }
    }

    private void closeKeyboard(){
        View view = requireActivity().getCurrentFocus();
        if( view != null ){
            InputMethodManager imm = (InputMethodManager) requireActivity().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        // Retrieve the SearchView and plug it into SearchManager
        MenuItem search = menu.findItem(R.id.nav_search);
        searchView = (SearchView) search.getActionView();

        String queryHint = "What's On Your Mind?";
        searchView.setQueryHint(queryHint);
        searchView.setOnClickListener(v -> searchView.onActionViewExpanded());
        searchView.setOnQueryTextListener(queryTextListener);

        super.onCreateOptionsMenu(menu,inflater);
    }
}