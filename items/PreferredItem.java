package com.eitan.shopik.items;

import android.os.Build;

import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;

import com.eitan.shopik.Macros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
@Keep
public class PreferredItem implements Serializable {

    private Map<String, Long> preferred;
    private ArrayList<String> fields_list;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public PreferredItem(Map<String, Long> preferred){
        // set of item strings from items description(name)..
     if(preferred == null)
         return;

     this.preferred = preferred;
     fields_list = getAttributeSortedByPreferences2();
   }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<String> getAttributeSortedByPreferences2(){

        ArrayList<String> sorted_fields = new ArrayList<>(preferred.keySet());

        Comparator<String> c = (o1, o2) -> {
            int res = 0;
            if(preferred!=null) {
                if (preferred.containsKey(o1) && preferred.containsKey(o2)) {
                    Long l1 = preferred.get(o1);
                    Long l2 = preferred.get(o2);

                    if(l1!=null && l2!=null)
                        res = Long.compare(l2, l1);
                }
            }
            return res;
        };

        Set<String> set = preferred.keySet();
        ArrayList<String> sorted_prices = new ArrayList<>();
        for (String field: set) {
            if(Arrays.asList(Macros.Arrays.PRICES).contains(field)) {
                sorted_prices.add(field);
                sorted_fields.remove(field);
            }
        }
        sorted_prices.sort(c);

        if(!sorted_prices.isEmpty())
            sorted_fields.add(sorted_prices.get(0));
        sorted_fields.sort(c);
        return sorted_fields;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int calculateMatchingPercentage(ShoppingItem shoppingItem){

       if( shoppingItem == null ) return 0;

       float result = 0;
       int attr = 0;

       for(String field : shoppingItem.getName()){
           if(fields_list.contains(field)) {
               result += (getAttributeEvaluation(field));
               ++attr ;
           }
       }
       return (int) ((result * 100) / (attr == 0 ? 1 : attr));
   }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private float getAttributeEvaluation(String wanted_field) {

       int i = fields_list.indexOf(wanted_field);
       if( i == -1 ) return 0;

       int size = fields_list.size();
       if(size == 1 || i == 0) return (float) 1;

       Object o = preferred.get(wanted_field);
       for( int j = 0 ; j < i ; ++j ) {
           Object o2 = preferred.get(fields_list.get(j));
           if (o == o2)
              return ((float)size - j)/size;
       }
       return ((float)size - i)/size;
    }
}
