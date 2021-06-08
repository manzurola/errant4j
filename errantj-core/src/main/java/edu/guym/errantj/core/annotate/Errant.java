package edu.guym.errantj.core.annotate;

import edu.guym.errantj.core.classify.Classifier;
import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.merge.Merger;
import edu.guym.spacyj.api.Spacy;
import io.squarebunny.aligner.Aligner;
import io.squarebunny.aligner.alignment.Alignment;
import io.squarebunny.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.api.containers.Token;

import java.util.List;
import java.util.stream.Collectors;

public class Errant implements Annotator {

    private final Spacy spacy;
    private final Aligner<Token> aligner;
    private final Merger merger;
    private final Classifier classifier;

    public Errant(Spacy spacy, Aligner<Token> aligner, Merger merger, Classifier classifier) {
        this.spacy = spacy;
        this.aligner = aligner;
        this.merger = merger;
        this.classifier = classifier;
    }

    @Override
    public Doc nlp(String value) {
        return spacy.nlp(value);
    }

    public Alignment<Token> align(List<Token> source, List<Token> target) {
        return aligner.align(source, target);
    }

    public List<Edit<Token>> merge(List<Edit<Token>> alignment) {
        return merger.merge(alignment);
    }

    public GrammaticalError classify(Edit<Token> edit) {
        return classifier.classify(edit);
    }

    @Override
    public List<Annotation<Token>> annotate(List<Token> source, List<Token> target) {
        Alignment<Token> alignment = align(source, target);
        List<Edit<Token>> merged = merge(alignment.edits());
        return annotate(merged);
    }

    public List<Annotation<Token>> annotate(List<Edit<Token>> edits) {
        return edits.stream()
                .map(edit -> {
                    GrammaticalError annotation = classify(edit);
                    return Annotation.of(edit).withError(annotation);
                })
                .collect(Collectors.toList());
    }
}
