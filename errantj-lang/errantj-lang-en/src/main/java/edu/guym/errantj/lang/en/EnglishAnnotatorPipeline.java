package edu.guym.errantj.lang.en;

import edu.guym.aligner.Aligner;
import edu.guym.aligner.alignment.Alignment;
import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.annotate.AnnotatorPipeline;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.align.AlignerSupplier;
import edu.guym.errantj.lang.en.classiy.ErrantClassifier;
import edu.guym.errantj.lang.en.classiy.rules.core.Classifier;
import edu.guym.errantj.lang.en.lemmatize.Lemmatizer;
import edu.guym.errantj.lang.en.lemmatize.WordNetLemmatizer;
import edu.guym.errantj.lang.en.merge.Merger;
import edu.guym.spacyj.api.Spacy;
import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.api.containers.Token;

import java.util.List;

public class EnglishAnnotatorPipeline implements AnnotatorPipeline {

    private final Spacy spacy;
    private final Aligner<Token> aligner;
    private final Merger merger;
    private final Classifier classifier;

    public EnglishAnnotatorPipeline(Spacy spacy, Aligner<Token> aligner, Merger merger, Classifier classifier) {
        this.spacy = spacy;
        this.aligner = aligner;
        this.merger = merger;
        this.classifier = classifier;
    }

    public static EnglishAnnotatorPipeline create(Spacy spacy) {
        Lemmatizer lemmatizer = new WordNetLemmatizer();
        Aligner<Token> aligner = AlignerSupplier.create(lemmatizer).get();
        return new EnglishAnnotatorPipeline(
                spacy,
                aligner,
                new Merger(),
                new ErrantClassifier(lemmatizer)
        );
    }

    @Override
    public Doc parse(String text) {
        return spacy.nlp(text);
    }

    @Override
    public Alignment<Token> align(List<Token> source, List<Token> target) {
        return aligner.align(source, target);
    }

    @Override
    public List<Edit<Token>> merge(List<Edit<Token>> edits) {
        return merger.merge(edits);
    }

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        return classifier.classify(edit);
    }
}
