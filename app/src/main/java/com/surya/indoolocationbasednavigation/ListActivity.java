package com.surya.indoolocationbasednavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListActivity extends AppCompatActivity {
    ListView l,l2;
    String Destinations[]
            = new String[1000];
    String Distance[]
            = new String[1000];
    String cat,startpos;
    ArrayAdapter<String> arr;
    ArrayAdapter<String> arr2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        l = findViewById(R.id.list);


        cat = getIntent().getStringExtra("ButtonText");
        startpos = getIntent().getStringExtra("startpos");
        Destinations = get_destinations();


    }
    public String[] get_destinations(){
        //execpt the destination that he has scanned everything should come
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Message");//child("Categories").child(cat);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String[] value = snapshot.child("Categories").child(cat).getValue(String.class).split(",");

                String[] va = new String[value.length];

                int count = 0;
                for(int i=0;i<value.length;i++){
                    int index = -1;
                    if(startpos.equals(value[i])){
                        index = i;
                    }
                    if(index == -1){
                        va[count] = value[i];
                        count++;
                    }
                }
                String a[] = new String[count];
                for(int i=0;i<count;i++){
                    a[i] = va[i];
                }
                Destinations = a;


                String[] directions = new String[10];
                String[] distance = new String[a.length];
                for(int i=0;i<Destinations.length;i++){

                    distance[i] = snapshot.child("Dist_Direct").child(startpos).child(a[i]).child("distance").getValue(String.class);
                }
                Toast.makeText(ListActivity.this,""+distance.length,Toast.LENGTH_SHORT).show();

                arr = new ArrayAdapter<String>(ListActivity.this, R.layout.support_simple_spinner_dropdown_item, a);
                l.setAdapter(arr);
                l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem = (String) parent.getItemAtPosition(position);
                        //Toast.makeText(ListActivity.this,""+selectedItem,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ListActivity.this,Navigation_Screen.class);
                        intent.putExtra("Destination",selectedItem);
                        intent.putExtra("Startpos",startpos);
                        intent.putExtra("Category",cat);
                        startActivity(intent);
                    }
                });
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return Destinations;
    }
    public String[] get_distance(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Message");//child("Dist_Direct").child(startpos);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String[] Destinations = snapshot.child("Categories").child(cat).getValue(String.class).split(",");
                String[] directions = new String[10];
                String[] distance = new String[10];
                for(int i=0;i<Destinations.length;i++){
                    distance[i] = snapshot.child(Destinations[i]).child("distance").getValue(String.class);

                }
                arr2 = new ArrayAdapter<String>(ListActivity.this, R.layout.support_simple_spinner_dropdown_item, distance);
                l.setAdapter(arr2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return Distance;
    }
}