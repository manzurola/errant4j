package edu.guym.errantj.core.classify;

import edu.guym.spacyj.api.containers.Token;
import io.squarebunny.aligner.edit.Edit;

public abstract class CategoryMatchRule implements Rule {

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        return isSatisfied(edit) ? GrammaticalError.create(edit, getCategory()) : GrammaticalError.unknown(edit);
    }

    public abstract GrammaticalError.Category getCategory();

    public abstract boolean isSatisfied(Edit<Token> edit);
}
