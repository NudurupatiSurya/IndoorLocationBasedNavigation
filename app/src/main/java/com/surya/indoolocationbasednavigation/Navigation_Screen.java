package com.surya.indoolocationbasednavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
double walking_speed = 0.000833333;
int delay=0;
//FrameLayout frameLayout;
GridLayout frameLayout,last_screen;
Show_Camera show_camera;
ImageView direction,down,left,right,straight,uturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_screen);
        Destination = getIntent().getStringExtra("Destination");
        Startpos = getIntent().getStringExtra("Startpos");
        cat = getIntent().getStringExtra("Category");
        tv = findViewById(R.id.directions);
        frameLayout = findViewById(R.id.Camera_view);
        last_screen = findViewById(R.id.last_screen);
        declare_images();
        show_directions();

    }
    public void declare_images(){
        direction = findViewById(R.id.direction);
        /*down = findViewById(R.id.down);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        straight = findViewById(R.id.straight);
        uturn = findViewById(R.id.uturn);*/
    }
    public void show_directions(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Message");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String[] directions = snapshot.child("Dist_Direct").child(Startpos).child(Destination).child("directions").getValue(String.class).split(",");
                String[] d = directions[0].split(" ");
                //tv.setText("Walk " + d[0] + " for " +d[1] +" Meters");
                delay = (int) (Integer.parseInt(d[1])/walking_speed);
                /*Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {*/
                int count = 0;
                for(int i=0;i<directions.length;i++) {
                    final int j = i;
                    if(count!=0){
                        Handler handler = new Handler();
                        int finalCount = count;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //nothing
                                ar_directions(directions[j]);
                                check_last(directions, finalCount);
                            }
                        },3000);
                    }
                    else{
                        ar_directions(directions[j]);
                    }
                    count++;
                }

                    /*        }
                        },3000);*/

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
    public void check_last(String[] directions,int count){
        Handler handler = new Handler();
        if(count == directions.length-1) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //nothing
                    frameLayout.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                    last_screen.setVisibility(View.VISIBLE);
                }
            }, 3000);
        }
    }
    public void ar_directions(String direct){

                // Show directions
                //tv.setText(directions[0].split(" ")[0]);
            String[] d = direct.split(" ");
            for(int i=0;i<d.length;i++){
                    tv.setText("Walk " + d[0] + " for " +d[1] +" Meters");
                    delay = (int) (Integer.parseInt(d[1])/walking_speed);
                    switch (d[0]){
                        case "Straight":
                            direction.setImageDrawable(getDrawable(R.drawable.straight));
                            break;
                        case "Left":
                            direction.setImageDrawable(getDrawable(R.drawable.left));
                            break;
                        case "Right":
                            direction.setImageDrawable(getDrawable(R.drawable.right));
                            break;
                        case "Up":
                            direction.setImageDrawable(getDrawable(R.drawable.up));
                            break;
                        case "Down":
                            direction.setImageDrawable(getDrawable(R.drawable.down));
                            break;
                        case "Uturn":
                            direction.setImageDrawable(getDrawable(R.drawable.uturn));
                            break;

                }
                   /* Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //nothing
                        }
                    },delay);*/
            }
    }
}