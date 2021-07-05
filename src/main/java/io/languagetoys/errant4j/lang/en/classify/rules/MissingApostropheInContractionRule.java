package io.languagetoys.errant4j.lang.en.classify.rules;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.grammar.GrammaticalError;
import io.languagetoys.errant4j.core.annotator.ClassificationPredicate;
import io.languagetoys.errant4j.lang.en.classify.rules.common.Predicates;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MissingApostropheInContractionRule extends ClassificationPredicate {

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
