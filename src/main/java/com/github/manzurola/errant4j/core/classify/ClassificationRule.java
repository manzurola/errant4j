package com.github.manzurola.errant4j.core.classify;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.errors.GrammaticalError;
import com.github.manzurola.spacy4j.api.containers.Token;

public interface ClassificationRule {
    GrammaticalError classify(Edit<Token> edit);
}
