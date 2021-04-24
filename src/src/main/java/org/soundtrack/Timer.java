package org.soundtrack;

import java.util.Calendar;

public class Timer {
    private long shift;

    public Timer() {
        shift = 0;
    }

    public long time() {
        return absTime() + shift;
    }

    public long absTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public void shift(long _shift) {
        shift += _shift;
    }

    public long getShift() {
        return shift;
    }

    public void reset(long to) {
        shift = to;
    }
}
