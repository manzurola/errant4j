package edu.guym.errantj.core.tools.mark;

import java.util.Objects;

public final class CharOffset {

    private final int start;
    private final int end;

    public CharOffset(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public static CharOffset of(int start, int end) {
        return new CharOffset(start, end);
    }

    public final int start() {
        return start;
    }

    public final int end() {
        return end;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharOffset that = (CharOffset) o;
        return start == that.start &&
                end == that.end;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public final String toString() {
        return "CharOffset{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }


}
