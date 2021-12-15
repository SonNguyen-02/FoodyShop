package com.example.foodyshop.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Filter {

    public enum SORT {ASC, NOR, DESC}

    private final ArrayList<String> itemsPriceRange;
    private Enum<SORT> sortEnum;
    private int index;
    private int minPrice;
    private int maxPrice;

    public Filter() {
        clear();
        itemsPriceRange = new ArrayList<>(Arrays.asList("Tất cả", "0 - 50.000", "50.000 - 100.000", "100.000 - 200.000", "200.000 - 500.000", "500.000 +"));
    }

    public void setPriceRange(int index) {
        if (index < 0 || index >= itemsPriceRange.size()) {
            return;
        }
        this.index = index;
        if (index == 0) {
            minPrice = 0;
            maxPrice = Integer.MAX_VALUE;
            return;
        }
        if (index == itemsPriceRange.size() - 1) {
            minPrice = Integer.parseInt(itemsPriceRange.get(index).replaceAll("\\D", ""));
            maxPrice = Integer.MAX_VALUE;
            return;
        }
        String[] arr = itemsPriceRange.get(index).split("-");
        minPrice = Integer.parseInt(arr[0].replaceAll("\\D", ""));
        maxPrice = Integer.parseInt(arr[1].replaceAll("\\D", ""));
    }

    public String getOrderPrice() {
        return sortEnum == SORT.ASC ? "asc" : sortEnum == SORT.DESC ? "desc" : "nor";
    }

    public ArrayList<String> getItemsPriceRange() {
        return itemsPriceRange;
    }

    public Enum<SORT> getSortEnum() {
        return sortEnum;
    }

    public void setSortEnum(Enum<SORT> sortEnum) {
        this.sortEnum = sortEnum;
    }

    public int getIndex() {
        return index;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void clear() {
        sortEnum = SORT.NOR;
        index = minPrice = 0;
        maxPrice = Integer.MAX_VALUE;
    }

}