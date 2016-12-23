package com.skt.realedu.mission.dragonfly;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * ������ ó�� Ŭ����
 */
public class Frog {

    Bitmap img[], tongue[], shot[], dragonfly[];
    int imgIndex, x = 0;
    int shotIndex = 0, tongueIndex = 0;
    long lastTime;
    boolean bShot;
    boolean eat;
    int type = -1, tongueLength;

    public Frog() {
        imgIndex = 0;
        lastTime = System.currentTimeMillis();
        bShot = false;
    }

    /**
     * ��� �̹����� �޸� ���� �Ѵ�.
     */
    public void destroyImages() {
        int i, size = img.length;
        for(i = 0; i < size; i++) {
            img[i].recycle();
            img[i] = null;
        }
        size = tongue.length;
        for(i = 0; i < size; i++) {
            tongue[i].recycle();
            tongue[i] = null;
        }
        size = shot.length;
        for(i = 0; i < size; i++) {
            shot[i].recycle();
            shot[i] = null;
        }
        size = dragonfly.length;
        for(i = 0; i < size; i++) {
            dragonfly[i].recycle();
            dragonfly[i] = null;
        }
    }

    /**
     * Ư�� ��ġ�� ���ڸ��� �������� Ȯ�� �Ѵ�.
     * 
     * @return ������ġ�� ��ȯ, ���������� -1
     */
    public int isCatched(int x, int y, int w, int h) {
        int cx = x + w / 2;
        int cy = y + h / 2;
        if(cx >= 246 && cx <= 371 && cy >= 185 && cy <= 253)
            return 1;
        if(cx >= 184 && cx <= 320 && cy >= 143 && cy <= 238)
            return 2;
        if(cx >= 92 && cx <= 173 && cy >= 106 && cy <= 174)
            return 3;
        if(cx >= 150 && cx <= 226 && cy >= 106 && cy <= 174)
            return 3;
        return -1;
    }

    /**
     * ���ڸ��� ��ƸԾ����� Ȯ��
     * 
     * @return �Ծ����� true
     */
    public boolean isEat() {
        return eat;
    }

    /**
     * ���ڸ��� ��ƸԾ����� �����Ѵ�.
     * 
     * @param eat ���� ����
     */
    public void setEat(boolean eat) {
        this.eat = eat;
    }

    /**
     * ��� �̹����� �����Ѵ�..
     */
    public void setImage(Bitmap image[], Bitmap shoot[], Bitmap tongue[], Bitmap dragonfly[]) {
        img = image;
        this.shot = shoot;
        this.tongue = tongue;
        this.dragonfly = dragonfly;
    }

    /**
     * �� ���б� ���������� Ȯ��
     * 
     * @return �������̸� true
     */
    public boolean shot() {
        if(bShot)
            return false;
        bShot = true;
        eat = false;
        shotIndex = 0;
        tongueIndex = 0;
        lastTime = System.currentTimeMillis();
        return true;
    }

    // �� �Ծ��� �� �ִϸ��̼�
    final int noEatFrog[] = { 0, 1, 2, 3, 4, 4, 5, 6, 7, 8 };
    final int noEatTong[] = { -1, 0, 1, 2, 3, 4, -1, -1, -1, -1 };
    // ��� �Ծ��� �� �ִϸ��̼�
    final int noEatFrog3[] = { 0, 1, 2, 3, 4, 3, 2, 1, 0 };
    final int noEatTong3[] = { -1, 0, 1, 2, 3, 2, 1, 0, -1 };
    // �߰� �Ծ��� �� �ִϸ��̼�
    final int noEatFrog2[] = { 0, 1, 2, 3, 2, 1, 0 };
    final int noEatTong2[] = { -1, 0, 1, 2, 1, 0, -1 };
    // ª�� �Ծ��� �� �ִϸ��̼�
    final int noEatFrog1[] = { 0, 1, 2, 1, 0 };
    final int noEatTong1[] = { -1, 0, 1, 0, -1 };
    // ������ ���ڸ� �׸��� ��ġ(���)
    final int catchFlyX[] = { 425, 296, 220, 131 };
    final int catchFlyY[] = { 301, 217, 167, 137 };

    /**
     * ������ �����̴� ó��
     */
    public void move() {
        long now = System.currentTimeMillis();

        if(bShot) {

            if(eat) {
                if(now > lastTime + 100) {
                    lastTime = now;
                    shotIndex++;
                }
            } else {
                shotIndex++;
            }
            if(type == -1) {
                // ���Ծ��� �� - �ִϸ��̼��� ������ �����Ų��.
                if(shotIndex >= noEatFrog.length)
                    bShot = false;
            } else {
                if(tongueLength == 3) {
                    if(shotIndex >= noEatFrog3.length)
                        bShot = false;
                    if(shotIndex >= 4)
                        eat = true;
                } else if(tongueLength == 2) {
                    if(shotIndex >= noEatFrog2.length)
                        bShot = false;
                    if(shotIndex >= 3)
                        eat = true;
                } else if(tongueLength == 1) {
                    if(shotIndex >= noEatFrog1.length)
                        bShot = false;
                    if(shotIndex >= 2)
                        eat = true;
                }
            }
        } else {
            if(now > lastTime + 500) {
                imgIndex = (imgIndex + 1) % img.length;
                lastTime = now;
            }
        }
    }

    /**
     * �������� �׸���.
     * 
     * @param c ĵ����
     * @param p ����Ʈ
     */
    public void draw(Canvas c, Paint p) {

        if(bShot) {
            if(type == -1) {
                if(noEatTong[shotIndex] != -1) {
                    c.drawBitmap(tongue[noEatTong[shotIndex]], 0, 0, p);
                }
                c.drawBitmap(shot[noEatFrog[shotIndex]], 0, 0, p);
            } else {
                if(tongueLength == 3) {
                    if(noEatTong3[shotIndex] != -1) {
                        c.drawBitmap(tongue[noEatTong3[shotIndex]], 0, 0, p);
                        if(eat) {
                            c.drawBitmap(dragonfly[type], catchFlyX[noEatTong3[shotIndex]] - dragonfly[type].getWidth() / 2, catchFlyY[noEatTong3[shotIndex]] - dragonfly[type].getHeight() / 2, p);
                        }
                    }
                    c.drawBitmap(shot[noEatFrog3[shotIndex]], 0, 0, p);
                } else if(tongueLength == 2) {
                    if(noEatTong2[shotIndex] != -1) {
                        c.drawBitmap(tongue[noEatTong2[shotIndex]], 0, 0, p);
                        if(eat) {
                            c.drawBitmap(dragonfly[type], catchFlyX[noEatTong2[shotIndex]] - dragonfly[type].getWidth() / 2, catchFlyY[noEatTong2[shotIndex]] - dragonfly[type].getHeight() / 2, p);
                        }
                    }
                    c.drawBitmap(shot[noEatFrog2[shotIndex]], 0, 0, p);
                } else if(tongueLength == 1) {
                    if(noEatTong1[shotIndex] != -1) {
                        c.drawBitmap(tongue[noEatTong1[shotIndex]], 0, 0, p);
                        if(eat) {
                            c.drawBitmap(dragonfly[type], catchFlyX[noEatTong1[shotIndex]] - dragonfly[type].getWidth() / 2, catchFlyY[noEatTong1[shotIndex]] - dragonfly[type].getHeight() / 2, p);
                        }
                    }
                    c.drawBitmap(shot[noEatFrog1[shotIndex]], 0, 0, p);
                }
            }
        } else {
            c.drawBitmap(img[imgIndex], 0, 0, p);
        }
    }

    /**
     * Ÿ���� �����Ѵ�.
     * 
     * @param type Ÿ�԰�
     */
    public void setType(int type, int tongueLength) {
        this.type = type;
        this.tongueLength = tongueLength;
    }
}
