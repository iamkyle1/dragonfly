package com.skt.realedu.mission.dragonfly;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 게임 화면
 */
public class GamePlay extends Activity implements GameView.Render {

    private final int GAME_STATE_PLAY = 0;
    private final int GAME_STATE_PAUSE = 1;
    private final int GAME_STATE_SUCCESS = 2;
    private final int GAME_STATE_FAIL = 3;
    private final int GAME_STATE_OVER = 4;
    private final int GAME_STATE_CONTINUE = 5;
    private final int GAME_STATE_INTRO = 6;

    CameraPreview cameraView = null;
    GameView gameView = null;
    SensorManager sensorManager;
    SensorEventHandler sensorHandler;
    int nEatCount = 0, gameState = -1;
    TextView game_number, gameTime;
    public SoundPool sndEffect;
    public int sndShot, sndSuccess, sound_time_limit, sndGameOver, sndFail, sndEat;
    public MediaPlayer bgm;
    Vibrator vibe;
    TextView pause_continue;
    ImageButton pauseButton;
    Resources res = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.gameplay);

        game_number = (TextView) findViewById(R.id.game_number);
        gameTime = (TextView) findViewById(R.id.game_time);

        FrameLayout background = (FrameLayout) findViewById(R.id.background);

        cameraView = new CameraPreview(this);
        background.addView(cameraView);

        gameView = new GameView(this);
        gameView.setRender(this);
        background.addView(gameView);

        pauseButton = (ImageButton) findViewById(R.id.game_top_btn);
        pauseButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(gameState == GAME_STATE_PLAY) {
                    changeState(GAME_STATE_PAUSE);
                }
            }
        });

        pause_continue = (TextView) findViewById(R.id.pause_continue);
        pause_continue.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                changeState(GAME_STATE_CONTINUE);
            }
        });

        TextView pause_replay = (TextView) findViewById(R.id.pause_replay);
        pause_replay.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                init();
            }
        });

        TextView pause_help = (TextView) findViewById(R.id.pause_help);
        pause_help.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GamePlay.this, GameHelp.class);
                startActivity(intent);
            }
        });

        TextView pause_end = (TextView) findViewById(R.id.pause_end);
        pause_end.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        TextView GameOver_help = (TextView) findViewById(R.id.GameOver_help);
        GameOver_help.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(GamePlay.this, GameHelp.class);
                startActivity(intent);
            }
        });

        TextView GameOver_end = (TextView) findViewById(R.id.GameOver_end);
        GameOver_end.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        TextView GameOver_replay = (TextView) findViewById(R.id.GameOver_replay);
        GameOver_replay.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                init();
            }
        });

        res = getResources();

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sensorHandler = new SensorEventHandler();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(sensorHandler, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(sensorHandler, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(sensorHandler, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);

        init();
    }

    protected void onDestroy() {
        super.onDestroy();
        destoryImages();
        sensorManager.unregisterListener(sensorHandler);
        if(bgm != null)
            bgm.stop();
        gameView.close();
    }

    // ////////////////////////
    /**
     * 센서 처리 클래스
     */
    class SensorEventHandler implements SensorEventListener {

        public float sensorHead, sensorRoll, sensorMagY, sensorAccZ;
        private long lastTime;
        private float speed, lastX, lastY, lastZ;
        private Averages heads = new Averages(5);
        private Averages rolls = new Averages(5);

        private static final int SHAKE_THRESHOLD = 800;

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                sensorHead = event.values[0];
                sensorRoll = event.values[2];
            } else if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                sensorAccZ = event.values[2];

                long currentTime = System.currentTimeMillis();
                long gabOfTime = (currentTime - lastTime);
                if(gabOfTime > 100) {
                    lastTime = currentTime;
                    float x, y, z;

                    x = event.values[0];
                    y = event.values[1];
                    z = event.values[2];

                    speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                    if(speed > SHAKE_THRESHOLD) {
                        // shaking event !
                        shot();
                    }
                    lastX = x;
                    lastY = y;
                    lastZ = z;
                }
            } else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                sensorMagY = event.values[1];
            }
        }

        // 상하 보정된 방향값(0 ~ 360)
        /**
         * 현재 방향을 반환한다. (0~360)
         */
        public float getHead() {
            float h = sensorHead + sensorMagY * 2;
            while(h < 0)
                h += 360;
            while(h > 360)
                h -= 360;
            return heads.addValue(h);
        }

        // 값 범위 보정된 기울임 값(-90~90)
        /**
         * 현재 기울임 값을 반환한다 (-90~90)
         */
        public float getRoll() {
            float r = sensorRoll - 90; // 정면으로 본것을 0으로 함
            if(sensorAccZ > 0)
                return rolls.addValue(r); // 아래로 내려보면 -90 까지 떨어진다.
            return rolls.addValue(-r); // 위로 올려보면 90까지 올라간다.
        }
    }

    // ////////////////////////
    final int PIXEL_PER_DEGREE_X = 8;
    final int PIXEL_PER_DEGREE_Y = 10;
    Paint paint = new Paint();

    ArrayList<Dragonfly> fly;
    Frog frog;
    Bitmap imgSuccess, imgFail, imgOver, imgReady, imgGo;
    Bitmap imgFrogBack;
    Bitmap top_counter[] = new Bitmap[10];
    Bitmap flyImg[][] = new Bitmap[5][2];
    Bitmap locater;
    public static int dragonfly_catch[] = new int[5];
    public static float playTime = 60 * 1000;
    long startTime;

    /**
     * 게임 데이터 초기화
     */
    protected void init() {
        final int numOfFly = 16;

        fly = new ArrayList<Dragonfly>();

        for(int i = 0; i < 5; i++) {
            dragonfly_catch[i] = 0;
        }

        if(flyImg[0][0] == null)
            flyImg[0][0] = BitmapFactory.decodeResource(res, R.drawable.dragonfly_a_01);
        if(flyImg[0][1] == null)
            flyImg[0][1] = BitmapFactory.decodeResource(res, R.drawable.dragonfly_a_02);
        if(flyImg[1][0] == null)
            flyImg[1][0] = BitmapFactory.decodeResource(res, R.drawable.dragonfly_b_01);
        if(flyImg[1][1] == null)
            flyImg[1][1] = BitmapFactory.decodeResource(res, R.drawable.dragonfly_b_02);
        if(flyImg[2][0] == null)
            flyImg[2][0] = BitmapFactory.decodeResource(res, R.drawable.dragonfly_c_01);
        if(flyImg[2][1] == null)
            flyImg[2][1] = BitmapFactory.decodeResource(res, R.drawable.dragonfly_c_02);
        if(flyImg[3][0] == null)
            flyImg[3][0] = BitmapFactory.decodeResource(res, R.drawable.dragonfly_d_01);
        if(flyImg[3][1] == null)
            flyImg[3][1] = BitmapFactory.decodeResource(res, R.drawable.dragonfly_d_02);
        if(flyImg[4][0] == null)
            flyImg[4][0] = BitmapFactory.decodeResource(res, R.drawable.dragonfly_e_01);
        if(flyImg[4][1] == null)
            flyImg[4][1] = BitmapFactory.decodeResource(res, R.drawable.dragonfly_e_02);

        if(top_counter[0] == null)
            top_counter[0] = BitmapFactory.decodeResource(res, R.drawable.top_counter01);
        if(top_counter[1] == null)
            top_counter[1] = BitmapFactory.decodeResource(res, R.drawable.top_counter02);
        if(top_counter[2] == null)
            top_counter[2] = BitmapFactory.decodeResource(res, R.drawable.top_counter03);
        if(top_counter[3] == null)
            top_counter[3] = BitmapFactory.decodeResource(res, R.drawable.top_counter04);
        if(top_counter[4] == null)
            top_counter[4] = BitmapFactory.decodeResource(res, R.drawable.top_counter05);
        if(top_counter[5] == null)
            top_counter[5] = BitmapFactory.decodeResource(res, R.drawable.top_counter06);
        if(top_counter[6] == null)
            top_counter[6] = BitmapFactory.decodeResource(res, R.drawable.top_counter07);
        if(top_counter[7] == null)
            top_counter[7] = BitmapFactory.decodeResource(res, R.drawable.top_counter08);
        if(top_counter[8] == null)
            top_counter[8] = BitmapFactory.decodeResource(res, R.drawable.top_counter09);
        if(top_counter[9] == null)
            top_counter[9] = BitmapFactory.decodeResource(res, R.drawable.top_counter10);

        if(locater == null)
            locater = BitmapFactory.decodeResource(res, R.drawable.game_locater);

        for(int i = 0; i < numOfFly; i++) {
            float randX = (float) (Math.random() * 360);
            float randY = (float) (Math.random() * 90 - 45);
            int type = (int) (Math.random() * 100);
            if(type < 4)
                type = 0; // 4%
            else if(type < 4 + 30)
                type = 1; // 30%
            else if(type < 4 + 30 + 5)
                type = 2; // 5%
            else if(type < 4 + 30 + 5 + 30)
                type = 3; // 30%
            else
                type = 4; // 그외

            Dragonfly f = new Dragonfly(randX, randY, type);
            f.setImage(flyImg, locater);
            fly.add(f);
        }

        if(frog == null) {
            Bitmap frogM_Img[] = { BitmapFactory.decodeResource(res, R.drawable.frog_hold01), BitmapFactory.decodeResource(res, R.drawable.frog_hold02), BitmapFactory.decodeResource(res, R.drawable.frog_hold03) };

            Bitmap shoot_Img[] = { BitmapFactory.decodeResource(res, R.drawable.frog01), BitmapFactory.decodeResource(res, R.drawable.frog02), BitmapFactory.decodeResource(res, R.drawable.frog03), BitmapFactory.decodeResource(res, R.drawable.frog04), BitmapFactory.decodeResource(res, R.drawable.frog05), BitmapFactory.decodeResource(res, R.drawable.frog06), BitmapFactory.decodeResource(res, R.drawable.frog07), BitmapFactory.decodeResource(res, R.drawable.frog08), BitmapFactory.decodeResource(res, R.drawable.frog09) };

            Bitmap tongue_Img[] = { BitmapFactory.decodeResource(res, R.drawable.frog_tongue01), BitmapFactory.decodeResource(res, R.drawable.frog_tongue02), BitmapFactory.decodeResource(res, R.drawable.frog_tongue03), BitmapFactory.decodeResource(res, R.drawable.frog_tongue04), BitmapFactory.decodeResource(res, R.drawable.frog_tongue05) };

            Bitmap frogC_Img[] = { BitmapFactory.decodeResource(res, R.drawable.dragonfly_a_catch), BitmapFactory.decodeResource(res, R.drawable.dragonfly_b_catch), BitmapFactory.decodeResource(res, R.drawable.dragonfly_c_catch), BitmapFactory.decodeResource(res, R.drawable.dragonfly_d_catch), BitmapFactory.decodeResource(res, R.drawable.dragonfly_e_catch) };

            frog = new Frog();
            frog.setImage(frogM_Img, shoot_Img, tongue_Img, frogC_Img);
        }

        if(imgSuccess == null)
            imgSuccess = BitmapFactory.decodeResource(res, R.drawable.top_success);
        if(imgFail == null)
            imgFail = BitmapFactory.decodeResource(res, R.drawable.top_fail);
        if(imgOver == null)
            imgOver = BitmapFactory.decodeResource(res, R.drawable.top_gameover);
        if(imgReady == null)
            imgReady = BitmapFactory.decodeResource(res, R.drawable.top_ready);
        if(imgGo == null)
            imgGo = BitmapFactory.decodeResource(res, R.drawable.top_go);
        if(imgFrogBack == null)
            imgFrogBack = BitmapFactory.decodeResource(res, R.drawable.frog_back);

        if(sndEffect == null) {
            sndEffect = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            Context cnt = getApplicationContext();
            sndShot = sndEffect.load(cnt, R.raw.shot, 0);
            sndFail = sndEffect.load(cnt, R.raw.fail, 0);
            sndGameOver = sndEffect.load(cnt, R.raw.game_over, 0);
            sndSuccess = sndEffect.load(cnt, R.raw.success, 0);
            sndEat = sndEffect.load(cnt, R.raw.eat, 0);
        }

        changeState(GAME_STATE_INTRO);
    }

    /**
     * 게임에서 사용된 이미지의 메모리 해제
     */
    protected void destoryImages() {
        if(imgSuccess != null)
            imgSuccess.recycle();
        imgSuccess = null;
        if(imgFail != null)
            imgFail.recycle();
        imgFail = null;
        if(imgOver != null)
            imgOver.recycle();
        imgOver = null;
        if(imgReady != null)
            imgReady.recycle();
        imgReady = null;
        if(imgGo != null)
            imgGo.recycle();
        imgGo = null;
        if(imgFrogBack != null)
            imgFrogBack.recycle();
        imgFrogBack = null;
        for(int i = 0; i < top_counter.length; i++) {
            if(top_counter[i] != null)
                top_counter[i].recycle();
            top_counter[i] = null;
        }
        if(frog != null)
            frog.destroyImages();
        for(int i = 0; i < flyImg.length; i++) {
            if(flyImg[i][0] != null)
                flyImg[i][0].recycle();
            flyImg[i][0] = null;
            if(flyImg[i][1] != null)
                flyImg[i][1].recycle();
            flyImg[i][1] = null;
        }
        if(locater != null)
            locater.recycle();
        locater = null;
    }

    int introFrame = 0;
    Bitmap introImage = null;
    final int introFrames[] = { R.drawable.frog_movie001, R.drawable.frog_movie002, R.drawable.frog_movie003, R.drawable.frog_movie004, R.drawable.frog_movie005, R.drawable.frog_movie006, R.drawable.frog_movie007, R.drawable.frog_movie008, R.drawable.frog_movie009, R.drawable.frog_movie010, R.drawable.frog_movie011, R.drawable.frog_movie012, R.drawable.frog_movie013, R.drawable.frog_movie014, R.drawable.frog_movie015, R.drawable.frog_movie016, R.drawable.frog_movie017, R.drawable.frog_movie018, R.drawable.frog_movie019, R.drawable.frog_movie020, R.drawable.frog_movie021, R.drawable.frog_movie022, R.drawable.frog_movie023, R.drawable.frog_movie024, R.drawable.frog_movie025, R.drawable.frog_movie026, R.drawable.frog_movie027, R.drawable.frog_movie028, R.drawable.frog_movie029, R.drawable.frog_movie030, R.drawable.frog_movie031, R.drawable.frog_movie032, R.drawable.frog_movie033, R.drawable.frog_movie034, R.drawable.frog_movie035, R.drawable.frog_movie036, R.drawable.frog_movie037, R.drawable.frog_movie038,
            R.drawable.frog_movie039, R.drawable.frog_movie040, R.drawable.frog_movie041, R.drawable.frog_movie042, R.drawable.frog_movie043, R.drawable.frog_movie044, R.drawable.frog_movie045, R.drawable.frog_movie046, R.drawable.frog_movie047, R.drawable.frog_movie048, R.drawable.frog_movie049, R.drawable.frog_movie050, R.drawable.frog_movie051, R.drawable.frog_movie052, R.drawable.frog_movie053, R.drawable.frog_movie054, R.drawable.frog_movie055, R.drawable.frog_movie056, R.drawable.frog_movie057, R.drawable.frog_movie058, R.drawable.frog_movie059 };

    /**
     * 게임의 매 프레임마다 그릴 데이터를 갱신한다.
     */
    public void preparePaint() {
        if(gameState == GAME_STATE_INTRO) {
            if(introImage != null) {
                introImage.recycle();
                introImage = null;
            }
            introImage = BitmapFactory.decodeResource(res, introFrames[introFrame]);
            introFrame++;
            if(introFrame >= introFrames.length) {
                changeState(GAME_STATE_PLAY);
            }
        } else if(gameState == GAME_STATE_PLAY) {
            int centerX = 800 / 2; // gameView.getWidth() /2;
            int centerY = (480 - 70) / 2; // gameView.getHeight() /2;
            float h = sensorHandler.getHead();
            float r = sensorHandler.getRoll();

            int size = fly.size();
            for(int i = 0; i < size; i++) {
                Dragonfly f = fly.get(i);

                f.move();

                float viewX = f.getX() - h;
                if(viewX > 180)
                    viewX = viewX - 360;
                float x = centerX + viewX * PIXEL_PER_DEGREE_X;

                float viewY = f.getY() - r;
                float y = centerY - viewY * PIXEL_PER_DEGREE_Y;

                f.setPosition(x, y);
            }

            frog.move();

        }
    }

    /**
     * 각 프레임을 그린다.
     * 
     * @param c 캔버스
     */
    public void onPaint(Canvas c) {
        long now = System.currentTimeMillis();

        if(gameState != GAME_STATE_INTRO) {
            // draw dragonfly
            int size = fly.size();
            for(int i = 0; i < size; i++) {
                Dragonfly f = fly.get(i);
                f.draw(c, paint);
            }
            // frog back
            c.drawBitmap(imgFrogBack, 0, 0, paint);
            // draw frog
            frog.draw(c, paint);
        }

        if(gameState == GAME_STATE_INTRO) {
            playTime -= (now - startTime);
            startTime = now;

            Rect src = new Rect();
            src.set(0, 0, introImage.getWidth(), introImage.getHeight());
            Rect dst = new Rect();
            dst.set(0, 0, gameView.getWidth(), gameView.getHeight());
            c.drawBitmap(introImage, src, dst, paint);

            int centiSec = (int) ((playTime / 100) % 10);
            if(centiSec > 4) {
                c.drawBitmap(imgReady, 237, 95, paint);
            }
        } else if(gameState == GAME_STATE_PLAY) {
            // time
            playTime -= (now - startTime);
            startTime = now;
            if(playTime < 0) {
                playTime = 0;
                changeState(GAME_STATE_FAIL);
            }
            gameTime.setText(String.format("%2.2f", playTime / 1000));

            // time limit
            if(playTime < 10 * 1000 && playTime > 0) {
                int sec = (int) ((playTime / 1000) % 10);
                int centiSec = (int) ((playTime / 100) % 10);
                c.drawBitmap(top_counter[sec], 369, 224, paint);

                if((sec == 9 || sec == 4) && centiSec >= 7) {
                    int oldColor = paint.getColor();
                    paint.setColor(0x40FF0000);
                    c.drawRect(0, 0, gameView.getWidth(), gameView.getHeight(), paint);
                    paint.setColor(oldColor);
                }
            }

            if(playTime > 59 * 1000) {
                c.drawBitmap(imgGo, 237, 95, paint);
            }
        } else if(gameState == GAME_STATE_SUCCESS) {
            c.drawBitmap(imgSuccess, 237, 219, paint);
        } else if(gameState == GAME_STATE_FAIL) {
            c.drawBitmap(imgFail, 237, 95, paint);
        } else if(gameState == GAME_STATE_OVER) {
            c.drawBitmap(imgOver, 237, 219, paint);
        }
    }

    /**
     * 게임 상태를 변경한다.
     * 
     * @param state 상태값
     */
    public void changeState(int state) {
        if(gameState == state)
            return;
        gameState = state;

        if(gameState == GAME_STATE_INTRO) {

            bgm = MediaPlayer.create(this, R.raw.intro_bg);
            bgm.start();
            bgm.setLooping(false);

            gameView.setRefreshTimeGap(10);
            introFrame = 0;
            playTime = 60 * 1000;
            startTime = System.currentTimeMillis();

            RelativeLayout panel = (RelativeLayout) findViewById(R.id.pause_panel);
            panel.setVisibility(View.INVISIBLE);

            RelativeLayout panel2 = (RelativeLayout) findViewById(R.id.GameOver_panel);
            panel2.setVisibility(View.INVISIBLE);
        } else {
            gameView.setRefreshTimeGap(); // set to default
            if(introImage != null)
                introImage.recycle();
            introImage = null;
        }

        if(gameState != GAME_STATE_PAUSE) {
            pauseButton.setClickable(true);
        }

        if(gameState == GAME_STATE_PLAY) {

            if(bgm != null)
                bgm.stop();
            bgm = MediaPlayer.create(this, R.raw.background);
            bgm.start();
            bgm.setLooping(true);

            nEatCount = 0;
            game_number.setText("X " + nEatCount);

            playTime = 60 * 1000;
            startTime = System.currentTimeMillis();

            RelativeLayout panel = (RelativeLayout) findViewById(R.id.pause_panel);
            panel.setVisibility(View.INVISIBLE);

            RelativeLayout panel2 = (RelativeLayout) findViewById(R.id.GameOver_panel);
            panel2.setVisibility(View.INVISIBLE);

        } else if(gameState == GAME_STATE_SUCCESS) {
            bgm.stop();
            sndEffect.play(sndSuccess, 1, 1, 0, 0, 1);

            Intent intent = new Intent(this, GameResult.class);
            startActivityForResult(intent, 100);

        } else if(gameState == GAME_STATE_FAIL) {

            sndEffect.play(sndFail, 1, 1, 0, 0, 1);
            bgm.pause();
            RelativeLayout panel = (RelativeLayout) findViewById(R.id.GameOver_panel);
            panel.setVisibility(View.VISIBLE);
        } else if(gameState == GAME_STATE_PAUSE) {
            bgm.pause();
            RelativeLayout panel = (RelativeLayout) findViewById(R.id.pause_panel);
            panel.setVisibility(View.VISIBLE);
            pause_continue.setVisibility(View.VISIBLE);
            pauseButton.setClickable(false);
        } else if(gameState == GAME_STATE_OVER) {
            bgm.stop();
            sndEffect.play(sndGameOver, 1, 1, 0, 0, 1);
        } else if(gameState == GAME_STATE_CONTINUE) {
            gameState = GAME_STATE_PLAY;

            bgm.start();
            startTime = System.currentTimeMillis();

            RelativeLayout panel = (RelativeLayout) findViewById(R.id.pause_panel);
            panel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 게임 실행 결과를 반영한다.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK) {
            init();
        } else if(requestCode == 100 && resultCode == RESULT_CANCELED) {
            finish();
        }
    }

    boolean bFrogTouched = false;
    boolean bTouchLock = false;

    /**
     * 터치 이벤트 처리
     */
    public boolean onTouchEvent(MotionEvent event) {
        if(bTouchLock == true)
            return false;
        if(gameState == GAME_STATE_PLAY) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                if(event.getX() > 567 && event.getY() > 325)
                    bFrogTouched = true;
            } else if(event.getAction() == MotionEvent.ACTION_UP) {
                if(bFrogTouched && event.getX() > 567 && event.getY() > 325) {
                    shot();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 잠자리 잡기 데이터 처리
     */
    public void shot() {
        if(gameState != GAME_STATE_PLAY)
            return;
        if(frog.shot() == false)
            return;

        sndEffect.play(sndShot, 1, 1, 0, 0, 1);

        frog.setType(-1, -1);
        int size = fly.size();
        for(int i = size - 1; i >= 0; i--) {
            Dragonfly f = fly.get(i);
            float fx = f.getPosX();
            float fy = f.getPosY();
            int type;

            int catched = frog.isCatched((int) fx, (int) fy, f.getWidth(), f.getHeight());
            if(catched != -1) {
                type = f.getType();
                f.setCatched(true); // 잠자리 그자리에 멈추기(먹어야 되니까)
                fly.remove(i); // 음.. 바로 없어지면 안되는데..

                // 먹은 효과 처리
                gameView.postDelayed(eatEffect, 400);
                vibe.vibrate(800);
                dragonfly_catch[type] = 1;

                frog.setType(type, catched);
                break; // 한번에 한마리씩 잡는다.
            }
        }
    }

    /**
     * 잠자리 먹는 효과 처리
     */
    private Runnable eatEffect = new Runnable() {
        public void run() {
            sndEffect.play(sndEat, 1, 1, 0, 0, 1);
            nEatCount++;
            game_number.setText("X " + nEatCount);

            if(nEatCount >= 10) {
                changeState(GAME_STATE_SUCCESS);
            }
        }
    };
}