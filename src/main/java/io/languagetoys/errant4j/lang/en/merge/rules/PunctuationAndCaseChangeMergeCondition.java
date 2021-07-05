package io.languagetoys.errant4j.lang.en.merge.rules;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.merge.Merger;
import io.languagetoys.errant4j.lang.en.classify.rules.common.Predicates;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.languagetoys.errant4j.lang.en.classify.rules.common.Predicates.ofSizeOneToOne;

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
        return edit -> edit.matches(ofSizeOneToOne()) && sidesEqualByLowerCase().test(edit);
    }

    public static Predicate<Edit<Token>> sidesEqualByLowerCase() {
        return edit -> {
            List<String> source = edit.source().stream().map(Token::lower).collect(Collectors.toList());
            List<String> target = edit.target().stream().map(Token::lower).collect(Collectors.toList());
            return source.equals(target);
        };
    }

}
