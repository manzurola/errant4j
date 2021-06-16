package edu.guym.errantj.lang.en.classiy.common;

import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.Dependency;
import edu.guym.spacyj.api.features.Pos;

import java.util.Collection;
import java.util.function.Predicate;

public class TokenPredicates {

    public static Predicate<Token> isVerb() {
        return word -> Pos.VERB.matches(word.pos());
    }

    public static Predicate<Token> isPreposition() {
        return word -> Pos.ADP.matches(word.pos());
    }

    public static Predicate<Token> isPunctuation() {
        return word -> Pos.PUNCT.matches(word.pos());
    }

    public static Predicate<Token> matchDependency(Dependency dependency) {
        return word -> dependency.matches(word.dependency());
    }

    public static Predicate<Token> matchAnyDependency(Collection<Dependency> dependencies) {
        return word -> dependencies
                .stream()
                .anyMatch(dependency -> matchDependency(dependency).test(word));
    }

    public static Predicate<Token> isContentWord() {
        return word -> word
                .matches(isAdjective()
                        .or(isAdverb())
                        .or(isNoun())
                        .or(isVerb()));
    }

    public static Predicate<Token> isAuxVerb() {
        return word -> isVerb()
                .and(matchDependency(Dependency.AUX))
                .and(matchDependency(Dependency.AUX_PASS))
                .test(word);
    }

    public static Predicate<Token> isAdjective() {
        return word -> Pos.ADJ.matches(word.pos());
    }

    public static Predicate<Token> isAdverb() {
        return word -> Pos.ADV.matches(word.pos());
    }

    public static Predicate<Token> isPronoun() {
        return word -> Pos.PRON.matches(word.pos());
    }

    public static Predicate<Token> isNoun() {
        return word -> Pos.NOUN.matches(word.pos());
    }

}
