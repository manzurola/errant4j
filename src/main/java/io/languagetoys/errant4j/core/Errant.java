package io.languagetoys.errant4j.core;

import io.languagetoys.aligner.Aligner;
import io.languagetoys.aligner.Alignment;
import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.classify.Classifier;
import io.languagetoys.errant4j.core.merge.Merger;
import io.languagetoys.errant4j.core.grammar.GrammaticalError;
import io.languagetoys.errant4j.lang.en.align.EnAligner;
import io.languagetoys.errant4j.lang.en.classify.EnClassifier;
import io.languagetoys.errant4j.lang.en.merge.EnMerger;
import io.languagetoys.errant4j.lang.en.utils.lemmatize.Lemmatizer;
import io.languagetoys.errant4j.lang.en.utils.lemmatize.WordNetLemmatizer;
import io.languagetoys.errant4j.lang.en.utils.wordlist.HunspellWordList;
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
     * @param spacy an instantiated spacy object
     * @return a new {@link Errant}
     */
    static Errant en(SpaCy spacy) {
        Lemmatizer lemmatizer = new WordNetLemmatizer();
        return new ErrantImpl(
                spacy,
                new EnAligner(lemmatizer),
                new EnMerger(),
                new EnClassifier(lemmatizer, new HunspellWordList())
        );
    }

    /**
     * Create your own custom {@link Errant}
     * @param spacy an instantiated spacy object
     * @param aligner an {@link Aligner} object, 1st step in the pipeline
     * @param merger a {@link Merger} that merges the alignment, 2nd step in the pipeline
     * @param classifier a {@link Classifier} that classifies merged edits, 3rd and final step in the pipeline
     * @return a new {@link Errant}
     */
    static Errant of(SpaCy spacy, Aligner<Token> aligner, Merger merger, Classifier classifier) {
        return new ErrantImpl(spacy, aligner, merger, classifier);
    }
}
