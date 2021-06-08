package edu.guym.errantj.core.classify;

import edu.guym.spacyj.api.containers.Token;
import io.squarebunny.aligner.edit.Edit;

public interface Rule {

    GrammaticalError classify(Edit<Token> edit);
}
