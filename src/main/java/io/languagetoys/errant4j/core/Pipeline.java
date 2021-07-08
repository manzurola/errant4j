package io.languagetoys.errant4j.core;

import io.languagetoys.aligner.Aligner;
import io.languagetoys.errant4j.core.classify.Classifier;
import io.languagetoys.errant4j.core.merge.Merger;
import io.languagetoys.spacy4j.api.containers.Token;

public interface Pipeline {

    Aligner<Token> getAligner();

    Merger getMerger();

    Classifier getClassifier();
}
