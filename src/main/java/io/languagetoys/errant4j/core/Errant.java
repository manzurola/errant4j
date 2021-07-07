package io.languagetoys.errant4j.core;

import io.languagetoys.aligner.Alignment;
import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.annotate.Annotator;
import io.languagetoys.errant4j.lang.en.EnAnnotator;
import io.languagetoys.spacy4j.api.SpaCy;
import io.languagetoys.spacy4j.api.containers.Doc;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.List;

public interface Errant {

    Doc parse(String text);

    Alignment<Token> align(List<Token> source, List<Token> target);

    List<Edit<Token>> merge(List<Edit<Token>> edits);

    GrammaticalError classify(Edit<Token> edit);

    /**
     * Run the full pipeline given parsed source and target texts.
     */
    List<Annotation> annotate(List<Token> source, List<Token> target);

    /**
     * Create an English {@link Errant}, the default implementation for Errant annotators.
     *
     * @param spacy an instantiated spacy object
     * @return a new {@link Errant}
     */
    static Errant en(SpaCy spacy) {
        return new ErrantImpl(spacy, new EnAnnotator());
    }

    /**
     * Create your own custom {@link Errant}
     *
     * @param spacy     an instantiated spacy object
     * @param annotator the annotator that handles alignment, merger and classification
     * @return a new {@link Errant}
     */
    static Errant of(SpaCy spacy, Annotator annotator) {
        return new ErrantImpl(spacy, annotator);
    }
}
