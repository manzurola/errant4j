package edu.guym.errantj.lang.en.classify.rules;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.api.errors.GrammaticalError;
import edu.guym.spacyj.api.containers.Token;

public interface Rule {

    GrammaticalError classify(Edit<Token> edit);
}
