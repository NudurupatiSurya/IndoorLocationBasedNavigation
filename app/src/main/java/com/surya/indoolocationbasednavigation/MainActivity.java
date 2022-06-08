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
import java.util.Map;

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

       scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StartPos = result.getText();

                        Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                        intent.putExtra("Startposi",StartPos);
                        startActivity(intent);

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

                Map<String, Object> Categories = (Map<String, Object>) dataSnapshot.getValue();

                String[] key = Categories.keySet().toArray(new String[Categories.keySet().size()]);

                Button1.setText(key[0]);
                Button2.setText(key[1]);
                Button3.setText(key[2]);

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

                final String[] Name = new String[1];
                var = Button1.getText().toString();
                Toast.makeText(MainActivity.this,""+var,Toast.LENGTH_LONG).show();
                DatabaseReference myRef = database.getReference("Message").child("Categories").child(var);
                DatabaseReference myRef2 = database.getReference("Message").child("Dist_Direct").child(StartPos);

                categories.setVisibility(View.GONE);

                list.setVisibility(View.VISIBLE);

                binding = ActivityMainBinding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());
                ArrayList<Data> dataArrayList = new ArrayList<>();
                for(int i=0;i<destinations.length;i++){
                    Data data = new Data(destinations[i]);
                    dataArrayList.add(data);
                }

                List_Adapter list_adapter = new List_Adapter(this,dataArrayList);

        }
    }
    @Override
    public void onBackPressed(){
        this.finishAffinity();
    }

}
