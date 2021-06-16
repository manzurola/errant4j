package edu.guym.errantj.lang.en.classiy.rules.core;

import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.aligner.edit.Edit;

public interface Rule {

    GrammaticalError classify(Edit<Token> edit);
}
