package com.github.manzurola.errant4j.core;

import com.github.manzurola.aligner.Aligner;
import com.github.manzurola.aligner.Alignment;
import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.classify.Classifier;
import com.github.manzurola.errant4j.core.errors.GrammaticalError;
import com.github.manzurola.errant4j.core.merge.Merger;
import com.github.manzurola.spacy4j.api.SpaCy;
import com.github.manzurola.spacy4j.api.containers.Doc;
import com.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;
import java.util.Objects;

final class AnnotatorImpl implements Annotator {

    private final SpaCy spaCy;
    private final Aligner<Token> aligner;
    private final Merger merger;
    private final Classifier classifier;

    public AnnotatorImpl(SpaCy spaCy,
                          Aligner<Token> aligner,
                          Merger merger,
                          Classifier classifier) {
        this.spaCy = Objects.requireNonNull(spaCy);
        this.aligner = Objects.requireNonNull(aligner);
        this.merger = Objects.requireNonNull(merger);
        this.classifier = Objects.requireNonNull(classifier);
    }

    @Override
    public final Doc parse(String text) {
        return spaCy.nlp(text);
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


}
