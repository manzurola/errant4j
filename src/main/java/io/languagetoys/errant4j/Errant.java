package io.languagetoys.errant4j;

import io.languagetoys.aligner.Aligner;
import io.languagetoys.errant4j.core.annotator.Annotator;
import io.languagetoys.errant4j.core.annotator.Classifier;
import io.languagetoys.errant4j.core.annotator.Merger;
import io.languagetoys.errant4j.lang.en.align.EnAligner;
import io.languagetoys.errant4j.lang.en.classify.EnClassifier;
import io.languagetoys.errant4j.lang.en.merge.EnMerger;
import io.languagetoys.errant4j.lang.en.utils.lemmatize.Lemmatizer;
import io.languagetoys.errant4j.lang.en.utils.lemmatize.WordNetLemmatizer;
import io.languagetoys.errant4j.lang.en.utils.wordlist.HunspellWordList;
import io.languagetoys.spacy4j.api.SpaCy;
import io.languagetoys.spacy4j.api.containers.Token;

/**
 * Entry point for creating {@link Annotator} objects via static factory methods.
 */
public final class Errant {

    private Errant() {
    }

    /**
     * Create an English {@link Annotator}, the default implementation for Errant annotators.
     * @param spacy an instantiated spacy object
     * @return a new {@link Annotator}
     */
    public static Annotator en(SpaCy spacy) {
        Lemmatizer lemmatizer = new WordNetLemmatizer();
        return Annotator.create(
                spacy,
                new EnAligner(lemmatizer),
                new EnMerger(),
                new EnClassifier(lemmatizer, new HunspellWordList())
        );
    }

    /**
     * Create your own custom {@link Annotator}
     * @param spacy an instantiated spacy object
     * @param aligner an {@link Aligner} object, 1st step in the pipeline
     * @param merger a {@link Merger} that merges the alignment, 2nd step in the pipeline
     * @param classifier a {@link Classifier} that classifies merged edits, 3rd and final step in the pipeline
     * @return a new {@link Annotator}
     */
    public static Annotator custom(SpaCy spacy, Aligner<Token> aligner, Merger merger, Classifier classifier) {
        return Annotator.create(spacy, aligner, merger, classifier);
    }
}
