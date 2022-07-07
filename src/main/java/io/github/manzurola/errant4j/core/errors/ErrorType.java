package io.github.manzurola.errant4j.core.errors;

public enum ErrorType {
    MISSING("M"),
    UNNECESSARY("U"),
    REPLACEMENT("R"),
    NONE("E");

    private final String tag;

    ErrorType(String tag) {
        this.tag = tag;
    }

    public String tag() {
        return tag;
    }
}
