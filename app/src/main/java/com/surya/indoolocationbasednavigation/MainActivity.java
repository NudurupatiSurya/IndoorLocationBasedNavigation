package com.surya.indoolocationbasednavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.surya.indoolocationbasednavigation.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CodeScanner mCodeScanner;
    String StartPos,Categories,cat1,cat2,cat3,dist_var,var;
    Button Button1,Button2,Button3;
    ActivityMainBinding binding;
    GridLayout categories,Scanner;
    TextView dummy;
    ListView list;
    CodeScannerView scannerView;
    String[] distances = new String[100];
    String[][] directions = new String[100][100];
    String[] destinations = new String[100];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* Button1 = findViewById(R.id.cat1);
        Button2 = findViewById(R.id.cat2);
        Button3 = findViewById(R.id.cat3);

        Button1.setOnClickListener(this);
        Button2.setOnClickListener(this);
        Button3.setOnClickListener(this);
        dummy = findViewById(R.id.dummy);*/
        //categories = findViewById(R.id.categories);
       // list = (ListView) findViewById(R.id.list);
       scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StartPos = result.getText();
                        //scannerView.setVisibility(View.GONE);
                        //categories.setVisibility(View.VISIBLE);
                        //displaycategories();
                        Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                        intent.putExtra("Startposi",StartPos);
                        startActivity(intent);
                        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Message").child("Categories").child("CSE");

                       /* myRef.setValue(result.getText());
                        Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();*/
                       /* myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                String value = dataSnapshot.getValue(String.class);
                                Toast.makeText(MainActivity.this, "Value is: " + value,Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Toast.makeText(MainActivity.this, "Failed to read value.", Toast.LENGTH_LONG).show();
                            }
                        });*/
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
    public void displaycategories(){
        categories.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Message").child("Categories");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                /*Categories = dataSnapshot.getValue(String.class);
                Button1.setText(Categories.split(",")[0]);
                Button2.setText(Categories.split(",")[1]);
                Button3.setText(Categories.split(",")[2]);*/
                Map<String, Object> Categories = (Map<String, Object>) dataSnapshot.getValue();
                //Toast.makeText(MainActivity.this, "" + Categories.keySet(),Toast.LENGTH_LONG).show();
                String[] key = Categories.keySet().toArray(new String[Categories.keySet().size()]);
                //Toast.makeText(MainActivity.this, "" + key[0],Toast.LENGTH_LONG).show();
                Button1.setText(key[0]);
                Button2.setText(key[1]);
                Button3.setText(key[2]);
                //Toast.makeText(MainActivity.this, "" + Categories.keySet(),Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, "Failed to read value.", Toast.LENGTH_LONG).show();
            }
        });
         Toast.makeText(MainActivity.this, "" + Categories,Toast.LENGTH_LONG).show();
         cat1 = Button1.getText().toString();
         cat2 = Button2.getText().toString();
         cat3 = Button3.getText().toString();
    }

    @Override
    public void onClick(View v) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        switch (v.getId()){
            case R.id.cat1:
                //Toast.makeText(this,StartPos,Toast.LENGTH_LONG).show();
                final String[] Name = new String[1];
                var = Button1.getText().toString();
                Toast.makeText(MainActivity.this,""+var,Toast.LENGTH_LONG).show();
                DatabaseReference myRef = database.getReference("Message").child("Categories").child(var);
                DatabaseReference myRef2 = database.getReference("Message").child("Dist_Direct").child(StartPos);
               /* myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //cse lo unna categories list
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        Categories = dataSnapshot.getValue(String.class);
                        String[] name = Categories.split(",");
                        String k;
                        k = name[0];
                        for(int i=1;i<name.length;i++){
                            k = k+","+name[i];
                        }
                        dummy.setText(k);
                        //Toast.makeText(MainActivity.this,""+Name[0],Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Toast.makeText(MainActivity.this, "Failed to read value.", Toast.LENGTH_LONG).show();
                    }
                });
                String[] name = dummy.getText().toString().split(",");
                //name has list of all the cse department classes
                ArrayList<String> display = new ArrayList<String>();
                for(int j=0;j<name.length;j++){
                    if(StartPos.equals(name[j]) == false){
                        display.add(name[j]);
                    }
                }
                Toast.makeText(MainActivity.this,""+display,Toast.LENGTH_LONG).show();
                //in display you'll be having the destinations
                */
              //  get_destinations(var);
                //get_distance_directions();
               /* myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //cse lo unna categories list
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        Categories = dataSnapshot.getValue(String.class);
                        //dummy.setText(Categories);
                        collect(Categories);
                        //Toast.makeText(MainActivity.this,""+dummy.getText(),Toast.LENGTH_LONG).show();
                    } //return list of destinations

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Toast.makeText(MainActivity.this, "Failed to read value.", Toast.LENGTH_LONG).show();
                    }

                });*/

                /*String[] name = dummy.getText().toString().split(",");
                //name has list of all the cse department classes
                ArrayList<String> display = new ArrayList<String>();
                for(int j=0;j<name.length;j++){
                    if(StartPos.equals(name[j]) == false){
                        display.add(name[j]);
                    }
                }*/

                //get_destinations(var);
               // get_distance_directions();
                /*myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //scan chesina place nunchi to other destinations dist_direct
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                        String[] dist_direct = value.get("N214").split(",");
                        Toast.makeText(MainActivity.this,""+dist_direct[0],Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,"",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Toast.makeText(MainActivity.this, "Failed to read value.", Toast.LENGTH_LONG).show();
                    }
                });*/
                //two arrays one with names of the destinations and other with distance to those destinations
                categories.setVisibility(View.GONE);
                //Scanner.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);

                binding = ActivityMainBinding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());
                ArrayList<Data> dataArrayList = new ArrayList<>();
                for(int i=0;i<destinations.length;i++){
                    Data data = new Data(destinations[i]);
                    dataArrayList.add(data);
                }

                List_Adapter list_adapter = new List_Adapter(this,dataArrayList);
               /* binding.list.setAdapter(list_adapter);
                binding.list.setClickable(true);*/
        }
    }
   /* public void collect_destinations(String c){
        //Toast.makeText(this,c,Toast.LENGTH_LONG).show();
        destinations = c.split(",");
        Toast.makeText(this, destinations[0], Toast.LENGTH_SHORT).show();
    }*/
    /*public void collect_dist_direct(HashMap<String,String> c){
    String[] d = destinations;
        /*for(int i=0;i<d.length;i++){
            distances[i] = c.get(d[i]).split(",")[0];
            //directions[i] = c.get(destinations[i]).split(",")[1].split(",");
        }
        distances[0] = c.get("N214").split(",")[0];
        Toast.makeText(this, ""+distances[0], Toast.LENGTH_SHORT).show();
    }*/

    /*public void get_destinations(String var){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Message").child("Categories").child(var);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //cse lo unna categories list
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Categories = dataSnapshot.getValue(String.class);
                //dummy.setText(Categories);
                collect_destinations(Categories);
                //Toast.makeText(MainActivity.this,""+dummy.getText(),Toast.LENGTH_LONG).show();
            } //return list of destinations

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, "Failed to read value.", Toast.LENGTH_LONG).show();
            }

        });
    }*/
    /*public void get_distance_directions(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database.getReference("Message").child("Dist_Direct").child(StartPos);
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //scan chesina place nunchi to other destinations dist_direct
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                //String[] dist_direct = value.get("N214").split(",");
                //Toast.makeText(MainActivity.this,""+dist_direct[0],Toast.LENGTH_LONG).show();
                collect_dist_direct(value);

                /*Toast.makeText(MainActivity.this,""+destinations.get(0),Toast.LENGTH_LONG).show();
                dummy.setText(value.get(destinations.get(0)).split(",").toString());
                Toast.makeText(MainActivity.this,""+dummy.getText(),Toast.LENGTH_LONG).show();
                for(int i=0;i<destinations.size();i++){

                }*/
                //Toast.makeText(MainActivity.this,"",Toast.LENGTH_LONG).show();
               /* HashMap<String, String> dest = (HashMap<String, String>) dataSnapshot.getValue();
                Toast.makeText(MainActivity.this,""+dest,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, "Failed to read value.", Toast.LENGTH_LONG).show();
            }
        });
    }*/

}
