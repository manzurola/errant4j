package edu.guym.errantj.core.classify;

import io.squarebunny.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;

public interface Classifier {

    GrammaticalError classify(Edit<Token> edit);
}
