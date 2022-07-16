package io.github.manzurola.errant4j.lang.en.classify;

import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.aligner.edit.Operation;
import io.github.manzurola.errant4j.core.classify.ClassificationRule;
import io.github.manzurola.errant4j.core.classify.Classifier;
import io.github.manzurola.errant4j.core.errors.ErrorCategory;
import io.github.manzurola.errant4j.core.errors.GrammaticalError;
import io.github.manzurola.errant4j.lang.en.classify.rules.*;
import io.github.manzurola.errant4j.lang.en.utils.lemmatize.Lemmatizer;
import io.github.manzurola.errant4j.lang.en.utils.wordlist.WordList;
import io.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;

public class EnClassifier implements Classifier {

    private final List<ClassificationRule> rules;

    public EnClassifier(Lemmatizer lemmatizer, WordList wordList) {
        this.rules = List.of(new IgnoreSpaceErrorRule(),

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

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        if (edit.operation().equals(Operation.EQUAL)) {
            return GrammaticalError.NONE;
        }
        GrammaticalError error = null;
        for (ClassificationRule classifier : rules) {
            error = classifier.classify(edit);
            if (!error.category().equals(ErrorCategory.OTHER)) {
                return error;
            }
        }
        return error;
    }
}
