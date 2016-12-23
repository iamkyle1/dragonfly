package com.skt.realedu.mission.dragonfly;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * �����̴� ��� ��ü�� �θ� Ŭ����
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
     * ��ü�� ���� ��ǥ
     * 
     * @return ������ǥ
     */
    public float getX() {
        return oriX;
    }

    /**
     * ��ü�� ���� ��ǥ
     * 
     * @return ���� ��ǥ
     */
    public float getY() {
        return oriY;
    }

    /**
     * ��ü�� ��ġ�� �����Ѵ�.
     * 
     * @param x ������ǥ
     * @param y ������ǥ
     */
    public void setPosition(float x, float y) {
        posX = x;
        posY = y;
    }

    /**
     * Ÿ���� ��ȯ�Ѵ�.
     * 
     * @return Ÿ��
     */
    public int getType() {
        return type;
    }

    /**
     * Ÿ���� �����Ѵ�.
     * 
     * @param type Ÿ��
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * ��ü�� ���� ���� ��ǥ�� ��ȯ�Ѵ�.
     * 
     * @return ������ǥ
     */
    public int getPosX() {
        return (int) posX;
    }

    /**
     * ��ü�� ���� ���� ��ǥ�� ��ȯ�Ѵ�.
     * 
     * @return ������ǥ
     */
    public int getPosY() {
        return (int) posY;
    }

    /**
     * ��ü�� �׸���.
     * 
     * @param c ĵ����
     * @param p ����Ʈ
     */
    public void draw(Canvas c, Paint p) {
    }
}
