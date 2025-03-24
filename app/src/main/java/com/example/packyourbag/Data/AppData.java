package com.example.packyourbag.Data;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.example.packyourbag.Database.RoomDB;
import com.example.packyourbag.Models.Items;
import com.example.packyourbag.constants.MyConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppData extends Application {

    RoomDB database;
    String category;
    Context context;

    public static final String LAST_VERSION = "LAST_VERSION";
    public static final int NEW_VERSION = 1;

    public AppData(RoomDB database) {
        this.database = database;
    }

    public AppData(RoomDB database, Context context) {
        this.database = database;
        this.context = context;
    }

    public List<Items> getBasicData(){
        category = "Basic Needs";
        List<Items> basicItem = new ArrayList<>();
        basicItem.add(new Items("Visa", category, false));
        basicItem.add(new Items("Passport", category, false));
        basicItem.add(new Items("Tickets", category, false));
        basicItem.add(new Items("Wallet", category, false));
        basicItem.add(new Items("Driving License", category, false));
        basicItem.add(new Items("Currency", category, false));
        basicItem.add(new Items("House Key", category, false));
        basicItem.add(new Items("Book", category, false));
        basicItem.add(new Items("Travel pillow", category, false));
        basicItem.add(new Items("Eye patch", category, false));
        basicItem.add(new Items("Umbrella", category, false));
        basicItem.add(new Items("Notebook", category, false));
        return basicItem;
    }

    public List<Items> getPersonalCareData(){
        String[] data = {"Tooth-brush", "Tooth-paste", "Flass", "Mouthwasher", "Facewash","Razor Blade",
        "Shaving Cream","Soap","Fiber","Lip balm","Hair Oil","Makeup Material","Sun-Cream", "Ear stick", "Moisturizer",
        "Nail Paint", "Makeup Remover"};
        return prepareItemList(MyConstant.PERSONAL_CARE_CAMEL_CASE, data);
    }

    public List<Items> getClothingData(){
        String[] data = {"Stockings", "Underwear", "Pajamas", "T-Shirts", "Casual Dress","Evening Dress",
                "Shirt","Coat","Vest","Jacket","Trouser","Jeans","Shoes", "Suit", "Saree",
                "Sneakers", "Hat"};
        return prepareItemList(MyConstant.CLOTHING_CAMEL_CASE, data);
    }

    public List<Items> getBabyNeedsData(){
        String[] data = {"Snap-suit", "Outfit", "Jumpsuit", "Babyhat", "Diapers","Baby Bottle",
                "Milk","Baby Laundary Detergent","Baby food spoon","Storage container","Baby Cotton","Feeding Cover","Wet Wipes", "Baby Nail Scissors", "HighChairs",
                "Baby Care Cover", "Body oil"};
        return prepareItemList(MyConstant.BABY_NEEDS_CAMEL_CASE, data);
    }

    public List<Items> getFoodData(){
        String[] data = {"Snacks", "Sandwich", "Juice", "Tea bags", "coffee","Namken", "Eggs","Chips","Baby food"};
        return prepareItemList(MyConstant.FOOD_CAMEL_CASE, data);
    }

    public List<Items> getBeachSuppliesData(){
        String[] data = {"Sea-glass", "sea bed", "suntan creams", "beach umbrella", "Swim suit","Beach bag",
                "Beach towel","beach slipper","batergroof clock"};
        return prepareItemList(MyConstant.BEACH_SUPPLIES_CAMEL_CASE, data);
    }

    public List<Items> getHealthData(){
        String[] data = {"Aspirin", "ice bag", "painkiller", "constipation relife", "First Aid kit","fever reducer",
                "condom"};
        return prepareItemList(MyConstant.HEALTH_CAMEL_CASE, data);
    }

    public List<Items> getTechnologyData(){
        String[] data = {"Mobile phone", "phone cover", "E-book Reader", "Camera", "Power Bank","Charger",
                "Data transfer cable","Mp3 player","Flash-light","DVD Player"};
        return prepareItemList(MyConstant.TECHNOLOGY_CAMEL_CASE, data);
    }

    public List<Items> getCarSuppliesData(){
        String[] data = {"Pump", "Car jack", "Spare car key", "Accident record set", "Auto refrigerator","Car cover",
                "Car charger"};
        return prepareItemList(MyConstant.CAR_SUPPLIES_CAMEL_CASE, data);
    }

    public List<Items> prepareItemList(String category, String[] data){
        List<String> list = Arrays.asList(data);
        List<Items> dataList = new ArrayList<>();
        dataList.clear();

        for(int i=0; i<list.size();i++){
            dataList.add(new Items(list.get(i), category, false));
        }
        return dataList;
    }

    public List<List<Items>> getAllData(){
        List<List<Items>> listOfAllItems = new ArrayList<>();
        listOfAllItems.clear();
        listOfAllItems.add(getBasicData());
        listOfAllItems.add(getClothingData());
        listOfAllItems.add(getPersonalCareData());
        listOfAllItems.add(getBabyNeedsData());
        listOfAllItems.add(getHealthData());
        listOfAllItems.add(getTechnologyData());
        listOfAllItems.add(getFoodData());
        listOfAllItems.add(getBeachSuppliesData());
        listOfAllItems.add(getCarSuppliesData());
        return listOfAllItems;
    }

    public void persistAllData(){
        List<List<Items>> listOfAllItems = getAllData();
        for(List<Items> list : listOfAllItems){
            for(Items items : list){
                database.mainDao().saveItems(items);
            }
        }
        System.out.println("Data Added");
    }
/*
    public void persistDataByCategory(String category, Boolean onlyDelete){
        try{
            List<Items> list = deleteandGetListByCategory(category, onlyDelete);
            if (!onlyDelete){
                for(Items item: list){
                    database.mainDao().saveItems(item);
                }
                Toast.makeText(this, category+ "Reset Successfully", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, category+ "Reset Successfully", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Items> deleteandGetListByCategory(String category, Boolean onlyDelete){
        if (onlyDelete){
            database.mainDao().deleteAllByCategoryAndAddedBy(category, MyConstant.SYSTEM_SMALL);
        }else{
            database.mainDao().deleteAllByCetegory(category);
        }

        switch (category){
            case MyConstant.BASIC_NEEDS_CAMEL_CASE:
                return getBasicData();
            case MyConstant.CLOTHING_CAMEL_CASE:
                return getClothingData();
            case MyConstant.PERSONAL_CARE_CAMEL_CASE:
                return getPersonalCareData();
            case MyConstant.BABY_NEEDS_CAMEL_CASE:
                return getBabyNeedsData();
            case MyConstant.HEALTH_CAMEL_CASE:
                return getHealthData();
            case MyConstant.TECHNOLOGY_CAMEL_CASE:
                return getTechnologyData();
            case MyConstant.CAR_SUPPLIES_CAMEL_CASE:
                return getCarSuppliesData();
            case MyConstant.FOOD_CAMEL_CASE:
                return getFoodData();
            case MyConstant.BEACH_SUPPLIES_CAMEL_CASE:
                return getBeachSuppliesData();
            default:
                return new ArrayList<>();
        }
    }*/


}
