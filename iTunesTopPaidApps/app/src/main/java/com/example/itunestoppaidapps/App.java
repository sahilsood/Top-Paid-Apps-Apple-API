package com.example.itunestoppaidapps;

import java.util.Comparator;

public class App {
    String name;
    String price;
    String imageUrl;


    public App() {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "App{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public static Comparator<App> priceSortDesc = new Comparator<App>() {

        @Override
        public int compare(App t1, App t2) {
            String name1 = t1.name;
            String name2 = t2.name;

            /*For descending order*/
            return name2.compareTo(name1);

            /*For ascending order*/
            //year1-year2;
        }
    };

    public static Comparator<App> priceSortAesc = new Comparator<App>() {

        @Override
        public int compare(App t1, App t2) {
            String name1 = t1.name;
            String name2 = t2.name;

            /*For descending order*/
            return name1.compareTo(name2);

            /*For ascending order*/
            //year1-year2;
        }
    };


}
