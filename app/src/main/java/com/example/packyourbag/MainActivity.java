package com.example.packyourbag;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.packyourbag.Adapter.Adapter;
import com.example.packyourbag.Data.AppData;
import com.example.packyourbag.Database.RoomDB;
import com.example.packyourbag.constants.MyConstant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<String> titles;
    List<Integer> images;
    Adapter adapter;
    RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.recyclerview);

        addAddTitles();
        addAllImages();
        persistAppData();
        database = RoomDB.getInstance(this);
        System.out.println("------------------------->"+database.mainDao().getAllSelected(false).get(0).getItemname());

        adapter = new Adapter(this, titles, images, MainActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    public void onBackPressed(){
        if (mBackPressed+TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(this, "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    private void persistAppData(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        database = RoomDB.getInstance(this);
        AppData appdata = new AppData(database);
        int last = prefs.getInt(AppData.LAST_VERSION, 0);
        if(!prefs.getBoolean(MyConstant.FIRST_TIME_CAMEL_CASE, false)){
            appdata.persistAllData();
            editor.putBoolean(MyConstant.FIRST_TIME_CAMEL_CASE, true);
            editor.commit();
        }else if(last < AppData.NEW_VERSION){
            database.mainDao().deleteAllSystemItems(MyConstant.SYSTEM_SMALL);
            appdata.persistAllData();
            editor.putInt(AppData.LAST_VERSION,AppData.NEW_VERSION);
            editor.commit();
        }
    }

    private void addAddTitles(){
        titles = new ArrayList<>();
        titles.add(MyConstant.BASIC_NEEDS_CAMEL_CASE);
        titles.add(MyConstant.CLOTHING_CAMEL_CASE);
        titles.add(MyConstant.PERSONAL_CARE_CAMEL_CASE);
        titles.add(MyConstant.BABY_NEEDS_CAMEL_CASE);
        titles.add(MyConstant.HEALTH_CAMEL_CASE);
        titles.add(MyConstant.TECHNOLOGY_CAMEL_CASE);
        titles.add(MyConstant.FOOD_CAMEL_CASE);
        titles.add(MyConstant.BEACH_SUPPLIES_CAMEL_CASE);
        titles.add(MyConstant.CAR_SUPPLIES_CAMEL_CASE);
        titles.add(MyConstant.NEEDS_CAMEL_CASE);
        titles.add(MyConstant.MY_LIST_CAMEL_CASE);
        titles.add(MyConstant.MY_SELECTIONS_CAMEL_CASE);
    }

    private void addAllImages(){
        images = new ArrayList<>();
        images.add(R.drawable.p1);
        images.add(R.drawable.p2);
        images.add(R.drawable.p3);
        images.add(R.drawable.p4);
        images.add(R.drawable.p5);
        images.add(R.drawable.p6);
        images.add(R.drawable.p7);
        images.add(R.drawable.p8);
        images.add(R.drawable.p9);
        images.add(R.drawable.p10);
        images.add(R.drawable.p11);
        images.add(R.drawable.p12);
    }
}