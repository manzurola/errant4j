package com.github.manzurola.errant4j.lang.en.merge;

import com.github.manzurola.aligner.Aligner;
import com.github.manzurola.aligner.Alignment;
import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.align.TokenAligner;
import com.github.manzurola.errant4j.core.merge.Merger;
import com.github.manzurola.spacy4j.adapters.corenlp.CoreNLPAdapter;
import com.github.manzurola.spacy4j.api.SpaCy;
import com.github.manzurola.spacy4j.api.containers.Doc;
import com.github.manzurola.spacy4j.api.containers.Token;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnMergerTest {

    private static SpaCy spacy;
    private static Aligner<Token> aligner;
    private static Merger merger;

    @BeforeAll
    static void setup() {
        spacy = SpaCy.create(CoreNLPAdapter.forEnglish());
        aligner = new TokenAligner();
        merger = new EnMerger();
    }

    @Test
    public void testMergeInfinitivalSamePos() {
        Doc source = spacy.nlp("I like to eat food.");
        Doc target = spacy.nlp("I like eating food.");
        Alignment<Token> alignment = aligner.align(source.tokens(), target.tokens());
        List<Edit<String>> merged = merger.merge(alignment.edits())
                .stream()
                .map(edit -> edit.map(Token::text))
                .collect(Collectors.toList());
        assertTrue(merged.contains(Edit.builder().substitute("to", "eat").with("eating").atPosition(2, 2)));
    }

    @Test
    public void testMergeInfinitivalSamePos2() {
        Doc source = spacy.nlp("I eated dinner yesterday");
        Doc target = spacy.nlp("I have eaten dinner yesterday");
        Alignment<Token> alignment = aligner.align(source.tokens(), target.tokens());
        List<Edit<String>> merged = merger.merge(alignment.edits())
                .stream()
                .map(edit -> edit.map(Token::text))
                .collect(Collectors.toList());
        assertTrue(merged.contains(Edit.builder().substitute("eated").with("have", "eaten").atPosition(1, 1)));
    }
}
