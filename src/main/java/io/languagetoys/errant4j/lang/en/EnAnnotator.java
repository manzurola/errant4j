package io.languagetoys.errant4j.lang.en;

import io.languagetoys.aligner.Alignment;
import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.GrammaticalError;
import io.languagetoys.errant4j.core.annotate.Annotator;
import io.languagetoys.errant4j.lang.en.align.EnAligner;
import io.languagetoys.errant4j.lang.en.classify.EnClassifier;
import io.languagetoys.errant4j.lang.en.merge.EnMerger;
import io.languagetoys.errant4j.lang.en.utils.lemmatize.Lemmatizer;
import io.languagetoys.errant4j.lang.en.utils.lemmatize.WordNetLemmatizer;
import io.languagetoys.errant4j.lang.en.utils.wordlist.HunspellWordList;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.List;

public class EnAnnotator implements Annotator {

    private final Annotator impl;

    public EnAnnotator() {
        Lemmatizer lemmatizer = new WordNetLemmatizer();
        this.impl = Annotator.of(
                new EnAligner(lemmatizer),
                new EnMerger(),
                new EnClassifier(lemmatizer, new HunspellWordList())
        );
    }

    @Override
    public Alignment<Token> align(List<Token> source, List<Token> target) {
        return impl.align(source, target);
    }

    @Override
    public List<Edit<Token>> merge(List<Edit<Token>> edits) {
        return impl.merge(edits);
    }

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        return impl.classify(edit);
    }

}
