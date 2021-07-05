package io.languagetoys.errant4j.core.annotator;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.grammar.GrammaticalError;
import io.languagetoys.spacy4j.api.containers.Token;

public abstract class ClassificationPredicate implements ClassificationRule {

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        return isSatisfied(edit) ? GrammaticalError.of(edit, getCategory()) : GrammaticalError.unknown(edit);
    }

    public abstract GrammaticalError.Category getCategory();

    public abstract boolean isSatisfied(Edit<Token> edit);
}
