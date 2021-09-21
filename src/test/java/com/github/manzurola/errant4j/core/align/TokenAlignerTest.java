package com.github.manzurola.errant4j.core.align;

import com.github.manzurola.aligner.Aligner;
import com.github.manzurola.aligner.Alignment;
import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.spacy4j.adapters.corenlp.CoreNLPAdapter;
import com.github.manzurola.spacy4j.api.SpaCy;
import com.github.manzurola.spacy4j.api.containers.Doc;
import com.github.manzurola.spacy4j.api.containers.Token;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenAlignerTest {

    private static SpaCy spacy;
    private static Aligner<Token> aligner;

    @BeforeAll
    static void setup() {
        spacy = SpaCy.create(CoreNLPAdapter.create());
        aligner = new TokenAligner();
    }

    @Test
    public void align() {
        Doc source = spacy.nlp("the guy am");
        Doc target = spacy.nlp("am guy the");

        List<Edit<String>> expected = new ArrayList<>();
        expected.add(Edit.builder().substitute("the").with("am").atPosition(0, 0));
        expected.add(Edit.builder().equal("guy").and("guy").atPosition(1, 1));
        expected.add(Edit.builder().substitute("am").with("the").atPosition(2, 2));

        List<Edit<String>> actual = aligner
                .align(source.tokens(), target.tokens())
                .stream()
                .map(edit -> edit.map(Token::text))
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void alignWhitespace() {
        Doc source = spacy.nlp("  I   like consume food.");
        Doc target = spacy.nlp("I like to eat food.");

        Alignment<Token> align = aligner.align(source.tokens(), target.tokens());
        System.out.println(align);
    }

}
