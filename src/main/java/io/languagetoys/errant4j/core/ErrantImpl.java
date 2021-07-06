package io.languagetoys.errant4j.core;

import io.languagetoys.aligner.Aligner;
import io.languagetoys.aligner.Alignment;
import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.classify.Classifier;
import io.languagetoys.errant4j.core.merge.Merger;
import io.languagetoys.errant4j.core.grammar.GrammaticalError;
import io.languagetoys.spacy4j.api.SpaCy;
import io.languagetoys.spacy4j.api.containers.Doc;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.List;
import java.util.stream.Collectors;

final class ErrantImpl implements Errant {

    private final SpaCy spacy;
    private final Aligner<Token> aligner;
    private final Merger merger;
    private final Classifier classifier;

    ErrantImpl(SpaCy spacy, Aligner<Token> aligner, Merger merger, Classifier classifier) {
        this.spacy = spacy;
        this.aligner = aligner;
        this.merger = merger;
        this.classifier = classifier;
    }

    public final Doc parse(String text) {
        return spacy.nlp(text);
    }

    /**
     * Run the full pipeline given parsed source and target texts.
     */
    public final List<Annotation> annotate(List<Token> source, List<Token> target) {
        Alignment<Token> alignment = align(source, target);
        List<Edit<Token>> merged = merge(alignment.edits());
        return merged.stream()
                .map(edit -> Annotation.of(edit).setGrammaticalError(classify(edit)))
                .collect(Collectors.toList());
    }

    public final Alignment<Token> align(List<Token> source, List<Token> target) {
        return aligner.align(source, target);
    }

    public final List<Edit<Token>> merge(List<Edit<Token>> edits) {
        return merger.merge(edits);
    }

    public final GrammaticalError classify(Edit<Token> edit) {
        return classifier.classify(edit);
    }
}
