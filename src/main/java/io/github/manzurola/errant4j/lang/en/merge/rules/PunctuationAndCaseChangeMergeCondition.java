package io.github.manzurola.errant4j.lang.en.merge.rules;

import com.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.merge.Merger;
import io.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import io.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PunctuationAndCaseChangeMergeCondition implements Merger.Rule {

    @Override
    public boolean test(Edit<Token> left, Edit<Token> right) {
        return left.matches(containsPunctuation()) && right.matches(isCaseChange());
    }

    public static Predicate<Edit<Token>> containsPunctuation() {
        return edit -> edit.stream()
                .anyMatch(Predicates.isPunctuation());
    }

    public static Predicate<Edit<Token>> isCaseChange() {
        return edit -> edit.matches(Predicates.ofSizeOneToOne()) && sidesEqualByLowerCase().test(edit);
    }

    public static Predicate<Edit<Token>> sidesEqualByLowerCase() {
        return edit -> {
            List<String> source = edit.source().stream().map(Token::lower).collect(Collectors.toList());
            List<String> target = edit.target().stream().map(Token::lower).collect(Collectors.toList());
            return source.equals(target);
        };
    }

}
