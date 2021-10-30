package com.github.manzurola.errant4j.lang.en.classify;

import com.github.manzurola.errant4j.core.classify.ClassificationRule;
import com.github.manzurola.errant4j.core.classify.RuleBasedClassifier;
import com.github.manzurola.errant4j.lang.en.classify.rules.*;
import com.github.manzurola.errant4j.lang.en.utils.lemmatize.Lemmatizer;
import com.github.manzurola.errant4j.lang.en.utils.lemmatize.WordNetLemmatizer;
import com.github.manzurola.errant4j.lang.en.utils.wordlist.HunspellWordList;
import com.github.manzurola.errant4j.lang.en.utils.wordlist.WordList;

import java.util.List;

public class EnClassifier extends RuleBasedClassifier {

    private final List<ClassificationRule> rules;

    public EnClassifier() {
        this(new WordNetLemmatizer(), new HunspellWordList());
    }

    public EnClassifier(Lemmatizer lemmatizer, WordList wordList) {
        this.rules = List.of(
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

    @Override
    protected List<ClassificationRule> getRules() {
        return rules;
    }
}
