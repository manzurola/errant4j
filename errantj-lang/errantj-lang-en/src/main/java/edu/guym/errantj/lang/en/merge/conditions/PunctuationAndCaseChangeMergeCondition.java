package edu.guym.errantj.lang.en.merge.conditions;

import edu.guym.errantj.lang.en.classiy.common.TokenPredicates;
import io.squarebunny.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;
import io.squarebunny.aligner.edit.predicates.EditPredicates;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.squarebunny.aligner.edit.predicates.EditPredicates.ofSizeOneToOne;

public class PunctuationAndCaseChangeMergeCondition implements EditMergeCondition {

    @Override
    public boolean test(Edit<Token> left, Edit<Token> right) {
        return left.matches(containsPunctuation()) && right.matches(isCaseChange());
    }

    public static Predicate<Edit<Token>> containsPunctuation() {
        return edit -> edit.stream()
                .anyMatch(TokenPredicates.isPunctuation());
    }

    public static Predicate<Edit<Token>> isCaseChange() {
        return edit -> edit.matches(ofSizeOneToOne()) && sidesEqualByLowerCase().test(edit);
    }

    public static Predicate<Edit<Token>> sidesEqualByLowerCase() {
        return edit -> {
            List<String> source = edit.source().stream().map(Token::lowerCase).collect(Collectors.toList());
            List<String> target = edit.target().stream().map(Token::lowerCase).collect(Collectors.toList());
            return source.equals(target);
        };
    }

}
