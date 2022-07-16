package io.github.manzurola.errant4j.lang.en;

import io.github.manzurola.aligner.Aligner;
import io.github.manzurola.errant4j.core.Annotator;
import io.github.manzurola.errant4j.core.AnnotatorFactory;
import io.github.manzurola.errant4j.core.merge.ExcludeTransposeAndEqualsMergeFilter;
import io.github.manzurola.errant4j.core.merge.Merger;
import io.github.manzurola.errant4j.core.merge.SameOpOrSubstituteMergeAction;
import io.github.manzurola.errant4j.lang.en.align.EnTokenSubstituteCost;
import io.github.manzurola.errant4j.lang.en.classify.EnClassifier;
import io.github.manzurola.errant4j.lang.en.merge.EnMergeRules;
import io.github.manzurola.errant4j.lang.en.utils.lemmatize.WordNetLemmatizer;
import io.github.manzurola.errant4j.lang.en.utils.wordlist.HunspellWordList;
import io.github.manzurola.spacy4j.api.SpaCy;
import io.github.manzurola.spacy4j.api.containers.Token;

import java.util.Comparator;

public final class EnAnnotatorFactory implements AnnotatorFactory {

    @Override
    public Annotator create(SpaCy spaCy) {
        var lemmatizer = new WordNetLemmatizer();
        var wordList = new HunspellWordList();
        return Annotator.of(
                spaCy,
                aligner(),
                merger(),
                new EnClassifier(lemmatizer, wordList)
        );
    }

    public Aligner<Token> aligner() {
        return Aligner.<Token>builder()
                .setSubstituteCost(new EnTokenSubstituteCost())
                .setEquals((source, target) -> source.text().equals(target.text()))
                .setCompareTo(Comparator.comparing(Token::lower))
                .build();
    }

    public Merger<Token> merger() {
        return Merger.create(
                new ExcludeTransposeAndEqualsMergeFilter<>(),
                new EnMergeRules(),
                new SameOpOrSubstituteMergeAction<>());
    }

}
