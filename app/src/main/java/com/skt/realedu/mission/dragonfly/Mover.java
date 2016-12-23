package com.skt.realedu.mission.dragonfly;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 움직이는 모든 객체의 부모 클래스
 */
public class Mover {

    float oriX, oriY;
    float posX, posY;
    int type;

    public Mover() {
        this(0, 0, 0);
    }

    public Mover(float x, float y, int type1) {
        oriX = x;
        oriY = y;
        type = type1;
    }

    /**
     * 객체의 가로 좌표
     * 
     * @return 가로좌표
     */
    public float getX() {
        return oriX;
    }

    /**
     * 객체의 세로 좌표
     * 
     * @return 세로 좌표
     */
    public float getY() {
        return oriY;
    }

    /**
     * 객체의 위치를 지정한다.
     * 
     * @param x 가로좌표
     * @param y 세로좌표
     */
    public void setPosition(float x, float y) {
        posX = x;
        posY = y;
    }

    /**
     * 타입을 반환한다.
     * 
     * @return 타입
     */
    public int getType() {
        return type;
    }

    /**
     * 타입을 지정한다.
     * 
     * @param type 타입
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 객체의 절대 가로 좌표를 반환한다.
     * 
     * @return 가로좌표
     */
    public int getPosX() {
        return (int) posX;
    }

    /**
     * 객체의 절대 세로 좌표를 반환한다.
     * 
     * @return 세로좌표
     */
    public int getPosY() {
        return (int) posY;
    }

    /**
     * 객체를 그린다.
     * 
     * @param c 캔버스
     * @param p 페인트
     */
    public void draw(Canvas c, Paint p) {
    }
}
