package io.languagetoys.errant4j.core;

import io.languagetoys.aligner.Aligner;
import io.languagetoys.aligner.Alignment;
import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.align.TokenAligner;
import io.languagetoys.errant4j.core.classify.Classifier;
import io.languagetoys.errant4j.core.merge.Merger;
import io.languagetoys.spacy4j.api.SpaCy;
import io.languagetoys.spacy4j.api.containers.Doc;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Annotator {

    protected final SpaCy spaCy;
    protected final Aligner<Token> aligner;
    protected final Merger merger;
    protected final Classifier classifier;

    private Annotator(SpaCy spaCy,
                         Aligner<Token> aligner,
                         Merger merger,
                         Classifier classifier) {
        this.spaCy = Objects.requireNonNull(spaCy);
        this.aligner = Objects.requireNonNull(aligner);
        this.merger = Objects.requireNonNull(merger);
        this.classifier = Objects.requireNonNull(classifier);
    }

    public static Annotator of(SpaCy spaCy, Merger merger, Classifier classifier) {
        return of(spaCy, new TokenAligner(), merger, classifier);
    }

    public static Annotator of(SpaCy spaCy, Aligner<Token> aligner, Merger merger, Classifier classifier) {
        return new Annotator(spaCy, aligner, merger, classifier);
    }

    public final Doc parse(String text) {
        return spaCy.nlp(text);
    }

    /**
     * Run the 1st step in the pipeline - Align
     */
    public final Alignment<Token> align(List<Token> source, List<Token> target) {
        return aligner.align(source, target);
    }

    /**
     * Run the 2nd step in the pipeline - Merge
     */
    public final List<Edit<Token>> merge(List<Edit<Token>> edits) {
        return merger.merge(edits);
    }

    /**
     * Run the 3rd step in the pipeline - Classify
     */
    public final GrammaticalError classify(Edit<Token> edit) {
        return classifier.classify(edit);
    }


    /**
     * Run the full pipeline given parsed source and target texts.
     */
    public final List<Annotation> annotate(List<Token> source, List<Token> target) {
        Alignment<Token> alignment = align(source, target);
        List<Edit<Token>> merged = merge(alignment.edits());
        return merged.stream()
                .map(edit -> Annotation.of(edit, classify(edit)))
                .collect(Collectors.toList());
    }

}
