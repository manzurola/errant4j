package io.languagetoys.errant4j.core;

import io.languagetoys.aligner.Aligner;
import io.languagetoys.aligner.Alignment;
import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.classify.Classifier;
import io.languagetoys.errant4j.core.merge.Merger;
import io.languagetoys.spacy4j.api.SpaCy;
import io.languagetoys.spacy4j.api.containers.Doc;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

final class ErrantImpl implements Errant {

    private final SpaCy spacy;
    private final Aligner<Token> aligner;
    private final Merger merger;
    private final Classifier classifier;

    public ErrantImpl(SpaCy spacy,
                      Aligner<Token> aligner,
                      Merger merger,
                      Classifier classifier) {
        this.spacy = Objects.requireNonNull(spacy);
        this.aligner = Objects.requireNonNull(aligner);
        this.merger = Objects.requireNonNull(merger);
        this.classifier = Objects.requireNonNull(classifier);
    }

    public final Doc parse(String text) {
        return spacy.nlp(text);
    }

    @Override
    public final Alignment<Token> align(List<Token> source, List<Token> target) {
        return aligner.align(source, target);
    }

    @Override
    public final List<Edit<Token>> merge(List<Edit<Token>> edits) {
        return merger.merge(edits);
    }

    @Override
    public final GrammaticalError classify(Edit<Token> edit) {
        return classifier.classify(edit);
    }

    @Override
    public Errant setAligner(Aligner<Token> aligner) {
        return new ErrantImpl(spacy, aligner, merger, classifier);
    }

    @Override
    public Errant setMerger(Merger merger) {
        return new ErrantImpl(spacy, aligner, merger, classifier);
    }

    @Override
    public Errant setClassifier(Classifier classifier) {
        return new ErrantImpl(spacy, aligner, merger, classifier);
    }
}
