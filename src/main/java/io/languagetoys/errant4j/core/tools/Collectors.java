package io.languagetoys.errant4j.core.tools;

import java.util.Optional;
import java.util.stream.Collector;

public class Collectors {

    public static <T> Collector<T, ?, Optional<T>> oneOrNone() {
        return java.util.stream.Collectors.collectingAndThen(
                java.util.stream.Collectors.toList(),
                list -> {
                    if (list.size() != 1) return Optional.empty();
                    return Optional.of(list.iterator().next());
                }
        );
    }
}
