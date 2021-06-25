package edu.guym.errantj.core.annotator;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.spacyj.api.containers.Token;

public interface Classifier {

    GrammaticalError classify(Edit<Token> edit);
}
