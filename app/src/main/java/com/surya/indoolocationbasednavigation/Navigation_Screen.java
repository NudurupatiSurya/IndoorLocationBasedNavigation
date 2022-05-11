package com.surya.indoolocationbasednavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Navigation_Screen extends AppCompatActivity {
String Destination,Startpos,cat;
TextView tv;
Camera camera;
FrameLayout frameLayout;
Show_Camera show_camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_screen);
        Destination = getIntent().getStringExtra("Destination");
        Startpos = getIntent().getStringExtra("Startpos");
        cat = getIntent().getStringExtra("Category");
        tv = findViewById(R.id.directions);
        frameLayout = findViewById(R.id.Camera_view);

        show_directions();

    }
    public void show_directions(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Message");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String directions = snapshot.child("Dist_Direct").child(Startpos).child(Destination).child("directions").getValue(String.class);//.split(",");
                tv.setText(directions);
                //Toast.makeText(Navigation_Screen.this,directions,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        camera = Camera.open();
        show_camera = new Show_Camera(this,camera);
        frameLayout.addView(show_camera);
    }
}