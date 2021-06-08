package edu.guym.errantj.lang.en.classiy.common;

import edu.guym.errantj.lang.en.lemmatize.Lemmatizer;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.UdPos;
import io.squarebunny.aligner.edit.Edit;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static edu.guym.errantj.core.tools.Collectors.oneOrNone;


public class TokenEditPredicates {

    public static Predicate<? super Edit<Token>> tokensShareSameTag() {
        return edit -> edit
                .stream()
                .map(Token::tag)
                .distinct()
                .collect(oneOrNone())
                .isPresent();
    }

    public static Predicate<? super Edit<Token>> tokensShareSamePos() {
        return edit -> edit
                .stream()
                .map(Token::pos)
                .distinct()
                .collect(oneOrNone())
                .isPresent();
    }

    public static Predicate<? super Edit<Token>> lemmasIntersect(Lemmatizer lemmatizer) {
        return edit -> {
            Set<String> sourceLemmas = edit.source()
                    .stream()
                    .map(Token::text)
                    .map(lemmatizer::lemmas)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

            Set<String> targetLemmas = edit.target()
                    .stream()
                    .map(Token::text)
                    .map(lemmatizer::lemmas)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

            return !Collections.disjoint(sourceLemmas, targetLemmas);
        };
    }

    public static Predicate<? super Edit<Token>> udPosTagSetEquals(UdPos... pos) {
        return edit -> edit.stream()
                .map(Token::pos)
                .map(UdPos::ofTag)
                .collect(Collectors.toSet())
                .equals(Set.of(pos));
    }

}
