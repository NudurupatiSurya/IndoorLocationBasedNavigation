package com.surya.indoolocationbasednavigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class List_Adapter extends ArrayAdapter<Data> {
    public List_Adapter(Context context, ArrayList<Data> datalist) {
        super(context, R.layout.list_item, datalist);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Data data = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        TextView name_tv = convertView.findViewById(R.id.list_item_tv_name);

        name_tv.setText(data.name);


        return super.getView(position, convertView, parent);
    }
}
