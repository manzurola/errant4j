package edu.guym.errantj;

import edu.guym.aligner.Aligner;
import edu.guym.errantj.core.annotator.Annotator;
import edu.guym.errantj.core.annotator.Classifier;
import edu.guym.errantj.core.annotator.Merger;
import edu.guym.errantj.lang.en.align.EnAligner;
import edu.guym.errantj.lang.en.classify.EnClassifier;
import edu.guym.errantj.lang.en.merge.EnMerger;
import edu.guym.errantj.lang.en.utils.lemmatize.Lemmatizer;
import edu.guym.errantj.lang.en.utils.lemmatize.WordNetLemmatizer;
import edu.guym.errantj.lang.en.utils.wordlist.HunspellWordList;
import edu.guym.spacyj.api.SpaCy;
import edu.guym.spacyj.api.containers.Token;

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
