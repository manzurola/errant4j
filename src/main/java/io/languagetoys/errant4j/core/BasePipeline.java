package io.languagetoys.errant4j.core;

import io.languagetoys.aligner.Aligner;
import io.languagetoys.errant4j.core.align.TokenAligner;
import io.languagetoys.spacy4j.api.containers.Token;

public abstract class BasePipeline implements Pipeline {

    @Override
    public Aligner<Token> getAligner() {
        return new TokenAligner();
    }

}
