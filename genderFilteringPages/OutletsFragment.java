package com.eitan.shopik.genderFilteringPages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.eitan.shopik.CustomItemAnimator;
import com.eitan.shopik.R;
import com.eitan.shopik.adapters.RecyclerGridAdapter;
import com.eitan.shopik.viewModels.GenderModel;
import com.eitan.shopik.viewModels.OutletsModel;

public class OutletsFragment extends Fragment {

    private RecyclerView recyclerView;
    private String gender;
    private GenderModel model;
    private OutletsModel outletsModel;
    private TextView header;
    private TextView count,total;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerGridAdapter recyclerGridAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(GenderModel.class);
        outletsModel = new ViewModelProvider(requireActivity()).get(OutletsModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_e3, container,false);

        header = view.findViewById(R.id.best_sellers2);
        count = view.findViewById(R.id.items_count);
        total = view.findViewById(R.id.items_total);
        recyclerView = view.findViewById(R.id.grid_view);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerGridAdapter = new RecyclerGridAdapter(outletsModel.getOutlets().getValue(),"outlets");
        recyclerView.setItemAnimator(new CustomItemAnimator());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gender = model.getGender().getValue();

        String header_text = "Save on Outlet";
        header.setText(header_text);

        model.getGender().observe(getViewLifecycleOwner(), s -> {
            if(!gender.equals(s)) {
                gender = s;
                outletsModel.clearAllOutlets();
                recyclerGridAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(recyclerGridAdapter);

        outletsModel.getOutlets().observe(requireActivity(), shoppingItems -> {
            recyclerGridAdapter.setAllItems(shoppingItems);
            recyclerGridAdapter.notifyDataSetChanged();
        });
        outletsModel.getTotalItems().observe(requireActivity(), integer -> {
            String text = "/ " + integer;
            total.setText(text);
        });
        outletsModel.getCurrentItem().observe(requireActivity(), integer ->{
            count.setText(String.valueOf(integer));
            recyclerGridAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        model.getGender().removeObservers(getViewLifecycleOwner());
        outletsModel.getOutlets().removeObservers(getViewLifecycleOwner());
        outletsModel.getTotalItems().removeObservers(getViewLifecycleOwner());
        outletsModel.getCurrentItem().removeObservers(getViewLifecycleOwner());
    }
    
}