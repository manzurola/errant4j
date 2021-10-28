package com.github.manzurola.errant4j.core.mark;

import java.util.Objects;

public class MarkedError {

    private final int charStart;
    private final int charEnd;
    private final String source;
    private final String target;

    public MarkedError(int charStart, int charEnd, String source, String replacement) {
        this.charStart = charStart;
        this.charEnd = charEnd;
        this.source = source;
        this.target = Objects.requireNonNull(replacement);
    }

    public int charStart() {
        return charStart;
    }

    public int çharEnd() {
        return charEnd;
    }

    public String source() {
        return source;
    }

    public String target() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MarkedError that = (MarkedError) o;
        return charStart == that.charStart &&
               charEnd == that.charEnd &&
               source.equals(that.source) &&
               target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(charStart, charEnd, source, target);
    }

    @Override
    public String toString() {
        return "MarkedError{" +
               "charStart=" + charStart +
               ", charEnd=" + charEnd +
               ", source='" + source + '\'' +
               ", target='" + target + '\'' +
               '}';
    }

}
