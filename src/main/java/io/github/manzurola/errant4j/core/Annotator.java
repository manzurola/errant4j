package io.github.manzurola.errant4j.core;

import io.github.manzurola.aligner.Aligner;
import io.github.manzurola.aligner.Alignment;
import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.classify.Classifier;
import io.github.manzurola.errant4j.core.errors.GrammaticalError;
import io.github.manzurola.errant4j.core.merge.Merger;
import io.github.manzurola.spacy4j.api.SpaCy;
import io.github.manzurola.spacy4j.api.containers.Doc;
import io.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Annotator {

    private final SpaCy spaCy;
    private final Aligner<Token> aligner;
    private final Merger<Token> merger;
    private final Classifier classifier;

    private Annotator(SpaCy spaCy,
                      Aligner<Token> aligner,
                      Merger<Token> merger,
                      Classifier classifier) {
        this.spaCy = Objects.requireNonNull(spaCy);
        this.aligner = Objects.requireNonNull(aligner);
        this.merger = Objects.requireNonNull(merger);
        this.classifier = Objects.requireNonNull(classifier);
    }

    public static Annotator of(SpaCy spaCy,
                               Aligner<Token> aligner,
                               Merger<Token> merger,
                               Classifier classifier) {
        return new Annotator(spaCy, aligner, merger, classifier);
    }

    /**
     * Helper method that applies spaCy.nlp(text)
     */
    public Doc parse(String text) {
        return spaCy.nlp(text);
    }


    /**
     * Run the full pipeline given parsed source and target texts.
     */
    public List<Annotation> annotate(List<Token> source, List<Token> target) {
        Alignment<Token> alignment = align(source, target);
        List<Edit<Token>> merged = merge(alignment.edits());
        return merged
                .stream()
                .map(edit -> Annotation.of(edit, classify(edit)))
                .collect(Collectors.toList());
    }

    /**
     * Run the 1st step in the pipeline - Align
     */
    public Alignment<Token> align(List<Token> source, List<Token> target) {
        return aligner.align(source, target);
    }

    /**
     * Run the 2nd step in the pipeline - Merge
     */
    public List<Edit<Token>> merge(List<Edit<Token>> edits) {
        return merger.merge(edits);
    }

    /**
     * Run the 3rd step in the pipeline - Classify
     */
    public GrammaticalError classify(Edit<Token> edit) {
        return classifier.classify(edit);
    }
}
