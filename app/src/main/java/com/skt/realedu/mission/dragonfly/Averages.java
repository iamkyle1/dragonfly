package com.skt.realedu.mission.dragonfly;

/**
 * 입력값의 평균으로 보정한다.
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
     * 입력값 추가
     * 
     * @param val 입력값
     * @return 입력된 값들의 평균값
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
     * 평균값을 반환한다.
     * 
     * @return 평균값
     */
    public float getAverage() {
        return avr;
    }
}
