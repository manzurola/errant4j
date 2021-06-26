package edu.guym.errantj.core.annotator;

import edu.guym.aligner.Aligner;
import edu.guym.aligner.Alignment;
import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.grammar.GrammaticalError;
import edu.guym.spacyj.api.Spacy;
import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.api.containers.Token;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A configurable parallel text grammatical error annotator.
 * The Annotator acts as a pipeline that consists of four steps:
 * <ol>
 *     <li>Parse a text to {@link Doc} objects
 *     <li>Align the tokens of the doc
 *     <li>Merge the edits of the alignment
 *     <li>Classify each merged edit with a {@link GrammaticalError}
 * </ol>
 */
public final class Annotator {

    private final Spacy spacy;
    private final Aligner<Token> aligner;
    private final Merger merger;
    private final Classifier classifier;

    private Annotator(Spacy spacy, Aligner<Token> aligner, Merger merger, Classifier classifier) {
        this.spacy = spacy;
        this.aligner = aligner;
        this.merger = merger;
        this.classifier = classifier;
    }

    public static Annotator create(Spacy spacy,
                                   Aligner<Token> aligner,
                                   Merger merger,
                                   Classifier classifier) {
        return new Annotator(spacy, aligner, merger, classifier);
    }

    public final Doc parse(String text) {
        return spacy.nlp(text);
    }

    /**
     * Run the full pipeline given parsed source and target texts.
     */
    public final List<Annotation<Token>> annotate(List<Token> source, List<Token> target) {
        Alignment<Token> alignment = align(source, target);
        List<Edit<Token>> merged = merge(alignment.edits());
        return merged.stream()
                .map(edit -> Annotation.of(edit).withError(classify(edit)))
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
