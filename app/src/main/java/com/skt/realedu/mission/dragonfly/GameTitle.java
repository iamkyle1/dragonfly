package com.skt.realedu.mission.dragonfly;

import java.io.FileInputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 게임 첫화면
 */
public class GameTitle extends Activity implements OnClickListener {

    TextView card_info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.gametitle);

        TextView title_play = (TextView) findViewById(R.id.title_play);
        TextView title_help = (TextView) findViewById(R.id.title_help);
        card_info = (TextView) findViewById(R.id.card_info);
        ImageButton intro_back_btn = (ImageButton) findViewById(R.id.intro_back_btn);

        card_info.setOnClickListener(this);
        title_play.setOnClickListener(this);
        title_help.setOnClickListener(this);
        intro_back_btn.setOnClickListener(this);

        setCellectionButtonVisible();
    }

    /**
     * 게임 결과 반영
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, GamePlay.class);
            startActivityForResult(intent, 100);
        }

        else if(requestCode == 100 && resultCode == RESULT_CANCELED) {
            setCellectionButtonVisible();
        }
    }

    /**
     * 게임 이력 여부 확인
     */
    private void setCellectionButtonVisible() {
        try {
            FileInputStream fis = openFileInput("game_score.txt");
            fis.close();
            card_info.setVisibility(View.VISIBLE);
        } catch(Exception e) {
            card_info.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.title_play: {
            Intent intent = new Intent(this, GamePlay.class);
            startActivityForResult(intent, 100);
            break;
        }

        case R.id.title_help: {
            Intent intent = new Intent(this, GameHelp.class);
            startActivity(intent);
            break;
        }

        case R.id.intro_back_btn: {
            finish();
            break;
        }

        case R.id.card_info: {
            GamePlay.dragonfly_catch = new int[5];
            Intent intent = new Intent(this, Collection.class);
            startActivityForResult(intent, 100);
            break;
        }
        }
    }
}