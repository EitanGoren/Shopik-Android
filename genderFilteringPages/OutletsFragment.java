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

// --Commented out by Inspection START (16-Sep-20 15:50):
//    private static class E3GridAdapter extends ArrayAdapter<RecyclerItem> implements Serializable {
//
//        private final List<RecyclerItem> AllItemsList;
//        private final List<RecyclerItem> ItemsList;
//
//        public E3GridAdapter(@NonNull Context context, int resource, List<RecyclerItem> items){
//            super(context, resource, items);
//            this.ItemsList = items;
//            this.AllItemsList = new ArrayList<>();
//        }
//
//        @NonNull
//        @SuppressLint("SetTextI18n")
//        @Override
//        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//
//            final RecyclerItem item = getItem(position);
//
//            if(convertView == null) {
//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.e3_grid_item, parent, false);
//            }
//
//            ImageView imageView = convertView.findViewById(R.id.image_slider);
//            Button link = convertView.findViewById(R.id.store_link);
//            TextView price = convertView.findViewById(R.id.slider_brand);
//            TextView reduced_price = convertView.findViewById(R.id.reduced_price);
//            TextView sale = convertView.findViewById(R.id.sale);
//            TextView description = convertView.findViewById(R.id.description);
//
//            link.setOnClickListener(v -> {
//                assert item != null;
//                Macros.Functions.buy(getContext(), item.getLink());
//            });
//
//            assert item != null;
//            final ArrayList<String> imagesUrl = item.getImages();
//
//            if(item.getBrand() != null){
//                description.setVisibility(View.VISIBLE);
//                description.setText(item.getBrand());
//            }
//            else
//                description.setVisibility(View.GONE);
//
//            if(imagesUrl.size() > 0)
//                Macros.Functions.GlidePicture(getContext(),imagesUrl.get(0),imageView);
//
//            if(item.isSale())
//                sale.setVisibility(View.VISIBLE);
//            else
//                sale.setVisibility(View.GONE);
//
//            if(item.isOutlet() || item.isSale()){
//                Currency shekel = Currency.getInstance("ILS");
//                String currency_symbol = shekel.getSymbol();
//                Double old = Double.parseDouble(item.getReduced_price()) * Macros.POUND_TO_ILS;
//                String cur_price = new DecimalFormat("##.##").format(old) + currency_symbol;
//
//                reduced_price.setVisibility(View.VISIBLE);
//                reduced_price.setText(cur_price);
//                reduced_price.setTextColor(Color.RED);
//                reduced_price.setTextSize(16);
//
//                price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                price.setText(item.getPrice());
//                price.setTextSize(12);
//            }
//            else {
//                reduced_price.setVisibility(View.INVISIBLE);
//                price.setText(item.getPrice());
//                price.setTextSize(16);
//            }
//
//            imageView.setOnClickListener(v -> {
//                Intent intent = new Intent(getContext(), FullscreenImageActivity.class);
//                intent.putExtra("isFav", false);
//                intent.putExtra("brand", item.getSeller());
//                intent.putExtra("id", item.getId());
//                intent.putExtra("img1", item.getImages().get(0));
//                intent.putExtra("img2", item.getImages().get(1));
//                intent.putExtra("img3", item.getImages().get(2));
//                intent.putExtra("img4", item.getImages().get(3));
//                intent.putExtra("seller_logo", item.getSellerLogoUrl());
//                intent.putExtra("description", item.toString());
//                intent.putExtra("type", item.getType());
//
//                Macros.Functions.fullscreen(getContext(), intent, null);
//            });
//
//            return convertView;
//        }
//
//        @Override
//        public int getCount() {
//            return ItemsList.size();
//        }
//
//        @Nullable
//        @Override
//        public RecyclerItem getItem(int position) {
//            return ItemsList.get(position);
//        }
//
//        @NonNull
//        @Override
//        public Filter getFilter() {
//            return filter;
//        }
//
//        public void setAllItems(CopyOnWriteArrayList<RecyclerItem> allItems) {
//            this.AllItemsList.clear();
//            this.AllItemsList.addAll(allItems);
//        }
//
//        public void clearItems() {
//            this.AllItemsList.clear();
//            this.ItemsList.clear();
//        }
//
//        final Filter filter = new Filter() {
//            //runs in background thread
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//
//                Set<RecyclerItem> filteredList = new ArraySet<>();
//
//                if(constraint.toString().isEmpty()){
//                    filteredList.addAll(AllItemsList);
//                }
//                else {
//                    for(RecyclerItem item : AllItemsList){
//                        if(item.getDescription() != null) {
//                            String description = "";
//                            for(String word : item.getDescription()) {
//                                description = description.concat(word.toLowerCase());
//                            }
//                            if(description.contains(constraint.toString().toLowerCase())){
//                                filteredList.add(item);
//                            }
//                        }
//                    }
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = filteredList;
//                filterResults.count = filteredList.size();
//
//                return filterResults;
//            }
//
//            //runs in UI thread
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                if(results.values == null )
//                    return;
//                ItemsList.clear();
//                ItemsList.addAll((Collection<? extends RecyclerItem>) results.values);
//                notifyDataSetChanged();
//            }
//        };
//    }
// --Commented out by Inspection STOP (16-Sep-20 15:50)
}