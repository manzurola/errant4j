package com.github.manzurola.errant4j.core.classify;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.GrammaticalError;
import com.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;


public interface Classifier {

    /**
     * Get the {@link GrammaticalError} for the supplied Edit.
     */
    GrammaticalError classify(Edit<Token> edit);

    static Classifier rules(List<Rule> rules) {
        return new ClassifierImpl(rules);
    }

    interface Rule {
        GrammaticalError classify(Edit<Token> edit);
    }

    abstract class Predicate implements Rule, java.util.function.Predicate<Edit<Token>> {

        @Override
        public GrammaticalError classify(Edit<Token> edit) {
            return test(edit) ? GrammaticalError.of(edit, getCategory()) : GrammaticalError.unknown(edit);
        }

        public abstract GrammaticalError.Category getCategory();

        public abstract boolean test(Edit<Token> edit);
    }
}