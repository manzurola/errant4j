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
import java.util.stream.Collectors;

public interface Errant {

    Doc parse(String text);

    /**
     * Run the full pipeline given parsed source and target texts.
     */
    default List<Annotation> annotate(List<Token> source, List<Token> target) {
        Alignment<Token> alignment = align(source, target);
        List<Edit<Token>> merged = merge(alignment.edits());
        return merged.stream()
                .map(edit -> Annotation.of(edit, classify(edit)))
                .collect(Collectors.toList());
    }

    Alignment<Token> align(List<Token> source, List<Token> target);

    List<Edit<Token>> merge(List<Edit<Token>> edits);

    GrammaticalError classify(Edit<Token> edit);

    /**
     * Set a new aligner as the alignment pipeline stage.
     *
     * @param aligner a Token {@link Aligner}
     * @return a new Errant with the supplied aligner
     */
    Errant setAligner(Aligner<Token> aligner);

    /**
     * Set a new merger as the merge pipeline stage.
     *
     * @param merger a {@link Merger}
     * @return a new Errant with the supplied merger
     */
    Errant setMerger(Merger merger);

    /**
     * Set a new classifier as the classification pipeline stage.
     *
     * @param classifier a {@link Classifier}
     * @return a new Errant with the supplied classifier
     */
    Errant setClassifier(Classifier classifier);

    /**
     * Create a custom {@link Errant}
     *
     * @param spacy    an instantiated spacy object
     * @param pipeline a pipeline that provides the necessary annotation components
     * @return a new {@link Errant}
     */
    static Errant of(SpaCy spacy, Pipeline pipeline) {
        return of(spacy, pipeline.getAligner(), pipeline.getMerger(), pipeline.getClassifier());
    }

    /**
     * Create your own custom {@link Errant}
     *
     * @param spacy      an instantiated spacy object
     * @param spacy      a custom Token {@link Aligner}
     * @param merger     a {@link Merger} that merges aligned edits
     * @param classifier a {@link Classifier} that classifies merged edits
     * @return a new {@link Errant}
     */
    static Errant of(SpaCy spacy, Aligner<Token> aligner, Merger merger, Classifier classifier) {
        return new ErrantImpl(spacy, aligner, merger, classifier);
    }

}
