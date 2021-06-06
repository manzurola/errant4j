package edu.guym.errantj.core.classify.rules;

import edu.guym.errantj.core.classify.Category;
import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.GrammaticalErrorFactory;
import io.squarebunny.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;

import java.util.function.Function;

public interface Rule extends Function<Edit<Token>, GrammaticalError> {

    default GrammaticalError classify(Edit<Token> edit, Category category) {
        return classify(category).apply(edit);
    }

    default Function<Edit<Token>, GrammaticalError> classify(Category category) {
        return edit -> GrammaticalErrorFactory.getInstance().create(category, edit);
    }

    default GrammaticalError unknown(Edit<Token> edit) {
        return GrammaticalErrorFactory.getInstance().createUnknown(edit);
    }
}
