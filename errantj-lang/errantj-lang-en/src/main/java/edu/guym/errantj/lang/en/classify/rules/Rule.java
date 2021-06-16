package edu.guym.errantj.lang.en.classify.rules;

import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.aligner.edit.Edit;

public interface Rule {

    GrammaticalError classify(Edit<Token> edit);
}
