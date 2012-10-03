package org.dani.lazy.util;

public class IndexInterval {
    private final int start;
    private final int end;

    public IndexInterval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }

    public boolean contains(int index) {
        return start <= index && index < end;
    }

    public String toString() {
        return "[" + start + ", " + end + "]";
    }
}
