package com.example.muzik3;

import java.lang.reflect.Field;
import android.app.Activity;
import android.app.AppComponentFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer mp;
    View oncekiView;
    ListView listView;
    Drawable orjArkaFon;
    TextView textBilgi;
    SeekBar seekBar ;
    Button btnplay,btnback,btnfor;
    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        seekBar = findViewById(R.id.seekbar);
        handler = new Handler();
        btnback = findViewById(R.id.btnback);
        btnfor = findViewById(R.id.btnfor);
        btnplay = findViewById(R.id.btnplay);

        btnplay.setOnClickListener(this);
        btnfor.setOnClickListener(this);
        btnback.setOnClickListener(this);

        Field[] fields = R.raw.class.getFields();
        String[] mediaList = new String[fields.length];
        for (int count = 0; count < fields.length; count++)
            mediaList[count] = fields[count].getName();

        listView = (ListView) findViewById(R.id.calma_liste);
        textBilgi = (TextView) findViewById(R.id.textBilgi1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_list_item_1,
                mediaList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position,
                                    long id) {
                if (oncekiView != null)
                    ((TextView) oncekiView).setBackgroundDrawable(orjArkaFon);
                orjArkaFon = ((TextView) v).getBackground();

                String uri = "android.resource://" + getPackageName() + "/raw/"
                        + ((TextView) v).getText();

                if (mp != null && mp.isPlaying())
                    mp.stop();

                mp = MediaPlayer.create(getApplicationContext(), Uri.parse(uri));


                ((TextView) v).setBackgroundColor(Color.GREEN);
                oncekiView = v;

                textBilgi.setText(((TextView) v).getText() + " ["
                        + mp.getDuration() / 1000 + " sn.]");
                seekBar.setMax(mp.getDuration());
                mp.start();
                changeseekbar();

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b ) {
                if (b){
                    mp.seekTo(i);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void changeseekbar() {
        seekBar.setProgress(mp.getCurrentPosition());
        if(mp.isPlaying()){
            runnable = new Runnable(){
                @Override
                public void run (){
                    changeseekbar();
                }
            };
            handler.postDelayed(runnable,1000);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnplay:
                if (mp.isPlaying()){

                    mp.pause();
                    btnplay.setText(">");
                }else {
                    mp.start();
                    btnplay.setText("||");
                    changeseekbar();
                }
                break;
            case R.id.btnfor:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btnback:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
        }
    }
}