package io.languagetoys.errant4j.core;

import io.languagetoys.aligner.Aligner;
import io.languagetoys.errant4j.core.align.TokenAligner;
import io.languagetoys.errant4j.core.classify.Classifier;
import io.languagetoys.errant4j.core.merge.Merger;
import io.languagetoys.spacy4j.api.containers.Token;

public interface Pipeline {

    Aligner<Token> getAligner();

    Merger getMerger();

    Classifier getClassifier();

    abstract class Base implements Pipeline {

        @Override
        public Aligner<Token> getAligner() {
            return new TokenAligner();
        }

    }
}
