package edu.guym.errantj.lang.en.classiy.rules.tokentier;

import edu.guym.errantj.core.classify.Category;
import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.rules.Rule;
import io.squarebunny.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.squarebunny.aligner.edit.predicates.EditPredicates.ofSize;

public class MissingApostropheInContractionRule implements Rule {
    @Override
    public GrammaticalError apply(Edit<Token> edit) {
        return edit
                .filter(e -> e.matches(ofSize(1, 2).or(ofSize(2,1))))
                .filter(missingApostropheInContraction())
                .map(classify(Category.ORTH))
                .orElse(unknown(edit));
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
