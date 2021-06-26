package edu.guym.errantj.core.annotator;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.grammar.GrammaticalError;
import edu.guym.spacyj.api.containers.Token;


public interface Classifier {

    /**
     * Get the {@link GrammaticalError} for the supplied Edit.
     */
    GrammaticalError classify(Edit<Token> edit);
}
