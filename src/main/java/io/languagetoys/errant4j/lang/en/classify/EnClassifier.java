package io.languagetoys.errant4j.lang.en.classify;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.annotator.Classifier;
import io.languagetoys.errant4j.core.grammar.GrammaticalError;
import io.languagetoys.errant4j.lang.en.classify.rules.*;
import io.languagetoys.errant4j.lang.en.classify.rules.common.Predicates;
import io.languagetoys.errant4j.lang.en.utils.lemmatize.Lemmatizer;
import io.languagetoys.errant4j.lang.en.utils.wordlist.WordList;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.List;

public class EnClassifier implements Classifier {

    private final List<Rule> rules;

    public EnClassifier(Lemmatizer lemmatizer, WordList wordList) {
        this.rules = getRules(lemmatizer, wordList);
    }

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        if (edit.matches(Predicates.isEqual())) {
            return GrammaticalError.NONE;
        }
        GrammaticalError error = null;
        for (Rule classifier : rules) {
            error = classifier.classify(edit);
            if (!error.category().equals(GrammaticalError.Category.OTHER)) {
                return error;
            }
        }
        return error;
    }

    public List<Rule> getRules(Lemmatizer lemmatizer, WordList wordList) {
        return List.of(

                new IgnoreSpaceErrorRule(),

                new WordOrderRule(),
                new OrthographyErrorRule(),

                new AdjectiveFormRule(lemmatizer),
                new MultiTokenAdjectiveFormRule(lemmatizer),

                new NounInflectionRule(wordList),
                new VerbInflectionRule(wordList),

                new NounNumberErrorRule(),
                new NounNumberAdjConfusion(),

                new NounPossessiveRule(),

                new ContractionRule(),
                new ContractionAuxRule(),

                new SubjectVerbAgreementRule(),
                new VerbTenseRule(lemmatizer),

                new VerbFormRule(lemmatizer),
                new MissingOrUnnecessaryVerbFormInfinitivalToRule(),
                new ReplacementVerbFormInfinitivalToRule(),

                new VerbRule(),
                new PartRule(),
                new DetPronRule(),
                new PunctuationEffectRule(),

                new SpellingErrorRule(wordList),
                new MissingApostropheInContractionRule(),
                new PartOfSpeechRule(),

                // cleanup
                new UnknownErrorCleanupRule()
        );
    }
}
