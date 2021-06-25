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
import edu.guym.spacyj.api.Spacy;
import edu.guym.spacyj.api.containers.Token;

public final class Errant {

    private Errant() {
    }

    public static Annotator en(Spacy spacy) {
        Lemmatizer lemmatizer = new WordNetLemmatizer();
        return Annotator.create(
                spacy,
                new EnAligner(lemmatizer),
                new EnMerger(),
                new EnClassifier(lemmatizer, new HunspellWordList())
        );
    }

    public static Annotator custom(Spacy spacy, Aligner<Token> aligner, Merger merger, Classifier classifier) {
        return Annotator.create(spacy, aligner, merger, classifier);
    }
}
