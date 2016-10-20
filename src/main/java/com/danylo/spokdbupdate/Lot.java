package com.danylo.spokdbupdate;

import java.util.List;

public class Lot {
    private String name;
    private String description;
    private String category;
    private List<String> images;
    private String currency;
    private float price;
    private float quickBuyPrice;
    private int step;
    private String startDate;
    private String endDate;

    public Lot(String name, String description, String category, List<String> images, String currency, float price, float quickBuyPrice, int step, String startDate, String endDate) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.images = images;
        this.currency = currency;
        this.price = price;
        this.quickBuyPrice = quickBuyPrice;
        this.step = step;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getImages() {
        return images;
    }

    public String getCurrency() {
        return currency;
    }

    public float getPrice() {
        return price;
    }

    public float getQuickBuyPrice() {
        return quickBuyPrice;
    }

    public int getStep() {
        return step;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "Lot{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", images=" + images +
                ", currency='" + currency + '\'' +
                ", price=" + price +
                ", quickBuyPrice=" + quickBuyPrice +
                ", step=" + step +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
