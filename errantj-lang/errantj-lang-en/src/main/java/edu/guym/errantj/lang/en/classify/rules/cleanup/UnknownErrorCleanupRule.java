package edu.guym.errantj.lang.en.classify.rules.cleanup;

import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.lang.en.classify.rules.Rule;
import edu.guym.spacyj.api.containers.Token;

public class UnknownErrorCleanupRule implements Rule {

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        return GrammaticalError.unknown(edit);
    }
}
