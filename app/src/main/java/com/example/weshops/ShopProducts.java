package com.example.weshops;

import java.util.Comparator;

public class ShopProducts {
    private final int ProdImage;
    private final String ProdName;
    private final String ProdDates;
    private final String ProdPrice;
    private final String Description;

    public ShopProducts(int prodImage, String name, String description,String price,String date) {
        //, int price, int date
        ProdImage = prodImage;
        ProdName = name;
        Description = description;
        ProdPrice = price;
        ProdDates = date;
    }

    public int getProdImage() {
        return ProdImage;
    }
    public String getProdDate() { return ProdDates;  }
    public String getProdName() {
        return ProdName;
    }
    public String getPrice() {
        return ProdPrice;
    }
    public String getdescription() {
        return Description;
    }
    
    public  static Comparator<ShopProducts> ProductCompareAZ=new Comparator<ShopProducts>() {
        @Override
        public int compare(ShopProducts p1, ShopProducts p2) {
            return p1.ProdName.compareTo(p2.ProdName);
        }
    };
    public  static Comparator<ShopProducts> ProductCompareZA=new Comparator<ShopProducts>() {
        @Override
        public int compare(ShopProducts p1, ShopProducts p2) {
            return p2.ProdName.compareTo(p1.ProdName);
        }
    };
    public  static Comparator<ShopProducts> Productpriceasc=new Comparator<ShopProducts>() {
        @Override
        public int compare(ShopProducts p1, ShopProducts p2) {
            return p2.ProdPrice.compareTo(p1.ProdPrice);
        }
    };
    public  static Comparator<ShopProducts> Productpricedesc=new Comparator<ShopProducts>() {
        @Override
        public int compare(ShopProducts p1, ShopProducts p2) {
            return p2.ProdPrice.compareTo(p1.ProdPrice);
        }
    };
    public  static Comparator<ShopProducts> Productlatest=new Comparator<ShopProducts>() {
        @Override
        public int compare(ShopProducts p1, ShopProducts p2) {
            return p2.ProdDates.compareTo(p1.ProdDates);
        }
    };
}
