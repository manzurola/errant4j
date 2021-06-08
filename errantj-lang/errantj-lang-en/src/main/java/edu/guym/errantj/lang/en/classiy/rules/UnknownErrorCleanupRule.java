package edu.guym.errantj.lang.en.classiy.rules;

import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.Rule;
import io.squarebunny.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;

public class UnknownErrorCleanupRule implements Rule {

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        return GrammaticalError.unknown(edit);
    }
}
