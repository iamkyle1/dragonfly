package com.skt.realedu.mission.dragonfly;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * ���ڸ� Ŭ����
 */
public class Dragonfly extends Mover {

    Bitmap image[][], locator;
    int imgIndex;
    int width, height, width2, height2;
    int locWidth, locHeight;
    float aX, aY; // �����̴� �Ÿ�
    long fTime, lastTime;
    boolean bCatched = false;

    public Dragonfly(float x, float y, int type) {
        super(x, y, type);
        imgIndex = (int) (System.currentTimeMillis() & 1);

        randomizeDirection();
    }

    /**
     * ���ڸ� �̹����� �����Ѵ�.
     * 
     * @param img �̹���
     * @param loc ���̴� ������ �̹���
     */
    public void setImage(Bitmap[][] img, Bitmap loc) {
        image = img;
        width = img[0][0].getWidth();
        height = img[0][0].getHeight();
        width2 = width / 2;
        height2 = height / 2;
        locator = loc;
        locWidth = locator.getWidth();
        locHeight = locator.getHeight();
    }

    /**
     * ���ڸ��� ���� ũ�⸦ ��´�.
     * 
     * @return ���ڸ� ũ��
     */
    public int getWidth() {
        return width;
    }

    /**
     * ���ڸ��� ���� ũ�⸦ ��´�.
     * 
     * @return ���ڸ� ũ��
     */
    public int getWidth2() {
        return width2;
    }

    /**
     * ���ڸ��� ���� ũ�⸦ ��´�.
     * 
     * @return ���ڸ� ũ��
     */
    public int getHeight() {
        return height;
    }

    /**
     * ���ڸ��� ���� ũ�⸦ ��´�.
     * 
     * @return ���ڸ� ũ��
     */
    public int getHeight2() {
        return height2;
    }

    /**
     * ������ �������� �����Ѵ�.
     */
    private void randomizeDirection() {
        do {
            aX = (float) (Math.random() * 2 - 1);
        } while(aX == 0);
        aY = (float) (Math.random() * 0.4 - 0.2);
        fTime = (long) (Math.random() * 2000 + 1000);
        lastTime = System.currentTimeMillis();
    }

    /**
     * �������� ���θ� �����Ѵ�.
     * 
     * @param b ��������
     */
    public void setCatched(boolean b) {
        bCatched = b;
    }

    /**
     * ������ �������� ���ڸ��� �����δ�.
     */
    public void move() {
        if(!bCatched) {
            if(System.currentTimeMillis() > lastTime + fTime) {
                randomizeDirection();
            }

            oriX += aX;
            oriY += aY;
            if(oriY < -45 || oriY > 45)
                aY = -aY;
            if(oriX < 0)
                oriX += 360;
            if(oriX > 360)
                oriX -= 360;
        }
    }

    /**
     * ���ڸ��� �׸���.
     */
    public void draw(Canvas c, Paint p) {
        int screenW = 800; // c.getWidth();
        int screenH = 480;// -70; //c.getHeight();

        int x = getPosX();
        int y = getPosY();
        if(x < -width || y < -height || x > screenW || y > screenH) {
            if(x < 0)
                x = 0;
            else if(x > screenW)
                x = screenW - locWidth;
            else
                x += width2;
            if(y < 0)
                y = 0;
            else if(y > screenH)
                y = screenH - locHeight;
            else
                y += height2;
            c.drawBitmap(locator, x, y, p);
        } else {

            if(aX < 0) {
                c.drawBitmap(image[type][imgIndex], x, y, p);
            }

            else {
                c.scale(-1, 1, posX + width / 2, posY + height / 2);
                c.drawBitmap(image[type][imgIndex], x, y, p);
                c.scale(-1, 1, posX + width / 2, posY + height / 2);
            }

            imgIndex = 1 - imgIndex;
        }
    }
}
