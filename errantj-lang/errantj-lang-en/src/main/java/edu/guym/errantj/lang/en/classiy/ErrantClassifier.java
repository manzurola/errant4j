package edu.guym.errantj.lang.en.classiy;

import edu.guym.errantj.core.classify.RuleBasedClassifier;
import edu.guym.errantj.lang.en.classiy.rules.UnknownErrorCleanupRule;
import edu.guym.errantj.lang.en.classiy.rules.morphtier.*;
import edu.guym.errantj.lang.en.classiy.rules.postier.*;
import edu.guym.errantj.lang.en.classiy.rules.tokentier.*;
import edu.guym.errantj.lang.en.lemmatize.Lemmatizer;
import edu.guym.errantj.lang.en.wordlist.HunspellEnglishWordList;
import edu.guym.errantj.wordlist.WordList;

import java.util.Arrays;

public class ErrantClassifier extends RuleBasedClassifier {

    public ErrantClassifier(Lemmatizer lemmatizer) {
        super(() -> {
            WordList wordList = new HunspellEnglishWordList();
            return Arrays.asList(
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


                    // cleanups
                    new SpellingErrorRule(wordList),
                    new MissingApostropheInContractionRule(),
                    new PartOfSpeechRule(),
                    new UnknownErrorCleanupRule()
            );
        });

    }
}
