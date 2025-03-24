package com.example.packyourbag;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.packyourbag.Adapter.CheckListAdapter;
import com.example.packyourbag.Database.RoomDB;
import com.example.packyourbag.Models.Items;
import com.example.packyourbag.constants.MyConstant;

import java.util.ArrayList;
import java.util.List;

public class CheckList extends AppCompatActivity {
    RecyclerView recyclerView;
    CheckListAdapter checkListAdapter;
    RoomDB database;
    List<Items> itemsList = new ArrayList<>();
    String header, show;

    EditText txtAdd;
    Button btnAdd;
    LinearLayout linearLayout;

    /*@Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_1, menu);

        if (MyConstant.MY_SELECTIONS.equals(header)){
            menu.getItem(0).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        } else if (MyConstant.MY_LIST_CAMEL_CASE.equals(header)) {
            menu.getItem(1).setVisible(false);
        }

        MenuItem menuitem = menu.findItem(R.id.btnSearch);
        SearchView searchView = (SearchView) menuitem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Items> finalList = new ArrayList<>();
                for(Items items : itemsList){
                    if (items.getItemname().toLowerCase().startsWith(newText.toLowerCase())){
                        finalList.add(items);
                    }
                }
                updateRecycler(finalList);
                return false;
            }
        });

        return true;
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        header = intent.getStringExtra(MyConstant.HEADER_SMALL);
        show = intent.getStringExtra(MyConstant.SHOW_SMALL);

        getSupportActionBar().setTitle(header);

        txtAdd = findViewById(R.id.txtAdd);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerview);
        linearLayout = findViewById(R.id.linearlayout);

        database = RoomDB.getInstance(this);

        if(MyConstant.FALSE_STRING.equals(show)){
            linearLayout.setVisibility(View.GONE);
            itemsList = database.mainDao().getAllSelected(true);
        }else{
            itemsList = database.mainDao().getAll(header);
        }

        updateRecycler(itemsList);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = txtAdd.getText().toString();
                if (itemName!=null && !itemName.isEmpty()){
                    addNewItem(itemName);
                    Toast.makeText(CheckList.this, "Item Added", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(CheckList.this, "Empty Can't be Added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addNewItem(String itemName){
        Items item = new Items();
        item.setChecked(false);
        item.setCategory(header);
        item.setItemname(itemName);
        item.setAddedby(MyConstant.USER_SMALL);
        database.mainDao().saveItems(item);
        itemsList = database.mainDao().getAll(header);
        updateRecycler(itemsList);
        recyclerView.scrollToPosition(checkListAdapter.getItemCount() - 1);
        txtAdd.setText(" ");
    }

    private void updateRecycler(List<Items> itemsList){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        checkListAdapter = new CheckListAdapter(CheckList.this, itemsList, database,show);
        recyclerView.setAdapter(checkListAdapter);
    }
}