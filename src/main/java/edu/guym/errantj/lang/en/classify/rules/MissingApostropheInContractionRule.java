package edu.guym.errantj.lang.en.classify.rules;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classify.CategoryMatchRule;
import edu.guym.errantj.lang.en.classify.rules.common.Predicates;
import edu.guym.spacyj.api.containers.Token;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MissingApostropheInContractionRule extends CategoryMatchRule {

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.ORTH;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        return edit
                .filter(e -> e.matches(Predicates.ofSize(1, 2).or(Predicates.ofSize(2,1))))
                .filter(missingApostropheInContraction())
                .isPresent();
    }

    public Predicate<Edit<Token>> missingApostropheInContraction() {
        return edit -> {
            String sourceText = edit.source().stream()
                    .map(Token::lower)
                    .map(String::trim)
                    .map(s -> s.replace("'", ""))
                    .collect(Collectors.joining());
            String targetText = edit.target().stream()
                    .map(Token::lower)
                    .map(String::trim)
                    .map(s -> s.replace("'", ""))
                    .collect(Collectors.joining());

            return sourceText.equals(targetText);
        };
    }
}
