package com.skt.realedu.mission.dragonfly;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 게임 결과 화면
 */
public class GameResult extends Activity implements OnClickListener {

    String flyname[] = { "Widow Skimmer", "Common Hawker", "Common Whitetail", "Green Darner", "Red-veined darter" };

    String Infomation[] = { "The Widow Skimmer (Libellula luctuosa) is one of the group of dragonflies known as King Skimmers. The species occurs commonly across much of the United States except in the higher Rocky Mountains areas and in souther Ontario and Quebec." + "\n\nSource: Wikipedia\nPhoto by Clinton & Charles Robertson",

    "The Common Hawker or Sedge Darner (Aeshna juncea) is one of the larger species of hawker dragonflies. It is native to Eurasia and northern North America. The flight period is from June to early October." + "\n\nSource: Wikipedia Photo by Tony Hisqett",

    "The Common Whitetail or Long-tailed Skimmer (Libellula lydia) is a common dragonfly across much of North America, with a striking and unusual appearance. The male's chunky white body (about 5cm long), combined with the brownish-black bands on its otherwise translucent wings, give it a checkered look. Females have a brown body and a different pattern of wing spots, closely resembling that of female Libellula pulchella, the Twelve-spotted Skimmer." + "\n\nSource: Wikipedia\nPhoto by Benny Mazur",

    "The Green Darner or Common Green Darner(Anax junius), after its resemblance to a darning-needle, is a species of dragonfly in the family Aeshnidae. Common throughout North America, it ranges south to Panama and occurs in the Caribbean, Tahiti, and Asia from Japan to mainland China. It is the official insect for the state of Washington in the United States." + "\n\nSource: Wikipedia\nPhoto by Bruce Marlin",

    "The red-veined darter (Sympetrum fonscolombii) is a dragonfly of the genus Sympetrum. It is a common species in southern Europe and from the 1990s onwards has increasingly been found in northwest Europe, including Britain and Ireland. Its name is sometimes spelt fonscolombei instead of fonscolombii but Askew (2004) gives the latter as the correct spelling. There is genetic and behavioural evidence that S. fonscolombii is not closely related to the other members of the Sympetrum genus and will at some time in the future be removed from this genus." + "\n\nSource: Wikipedia\nPhoto by Vitaly Charny" };

    String card_Infomation[] = {
            "Adults have a steely blue body area but juveniles are yellow with brown stipes. Wings of both sexes are marked with prominent black basal bands. Adult males develop broad white spots at midwing. The nymphs live in the water, molting and growing until they are ready to emerge from the water and then molting a final time to reveal their wings.",

            "It is 74 millimetres (2.9 in) long with a brown body. The male has a black abdomen with paired blue and yellow spots on each abdominal segment, and narrow stripes along the dorsal surface of the thorax. In the female, the abdomen is brown with yellow or sometimes green or blue spots. The wings of both sexes display a yellow costa (the major vein running along the leading edge of the wings). This species lacks the green thorax stripes of the Southern Hawker",

            "The Common Whitetail can be seen hawking for mosquitoes and other small flying insects over ponds, marshes, and slow-moving rivers in most regions except the higher mountain regions. Periods of activity vary between regions; for example in California, the adults are active from April to September. Like all perchers, Common Whitetails often rest on objects near the water, and sometimes on the ground. Males are territorial, holding a 10 to 30 meter stretch of " + "the water's edge, and patrolling it to drive off other males. The white pruinescence on the abdomen, found only in mature males, is displayed to other males as a territorial threat. The nymphs are dark green or brown, but are usually found covered in algae. They feed on aquatic invertebrates such as mayfly larvae and small crayfish, and also on small aquatic vertebrates such as tadpoles and minnows. Because of their abundance, whitetail naiads are in turn an "
                    + "important food source for various fish, frogs, and birds, and also for other aquatic insects.Some authorities classify the whitetails, including the Common Whitetail, in genus Plathemis rather than Libellula. This matter has been debated at least since the end of the nineteenth century. Recent molecular systematics evidence suggests that separation of the whitetails from the rest of Libellula may be appropriate.",

            "The Green Darner is one of the largest dragonflies existent: males grow to 76mm (3.0 in) in length with a wingspan of up to 80mm (3.1 in). Females oviposit in aquatic vegetation, eggs laid beneath the water surface. Nymphs (naiads) are aquatic carnivores, feeding on insects, tadpoles and small fish. Adult darners catch insects on the wing, including ant royalty, moths, mosquitoes and flies.",

            "S. fonscolombii is similar to other Sympetrum species but a good view with binoculars should give a positive identification, especially with a male. Males have a red abdomen, redder than many other Sympetrum species. The wings have red veins and the wing bases of the hind-wings are yellow. The pterostigma are pale with a border of black veins and the underside of the eye is blue/grey. The female is similar but the abdomen is yellow, not red, and the wings have yellow veins, not red veins as found in the males. The legs of both sexes are mostly black with some yellow. Immature males are like females but often with more red. Male S. fonscolombii can be mistaken for Crocothemis erythraea as both are very red dragonflies with yellow bases to the wings, red veins and pale pterostigma. However C. erythraea has no black on the legs, a broader body and no black on the head. Also C. erythraea females do not oviposit in tandem. The jizz of these two species is different and with some experience are easy to tell apart." };

    int number[] = new int[5], card_n[] = new int[5], cnt = 0;
    int pageIndex = 0;

    static int[] result_Img = { R.drawable.game_result_bugs01, R.drawable.game_result_bugs02, R.drawable.game_result_bugs03, R.drawable.game_result_bugs04, R.drawable.game_result_bugs05 };

    TextView result_info, result_number, result_cardinfo, card_flyname, result_name;

    private boolean isCardShowing;

    ImageButton card_arrow01, card_arrow02;
    Button result_send, result_replay, result_end;
    ImageView result_img;
    RelativeLayout result_lable;
    RelativeLayout result_lable1;
    RelativeLayout resultParent;
    RelativeLayout card_bottom;

    ScrollView scroll1, scroll2;

    /**
     * 터치 제스처 처리
     */
    GestureDetector gd = new GestureDetector(new GestureDetector.OnGestureListener() {
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        public void onShowPress(MotionEvent e) {
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onDown(MotionEvent e) {
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if(e2.getX() - e1.getX() > 100 && isCardShowing) {
                changeToback();
            } else if(e2.getX() - e1.getX() < -100 && !isCardShowing) {
                changeToCard();
            }

            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        isCardShowing = false;

        number = GamePlay.dragonfly_catch;
        setContentView(R.layout.gameresult);
        result_number = (TextView) findViewById(R.id.result_number);
        result_info = (TextView) findViewById(R.id.result_info);

        card_flyname = (TextView) findViewById(R.id.card_flyname);
        result_name = (TextView) findViewById(R.id.result_name);
        result_cardinfo = (TextView) findViewById(R.id.result_cardinfo);
        Button result_send = (Button) findViewById(R.id.result_send);
        Button result_replay = (Button) findViewById(R.id.result_replay);
        Button result_end = (Button) findViewById(R.id.result_end);
        result_img = (ImageView) findViewById(R.id.result_img);
        ImageButton card_btn01 = (ImageButton) findViewById(R.id.card_btn01);
        ImageButton card_btn02 = (ImageButton) findViewById(R.id.card_btn02);
        card_arrow01 = (ImageButton) findViewById(R.id.card_arrow01);
        card_arrow02 = (ImageButton) findViewById(R.id.card_arrow02);

        scroll1 = (ScrollView) findViewById(R.id.infoScroll1);
        scroll2 = (ScrollView) findViewById(R.id.infoScroll2);

        card_btn02.setOnClickListener(this);
        card_arrow02.setOnClickListener(this);
        card_arrow01.setOnClickListener(this);
        card_btn01.setOnClickListener(this);
        result_send.setOnClickListener(this);
        result_replay.setOnClickListener(this);
        result_end.setOnClickListener(this);

        resultParent = (RelativeLayout) findViewById(R.id.resultParent);
        result_lable = (RelativeLayout) findViewById(R.id.result_lable);
        result_lable1 = (RelativeLayout) findViewById(R.id.result_lable1);
        card_bottom = (RelativeLayout) findViewById(R.id.card_bottom);

        // result_lable1.setVisibility(View.GONE);
        resultParent.removeView(result_lable1);

        result_lable.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(isCardShowing == true)
                    return false;
                return gd.onTouchEvent(event);
            }
        });

        result_lable1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(isCardShowing == false)
                    return false;
                return gd.onTouchEvent(event);
            }
        });

        // ////////////////////////////////////
        for(int i = 0; i < 5; i++) {
            if(number[i] == 1) {
                card_n[cnt++] = i;
            }
        }

        if(cnt == 0) {
            result_replay.setVisibility(View.INVISIBLE);
            result_send.setVisibility(View.INVISIBLE);
            TextView title = (TextView) findViewById(R.id.GameTitle);
            title.setText("COLLECTION");
            ImageView info1 = (ImageView) findViewById(R.id.GameInfo1);
            info1.setVisibility(View.INVISIBLE);
            TextView info2 = (TextView) findViewById(R.id.GameInfo2);
            info2.setVisibility(View.INVISIBLE);
            TextView info3 = (TextView) findViewById(R.id.GameInfo3);
            info3.setVisibility(View.INVISIBLE);
            card_bottom.setVisibility(View.INVISIBLE);
            result_end.setText("BACK");

            number = readGameData();
            for(int i = 0; i < 5; i++) {
                if(number[i] == 1) {
                    card_n[cnt++] = i;
                }
            }

            Intent intent = getIntent();
            pageIndex = intent.getExtras().getInt("item");
            Log.i("GameResult", "pageIndex:" + pageIndex);

            switch(pageIndex) {
            case 0:
                result_img.setBackgroundResource(result_Img[0]);
                break;
            case 1:
                result_img.setBackgroundResource(result_Img[1]);
                break;
            case 2:
                result_img.setBackgroundResource(result_Img[2]);
                break;
            case 3:
                result_img.setBackgroundResource(result_Img[3]);
                break;
            case 4:
                result_img.setBackgroundResource(result_Img[4]);
                break;
            }

            card_n[pageIndex] = pageIndex;
        } else { 
            result_replay.setVisibility(View.VISIBLE);
            result_send.setVisibility(View.VISIBLE);
            card_bottom.setVisibility(View.VISIBLE);
            int[] scores = readGameData();
            for(int i = 0; i < 5; i++) {
                if(number[i] == 1) {
                    scores[i] = 1;
                }
            }
            writeGameData(scores);
            result_img.setBackgroundResource(result_Img[card_n[pageIndex]]);
            result_number.setText((pageIndex + 1) + "/" + cnt);

        }
        // ////////////////////////////////////

        result_name.setText(flyname[card_n[pageIndex]]);
        result_info.setText(Infomation[card_n[pageIndex]]);

        scroll1.scrollTo(0, 0);
        scroll2.scrollTo(0, 0);

        setResult(RESULT_CANCELED);
    }

    /**
     * 저장된 게임 데이터 이력을 읽는다.
     * 
     * @return 게임 데이터
     */
    private int[] readGameData() {
        int[] scores = new int[5];
        try {
            FileInputStream fis = openFileInput("game_score.txt");
            DataInputStream in = new DataInputStream(fis);

            for(int i = 0; i < 5; i++) {
                scores[i] = in.readInt();
            }

            in.close();
            fis.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return scores;
    }

    /**
     * 게임 이력 데이터를 저장한다.
     * 
     * @param data 게임 데이터
     */
    private void writeGameData(int[] data) {
        try {
            FileOutputStream fos = openFileOutput("game_score.txt", MODE_PRIVATE);
            DataOutputStream out = new DataOutputStream(fos);

            for(int i = 0; i < 5; i++) {
                out.writeInt(data[i]);
            }

            out.flush();
            out.close();

            fos.flush();
            fos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.result_send: {
            final String MISSION_RESULT_ACTION = "com.skt.realedu.mission.RESULT";

            Intent intent = new Intent(MISSION_RESULT_ACTION);
            intent.putExtra("book", "172db6e4ea6744278f422dc72f23ea9c");
            intent.putExtra("id", "1");
            intent.putExtra("success", (boolean) true);
            intent.putExtra("score", cnt);
            sendBroadcast(intent);

            Toast.makeText(this, "Result sended.", Toast.LENGTH_LONG).show();
            break;
        }

        case R.id.result_replay: {
            setResult(RESULT_OK);
            finish();
            break;
        }

        case R.id.result_end: {

            for(int i = 0; i < 5; i++) {
                GamePlay.dragonfly_catch[i] = 0;
            }
            setResult(RESULT_CANCELED);
            finish();
            break;
        }

        case R.id.card_btn01: {
            changeToCard();
            break;
        }

        case R.id.card_btn02: {
            changeToback();
            break;
        }

        case R.id.card_arrow01: {
            changeToback();
        }

            pageIndex--;
            if(pageIndex < 0)
                pageIndex += cnt;
            {
                result_img.setBackgroundResource(result_Img[card_n[pageIndex]]);
                result_name.setText(flyname[card_n[pageIndex]]);
                result_info.setText(Infomation[card_n[pageIndex]]);
                scroll1.scrollTo(0, 0);
                scroll2.scrollTo(0, 0);

                result_number.setText((pageIndex + 1) + "/" + cnt);
                break;
            }

        case R.id.card_arrow02: {
            changeToback();
        }

            pageIndex = (pageIndex + 1) % cnt;
            result_img.setBackgroundResource(result_Img[card_n[pageIndex]]);
            result_name.setText(flyname[card_n[pageIndex]]);
            result_info.setText(Infomation[card_n[pageIndex]]);
            scroll1.scrollTo(0, 0);
            scroll2.scrollTo(0, 0);

            result_number.setText((pageIndex + 1) + "/" + cnt);

            break;

        }
    }

    /**
     * 게임 결과 반영
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_CANCELED) {
            finish();
        }
    }

    /**
     * 카드를 앞으로 뒤집는다.
     */
    public void changeToCard() {
        if(isCardShowing)
            return;
        isCardShowing = true;

        final float centerX = 620 / 2.0f;
        final float centerY = 340 / 2.0f;

        final Flip3dAnimation rotation = new Flip3dAnimation(0, -90, centerX, centerY);
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {

                resultParent.removeView(result_lable);
                resultParent.addView(result_lable1);

                Flip3dAnimation rotation = new Flip3dAnimation(90, 0, centerX, centerY);

                rotation.setDuration(500);
                rotation.setFillAfter(true);
                rotation.setInterpolator(new DecelerateInterpolator());

                result_lable1.startAnimation(rotation);
            }
        });
        result_lable.startAnimation(rotation);

        card_flyname.setText(flyname[card_n[pageIndex]]);
        result_cardinfo.setText(card_Infomation[pageIndex]);
        scroll1.scrollTo(0, 0);
        scroll2.scrollTo(0, 0);
    }

    /**
     * 카드를 뒤로 뒤집는다.
     */
    public void changeToback() {
        if(!isCardShowing)
            return;
        isCardShowing = false;

        final float centerX = 620 / 2.0f;
        final float centerY = 340 / 2.0f;

        final Flip3dAnimation rotation = new Flip3dAnimation(0, 90, centerX, centerY);
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {

                resultParent.removeView(result_lable1);
                resultParent.addView(result_lable);

                Flip3dAnimation rotation = new Flip3dAnimation(-90, 0, centerX, centerY);

                rotation.setDuration(500);
                rotation.setFillAfter(true);
                rotation.setInterpolator(new DecelerateInterpolator());

                result_lable.startAnimation(rotation);
            }
        });
        result_lable1.startAnimation(rotation);

    }
}
