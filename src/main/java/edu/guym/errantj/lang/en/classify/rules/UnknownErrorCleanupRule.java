package edu.guym.errantj.lang.en.classify.rules;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classify.Rule;
import edu.guym.spacyj.api.containers.Token;

public class UnknownErrorCleanupRule implements Rule {

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        return GrammaticalError.unknown(edit);
    }
}
