package io.github.manzurola.errant4j.core.mark;

import java.util.Objects;

public class MarkedError {

    private final int charStart;
    private final int charEnd;
    private final String original;
    private final String corrected;

    public MarkedError(
        int charStart,
        int charEnd,
        String original,
        String corrected
    ) {
        this.charStart = charStart;
        this.charEnd = charEnd;
        this.original = original;
        this.corrected = Objects.requireNonNull(corrected);
    }

    public int charStart() {
        return charStart;
    }

    public int çharEnd() {
        return charEnd;
    }

    public String source() {
        return original;
    }

    public String target() {
        return corrected;
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
               original.equals(that.original) &&
               corrected.equals(that.corrected);
    }

    @Override
    public int hashCode() {
        return Objects.hash(charStart, charEnd, original, corrected);
    }

    @Override
    public String toString() {
        return "MarkedError{" +
               "charStart=" + charStart +
               ", charEnd=" + charEnd +
               ", source='" + original + '\'' +
               ", target='" + corrected + '\'' +
               '}';
    }

}
