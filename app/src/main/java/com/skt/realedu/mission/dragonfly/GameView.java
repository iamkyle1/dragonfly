package com.skt.realedu.mission.dragonfly;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.View;

/**
 * 게임 화면 뷰
 */
public class GameView extends View {
    SurfaceHolder holder;
    boolean bPaintable = true;

    // Renderer Interface
    Render renderer;

    /**
     * 화면 갱신 랜더러 인터페이스
     */
    public interface Render {
        public void preparePaint();

        public void onPaint(Canvas c);
    }

    /**
     * 랜더러 등록
     * 
     * @param render
     */
    public void setRender(Render render) {
        renderer = render;
    }

    /**
     * 랜더러를 반환한다.
     * 
     * @return 랜더러
     */
    protected Render getRender() {
        return renderer;
    }

    // /

    public GameView(Context context) {
        super(context);
    }

    /**
     * 화면 갱신을 멈춘다.
     */
    public void close() {
        bPaintable = false;
    }

    /**
     * 화면 갱신 간격을 기본값으로 지정한다.
     */
    public void setRefreshTimeGap() {
        setRefreshTimeGap(100); // default
    }

    /**
     * 화면 갱신 간격을 지정한다.
     * 
     * @param gap 갱신간격
     */
    public void setRefreshTimeGap(int gap) {
        REFRESH_TIME_GAP = gap;
    }

    int REFRESH_TIME_GAP = 30;

    /**
     * 화면 그리기
     * 
     * @param canvas 캔버스
     */
    protected void onDraw(Canvas canvas) {
        if(renderer != null) {

            renderer.preparePaint();
            renderer.onPaint(canvas);
        }

        postDelayed(new Runnable() {
            public void run() {
                invalidate();
            }
        }, REFRESH_TIME_GAP);
    }
}
