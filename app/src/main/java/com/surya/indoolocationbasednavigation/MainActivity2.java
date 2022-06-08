package com.surya.indoolocationbasednavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
Button B1,B2,B3;

    String startpos,cat1,cat2,cat3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        startpos = getIntent().getStringExtra("Startposi");
        B1 = findViewById(R.id.cat1);
        B2 = findViewById(R.id.cat2);
        B3 = findViewById(R.id.cat3);
        B1.setOnClickListener(this);
        B2.setOnClickListener(this);
        B3.setOnClickListener(this);

        Display_Categories();
    }
    public void Display_Categories(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Message").child("Categories");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Object> Categories = (Map<String, Object>) dataSnapshot.getValue();

                String[] key = Categories.keySet().toArray(new String[Categories.keySet().size()]);

                B1.setText(key[0]);
                B2.setText(key[1]);
                B3.setText(key[2]);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity2.this, "Failed to read value.", Toast.LENGTH_LONG).show();
            }
        });
        cat1 = B1.getText().toString();
        cat2 = B2.getText().toString();
        cat3 = B3.getText().toString();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,ListActivity.class);
        switch (v.getId()){
            case R.id.cat1:
                intent.putExtra("ButtonText",B1.getText());
                intent.putExtra("startpos",startpos);
                startActivity(intent);
                break;
            case R.id.cat2:
                intent.putExtra("ButtonText",B2.getText());
                intent.putExtra("startpos",startpos);
                startActivity(intent);
                break;
            case R.id.cat3:
                intent.putExtra("ButtonText",B3.getText());
                intent.putExtra("startpos",startpos);
                startActivity(intent);
                break;
        }
    }

}