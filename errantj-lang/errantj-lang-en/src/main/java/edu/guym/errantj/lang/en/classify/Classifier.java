package edu.guym.errantj.lang.en.classify;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classify.rules.Rule;
import edu.guym.errantj.lang.en.classify.rules.cleanup.IgnoreSpaceErrorRule;
import edu.guym.errantj.lang.en.classify.rules.cleanup.UnknownErrorCleanupRule;
import edu.guym.errantj.lang.en.classify.rules.morphtier.*;
import edu.guym.errantj.lang.en.classify.rules.postier.*;
import edu.guym.errantj.lang.en.classify.rules.tokentier.*;
import edu.guym.errantj.lang.en.utils.lemmatize.Lemmatizer;
import edu.guym.errantj.lang.en.utils.wordlist.WordList;
import edu.guym.spacyj.api.containers.Token;

import java.util.List;

import static edu.guym.errantj.core.errors.GrammaticalError.NONE;
import static edu.guym.errantj.lang.en.classify.rules.common.Predicates.isEqual;

public class Classifier {

    private final List<Rule> rules;

    public Classifier(Lemmatizer lemmatizer, WordList wordList) {
        this.rules = getRules(lemmatizer, wordList);
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

    public GrammaticalError classify(Edit<Token> edit) {
        if (edit.matches(isEqual())) {
            return NONE;
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
}
