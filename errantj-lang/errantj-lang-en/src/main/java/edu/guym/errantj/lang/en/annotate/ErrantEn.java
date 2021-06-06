package edu.guym.errantj.lang.en.annotate;

import edu.guym.errantj.core.annotate.Errant;
import edu.guym.errantj.core.classify.Classifier;
import edu.guym.errantj.core.merge.Merger;
import edu.guym.errantj.lang.en.aligner.TokenSubstituteCost;
import edu.guym.errantj.lang.en.classiy.ErrantClassifier;
import edu.guym.errantj.lang.en.lemmatize.Lemmatizer;
import edu.guym.errantj.lang.en.lemmatize.WordNetLemmatizer;
import edu.guym.errantj.lang.en.merge.ErrantMerger;
import edu.guym.spacyj.api.Spacy;
import edu.guym.spacyj.api.containers.Token;
import io.squarebunny.aligner.Aligner;

import java.util.Comparator;

public class ErrantEn extends Errant {

    private ErrantEn(Spacy spacy, Aligner<Token> aligner, Merger merger, Classifier classifier) {
        super(spacy, aligner, merger, classifier);
    }

    public static ErrantEn create(Spacy spacy) {
        Lemmatizer lemmatizer = new WordNetLemmatizer();
        return new ErrantEn(
                spacy,
                Aligner.damerauLevenshtein(
                        (source, target) -> source.text().equals(target.text()),
                        Comparator.comparing(Token::lowerCase),
                        new TokenSubstituteCost(lemmatizer)
                ),
                new ErrantMerger(),
                new ErrantClassifier(lemmatizer)
        );
    }
}
