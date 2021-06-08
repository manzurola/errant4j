package edu.guym.errantj.lang.en.classiy.rules.tokentier;

import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.CategoryMatchRule;
import io.squarebunny.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.squarebunny.aligner.edit.predicates.EditPredicates.ofSize;

public class MissingApostropheInContractionRule extends CategoryMatchRule {

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.ORTH;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        return edit
                .filter(e -> e.matches(ofSize(1, 2).or(ofSize(2,1))))
                .filter(missingApostropheInContraction())
                .isPresent();
    }

    public Predicate<Edit<Token>> missingApostropheInContraction() {
        return edit -> {
            String sourceText = edit.source().stream()
                    .map(Token::lowerCase)
                    .map(String::trim)
                    .map(s -> s.replace("'", ""))
                    .collect(Collectors.joining());
            String targetText = edit.target().stream()
                    .map(Token::lowerCase)
                    .map(String::trim)
                    .map(s -> s.replace("'", ""))
                    .collect(Collectors.joining());

            return sourceText.equals(targetText);
        };
    }
}
