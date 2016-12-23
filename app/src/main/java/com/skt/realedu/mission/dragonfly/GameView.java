package com.skt.realedu.mission.dragonfly;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.View;

/**
 * ���� ȭ�� ��
 */
public class GameView extends View {
    SurfaceHolder holder;
    boolean bPaintable = true;

    // Renderer Interface
    Render renderer;

    /**
     * ȭ�� ���� ������ �������̽�
     */
    public interface Render {
        public void preparePaint();

        public void onPaint(Canvas c);
    }

    /**
     * ������ ���
     * 
     * @param render
     */
    public void setRender(Render render) {
        renderer = render;
    }

    /**
     * �������� ��ȯ�Ѵ�.
     * 
     * @return ������
     */
    protected Render getRender() {
        return renderer;
    }

    // /

    public GameView(Context context) {
        super(context);
    }

    /**
     * ȭ�� ������ �����.
     */
    public void close() {
        bPaintable = false;
    }

    /**
     * ȭ�� ���� ������ �⺻������ �����Ѵ�.
     */
    public void setRefreshTimeGap() {
        setRefreshTimeGap(100); // default
    }

    /**
     * ȭ�� ���� ������ �����Ѵ�.
     * 
     * @param gap ���Ű���
     */
    public void setRefreshTimeGap(int gap) {
        REFRESH_TIME_GAP = gap;
    }

    int REFRESH_TIME_GAP = 30;

    /**
     * ȭ�� �׸���
     * 
     * @param canvas ĵ����
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
