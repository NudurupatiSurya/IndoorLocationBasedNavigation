package com.surya.indoolocationbasednavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Navigation_Screen extends AppCompatActivity {
    String Destination, Startpos, cat;
    TextView tv;
    Camera camera;
    Button nextdirectbutton;
    double walking_speed = 0.000833333;
    int delay = 0;
    boolean nextdirect = false;

    GridLayout frameLayout, last_screen;
    int clickedcount = 0;
    Show_Camera show_camera;
    Handler handler2;
    int countforbutton = 0;
    ImageView direction, down, left, right, straight, uturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_screen);
        Destination = getIntent().getStringExtra("Destination");
        Startpos = getIntent().getStringExtra("Startpos");
        cat = getIntent().getStringExtra("Category");
        tv = findViewById(R.id.directions);
        nextdirectbutton = findViewById(R.id.nextdirectbutton);
        frameLayout = findViewById(R.id.Camera_view);
        last_screen = findViewById(R.id.last_screen);
        declare_images();
        show_directions();

    }

    public void declare_images() {
        direction = findViewById(R.id.direction);

    }

    public void show_directions() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Message");
        int shown = 0;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String[] directions = snapshot.child("Dist_Direct").child(Startpos).child(Destination).child("directions").getValue(String.class).split(",");
                String[] d = directions[0].split(" ");

                delay = (int) (Integer.parseInt(d[1]) / walking_speed);

                int count = 0;
                for (int i = 0; i < directions.length; i++) {
                    final int j = i;//temp for handler
                    if (count != 0) {
                        Handler handler = new Handler();

                        int finalCount = count;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ar_directions(directions[j]);
                                check_last(directions, finalCount, false);
                            }
                        }, delay);

                        boolean lastah = false;
                        if (i == directions.length - 1) {
                            lastah = true;
                        }
                        final boolean l = lastah;

                        nextdirectbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handler.removeMessages(0);
                                if(countforbutton == directions.length-1){
                                    frameLayout.setVisibility(View.GONE);
                                    tv.setVisibility(View.GONE);
                                    last_screen.setVisibility(View.VISIBLE);
                                }
                                if (j < directions.length) {
                                    //remove_runnable();
                                    ar_directions(directions[j]);
                                    if (finalCount + 1 == 3) {
                                        frameLayout.setVisibility(View.GONE);
                                        tv.setVisibility(View.GONE);
                                        last_screen.setVisibility(View.VISIBLE);
                                    }
                                    check_last(directions, finalCount + 1, false);

                                } else {
                                    frameLayout.setVisibility(View.GONE);
                                    tv.setVisibility(View.GONE);
                                    last_screen.setVisibility(View.VISIBLE);

                                }
                                countforbutton++;
                            }
                        });



                    } else {
                        ar_directions(directions[j]);
                    }
                    count++;
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        camera = Camera.open();
        show_camera = new Show_Camera(this, camera);
        frameLayout.addView(show_camera);
    }


    public void check_last(String[] directions, int count, boolean lastah) {
        handler2 = new Handler();
        if (count <= directions.length - 1) {
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //nothing
                    frameLayout.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                    last_screen.setVisibility(View.VISIBLE);
                }
            }, delay);
        }

        if (lastah == true) {
            handler2.removeMessages(0);
            frameLayout.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            last_screen.setVisibility(View.VISIBLE);
        }
    }

    public void ar_directions(String direct) {


        String[] d = direct.split(" ");
        for (int i = 0; i < d.length; i++) {
            tv.setText("Walk " + d[0] + " for " + d[1] + " Meters");
            delay = (int) (Integer.parseInt(d[1]) / walking_speed);

            switch (d[0]) {
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
        }
    }
}