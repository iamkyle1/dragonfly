package com.skt.realedu.mission.dragonfly;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 잠자리 클래스
 */
public class Dragonfly extends Mover {

    Bitmap image[][], locator;
    int imgIndex;
    int width, height, width2, height2;
    int locWidth, locHeight;
    float aX, aY; // 움직이는 거리
    long fTime, lastTime;
    boolean bCatched = false;

    public Dragonfly(float x, float y, int type) {
        super(x, y, type);
        imgIndex = (int) (System.currentTimeMillis() & 1);

        randomizeDirection();
    }

    /**
     * 잠자리 이미지를 설정한다.
     * 
     * @param img 이미지
     * @param loc 레이더 포인터 이미지
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
     * 잠자리의 가로 크기를 얻는다.
     * 
     * @return 잠자리 크기
     */
    public int getWidth() {
        return width;
    }

    /**
     * 잠자리의 가로 크기를 얻는다.
     * 
     * @return 잠자리 크기
     */
    public int getWidth2() {
        return width2;
    }

    /**
     * 잠자리의 세로 크기를 얻는다.
     * 
     * @return 잠자리 크기
     */
    public int getHeight() {
        return height;
    }

    /**
     * 잠자리의 세로 크기를 얻는다.
     * 
     * @return 잠자리 크기
     */
    public int getHeight2() {
        return height2;
    }

    /**
     * 방향을 무작위로 설정한다.
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
     * 잡혔는지 여부를 설정한다.
     * 
     * @param b 잡힌여부
     */
    public void setCatched(boolean b) {
        bCatched = b;
    }

    /**
     * 정해진 방향으로 잠자리를 움직인다.
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
     * 잠자리를 그린다.
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
