package com.skt.realedu.mission.dragonfly;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 도움말 화면
 */
public class GameHelp extends Activity implements OnClickListener {

    int pageIndex;
    TextView help_text1, help_text2, help_text3, help_text4, help_text5;
    RelativeLayout screen_help;
    static String[][] help_text = { { "How to play - Basic decription", "Catch the flying dragonflies on the screen in 60 seconds.", "Pan your phone around in any direction to find", "more dragonflies." }, { "How to play - Radar", "Radars located along the edges of the screen keep", "you informed of the whereabouts of other dragonflies.", "" }, { "How to play - Catch", "Catch the dragonflies flying in front of the frog by wiggling", "your phone up and down or by touching the frog.", "" } };

    static int[] Imagehelp = { R.drawable.game_help_bg01, R.drawable.game_help_bg02, R.drawable.game_help_bg03 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.gamehelp);

        help_text1 = (TextView) findViewById(R.id.help_text1);
        help_text2 = (TextView) findViewById(R.id.help_text2);
        help_text3 = (TextView) findViewById(R.id.help_text3);
        help_text4 = (TextView) findViewById(R.id.help_text4);
        help_text5 = (TextView) findViewById(R.id.help_text5);
        screen_help = (RelativeLayout) findViewById(R.id.screen_help);

        ImageButton game_help_arrow02 = (ImageButton) findViewById(R.id.game_help_arrow02);
        ImageButton game_help_arrow01 = (ImageButton) findViewById(R.id.game_help_arrow01);

        game_help_arrow01.setOnClickListener(this);
        game_help_arrow02.setOnClickListener(this);

        pageIndex = 0;

        help_text1.setText(help_text[pageIndex][0]);
        help_text2.setText(help_text[pageIndex][1]);
        help_text3.setText(help_text[pageIndex][2]);
        help_text4.setText(help_text[pageIndex][3]);
        help_text5.setText((pageIndex + 1) + "/3");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.game_help_arrow01:
            pageIndex--;
            if(pageIndex < 0)
                pageIndex += Imagehelp.length;
            {
                screen_help.setBackgroundResource(Imagehelp[pageIndex]);
                help_text1.setText(help_text[pageIndex][0]);
                help_text2.setText(help_text[pageIndex][1]);
                help_text3.setText(help_text[pageIndex][2]);
                help_text4.setText(help_text[pageIndex][3]);
                help_text5.setText((pageIndex + 1) + "/3");
                break;
            }

        case R.id.game_help_arrow02:
            pageIndex = (pageIndex + 1) % Imagehelp.length;
            {
                screen_help.setBackgroundResource(Imagehelp[pageIndex]);
                help_text1.setText(help_text[pageIndex][0]);
                help_text2.setText(help_text[pageIndex][1]);
                help_text3.setText(help_text[pageIndex][2]);
                help_text4.setText(help_text[pageIndex][3]);
                help_text5.setText((pageIndex + 1) + "/3");
                break;
            }
        }
    }
}
