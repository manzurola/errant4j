package io.languagetoys.errant4j.lang.en.classify.rules;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.grammar.GrammaticalError;
import io.languagetoys.errant4j.core.annotator.ClassificationRule;
import io.languagetoys.spacy4j.api.containers.Token;

public class UnknownErrorCleanupRule implements ClassificationRule {

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        return GrammaticalError.unknown(edit);
    }
}
