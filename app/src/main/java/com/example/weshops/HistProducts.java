package com.example.weshops;

import java.util.Comparator;

public class HistProducts {
    private final int histImage;
    private final String HistName;
    private final String HistDates;
    private final String HistPrice;
    private final String Description;

    public HistProducts(int HistImage, String name, String description, String price, String date) {
        //, int price, int date
        histImage = HistImage;
        HistName = name;
        Description = description;
        HistPrice = price;
        HistDates = date;
    }

    public int getHistImage() {
        return histImage;
    }
    public String getHistDate() { return HistDates;  }
    public String getHistName() {
        return HistName;
    }
    public String getPrice() {
        return HistPrice;
    }
    public String getdescription() {
        return Description;
    }


}
