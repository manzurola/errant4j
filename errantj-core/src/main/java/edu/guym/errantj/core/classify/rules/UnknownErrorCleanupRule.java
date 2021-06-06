package edu.guym.errantj.core.classify.rules;

import edu.guym.errantj.core.classify.GrammaticalError;
import io.squarebunny.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;

public class UnknownErrorCleanupRule implements Rule {

    @Override
    public GrammaticalError apply(Edit<Token> edit) {
        return unknown(edit);
    }
}
