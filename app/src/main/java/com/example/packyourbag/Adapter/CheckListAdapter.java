package com.example.packyourbag.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.packyourbag.Database.RoomDB;
import com.example.packyourbag.Models.Items;
import com.example.packyourbag.R;
import com.example.packyourbag.constants.MyConstant;

import java.util.List;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListViewHolder>{

    Context context;
    List<Items> itemList;
    RoomDB database;
    String show;

    public CheckListAdapter() {
    }

    public CheckListAdapter(Context context, List<Items> itemList, RoomDB database, String show) {
        this.context = context;
        this.itemList = itemList;
        this.database = database;
        this.show = show;
        if(itemList.size() == 0){
            Toast.makeText(context.getApplicationContext(), "Nothing to show", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public CheckListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckListViewHolder(LayoutInflater.from(context).inflate(R.layout.check_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListViewHolder holder, int position) {
        holder.checkbox.setText(itemList.get(position).getItemname());
        holder.checkbox.setChecked(itemList.get(position).getChecked());

        if(MyConstant.FALSE_STRING.equals(show)){
            holder.btndelete.setVisibility(View.GONE);
            holder.layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_1));
        }else{
            if(itemList.get(position).getChecked()){
                holder.layout.setBackgroundColor(Color.parseColor("#8e546f"));
            }else {
                holder.layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_1));
            }
        }

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = holder.checkbox.isChecked();
                database.mainDao().checkuncheck(itemList.get(position).getID(),check);
                if(MyConstant.FALSE_STRING.equals(show)){
                    itemList = database.mainDao().getAllSelected(true);
                    notifyDataSetChanged();
                }else {
                    itemList.get(position).setChecked(check);
                    notifyDataSetChanged();
                    Toast tostMessage = null;
                    if (tostMessage != null){
                        tostMessage.cancel();
                    }
                    if(itemList.get(position).getChecked()){
                        tostMessage = Toast.makeText(context, "(" +holder.checkbox.getText()+ ") Packed", Toast.LENGTH_SHORT);
                    }else {
                        tostMessage = Toast.makeText(context, "(" +holder.checkbox.getText()+ ") Unpacked", Toast.LENGTH_SHORT);
                    }
                    tostMessage.show();
                }
            }
        });

        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete ("+itemList.get(position).getItemname()+")")
                        .setMessage("Are you Sure?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                database.mainDao().delete(itemList.get(position));
                                itemList.remove(itemList.get(position));
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        }).setIcon(R.drawable.ic_delete)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

class CheckListViewHolder extends RecyclerView.ViewHolder{

    LinearLayout layout;
    CheckBox checkbox;
    Button btndelete;

    public CheckListViewHolder(@NonNull View itemView) {
        super(itemView);
        layout = itemView.findViewById(R.id.linearlayout);
        checkbox = itemView.findViewById(R.id.checkbox);
        btndelete = itemView.findViewById(R.id.btndelete);
    }
}