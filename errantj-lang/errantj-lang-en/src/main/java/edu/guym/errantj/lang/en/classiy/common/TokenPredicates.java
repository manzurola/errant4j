package edu.guym.errantj.lang.en.classiy.common;

import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.Dependency;
import edu.guym.spacyj.api.features.UdPos;

import java.util.Collection;
import java.util.function.Predicate;

public class TokenPredicates {

    public static Predicate<Token> isVerb() {
        return word -> UdPos.VERB.matches(word.pos());
    }

    public static Predicate<Token> isPreposition() {
        return word -> UdPos.ADP.matches(word.pos());
    }

    public static Predicate<Token> isPunctuation() {
        return word -> UdPos.PUNCT.matches(word.pos());
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
        return word -> UdPos.ADJ.matches(word.pos());
    }

    public static Predicate<Token> isAdverb() {
        return word -> UdPos.ADV.matches(word.pos());
    }

    public static Predicate<Token> isPronoun() {
        return word -> UdPos.PRON.matches(word.pos());
    }

    public static Predicate<Token> isNoun() {
        return word -> UdPos.NOUN.matches(word.pos());
    }

}
