package com.skt.realedu.mission.dragonfly;

/**
 * �Է°��� ������� �����Ѵ�.
 */
class Averages {
    float vals[], sum, average, avr;
    int capacity, position, inputCount;

    public Averages() {
        this(10);
    }

    public Averages(int cap) {
        capacity = cap;
        vals = new float[capacity];
        position = inputCount = 0;
        sum = 0;
    }

    /**
     * �Է°� �߰�
     * 
     * @param val �Է°�
     * @return �Էµ� ������ ��հ�
     */
    public float addValue(float val) {
        position = (position + 1) % vals.length;
        if(inputCount < vals.length) {
            inputCount++;
        } else {
            sum -= vals[position];
        }

        vals[position] = val;
        sum += val;

        avr = sum / inputCount;
        return avr;
    }

    /**
     * ��հ��� ��ȯ�Ѵ�.
     * 
     * @return ��հ�
     */
    public float getAverage() {
        return avr;
    }
}
