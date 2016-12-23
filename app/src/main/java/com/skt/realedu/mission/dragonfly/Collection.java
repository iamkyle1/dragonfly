package com.skt.realedu.mission.dragonfly;

import java.io.DataInputStream;
import java.io.FileInputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * 수집된 잠자리 카드 화면
 */
public class Collection extends Activity implements OnClickListener {

    int[] score_data = new int[5];
    int number[] = new int[5];
    int card_n[] = new int[5];
    int cnt = 0;

    FrameLayout collect_card1, collect_card2, collect_card3, collect_card4, collect_card5;
    ImageView Image_card1, Image_card2, Image_card3, Image_card4, Image_card5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.collection);

        try {
            FileInputStream fis = openFileInput("game_score.txt");
            DataInputStream in = new DataInputStream(fis);

            for(int i = 0; i < 5; i++)
                score_data[i] = in.readInt();
            number = score_data;

            in.close();
            fis.close();
        } catch(Exception e) {
        }

        ImageButton collection_back_btn = (ImageButton) findViewById(R.id.collection_back_btn);

        Image_card1 = (ImageView) findViewById(R.id.Image_card1);
        Image_card2 = (ImageView) findViewById(R.id.Image_card2);
        Image_card3 = (ImageView) findViewById(R.id.Image_card3);
        Image_card4 = (ImageView) findViewById(R.id.Image_card4);
        Image_card5 = (ImageView) findViewById(R.id.Image_card5);

        collect_card1 = (FrameLayout) findViewById(R.id.collect_card1);
        collect_card2 = (FrameLayout) findViewById(R.id.collect_card2);
        collect_card3 = (FrameLayout) findViewById(R.id.collect_card3);
        collect_card4 = (FrameLayout) findViewById(R.id.collect_card4);
        collect_card5 = (FrameLayout) findViewById(R.id.collect_card5);

        if(score_data[0] == 1) {
            collect_card1.setOnClickListener(this);
        }

        if(score_data[1] == 1) {
            collect_card2.setOnClickListener(this);
        }

        if(score_data[2] == 1) {
            collect_card3.setOnClickListener(this);
        }

        if(score_data[3] == 1) {
            collect_card4.setOnClickListener(this);
        }
        if(score_data[4] == 1) {
            collect_card5.setOnClickListener(this);
        }

        collection_back_btn.setOnClickListener(this);

        for(int i = 0; i < 5; i++) {
            if(number[i] == 1) {
                card_n[cnt++] = i;
            }
        }

        for(int i = 0; i < cnt; i++) {
            if(card_n[i] == 0) {
                Image_card1.setVisibility(View.VISIBLE);
                collect_card1.setFocusable(true);
            } else if(card_n[i] == 1) {
                Image_card2.setVisibility(View.VISIBLE);
                collect_card2.setFocusable(true);
            } else if(card_n[i] == 2) {
                Image_card3.setVisibility(View.VISIBLE);
                collect_card3.setFocusable(true);
            } else if(card_n[i] == 3) {
                Image_card4.setVisibility(View.VISIBLE);
                collect_card4.setFocusable(true);
            } else if(card_n[i] == 4) {
                Image_card5.setVisibility(View.VISIBLE);
                collect_card5.setFocusable(true);
            }
        }
    }

    /**
     * 잠자리 카드 선택시 호출됨
     */
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.collect_card1: {
            Intent intent = new Intent(this, GameResult.class);
            intent.putExtra("item", 0);
            startActivity(intent);
            break;
        }
        case R.id.collect_card2: {
            Intent intent = new Intent(this, GameResult.class);
            intent.putExtra("item", 1);
            startActivity(intent);
            break;
        }
        case R.id.collect_card3: {
            Intent intent = new Intent(this, GameResult.class);
            intent.putExtra("item", 2);
            startActivity(intent);
            break;
        }
        case R.id.collect_card4: {
            Intent intent = new Intent(this, GameResult.class);
            intent.putExtra("item", 3);
            startActivity(intent);
            break;
        }
        case R.id.collect_card5: {
            Intent intent = new Intent(this, GameResult.class);
            intent.putExtra("item", 4);
            startActivity(intent);
            break;
        }

        case R.id.collection_back_btn:
            finish();
            break;
        }
    }

}