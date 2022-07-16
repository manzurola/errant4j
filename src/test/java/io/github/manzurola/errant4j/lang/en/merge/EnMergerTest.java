package io.github.manzurola.errant4j.lang.en.merge;

import io.github.manzurola.aligner.Alignment;
import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.Annotator;
import io.github.manzurola.errant4j.lang.en.EnAnnotatorFactory;
import io.github.manzurola.spacy4j.adapters.corenlp.CoreNLPAdapter;
import io.github.manzurola.spacy4j.api.SpaCy;
import io.github.manzurola.spacy4j.api.containers.Doc;
import io.github.manzurola.spacy4j.api.containers.Token;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnMergerTest {

    private static Annotator annotator;

    @BeforeAll
    static void setup() {
        annotator = new EnAnnotatorFactory().create(SpaCy.create(CoreNLPAdapter.forEnglish()));
    }

    @Test
    public void testMergeInfinitivalSamePos() {
        Doc source = annotator.parse("I like to eat food.");
        Doc target = annotator.parse("I like eating food.");
        Alignment<Token> alignment = annotator.align(source.tokens(), target.tokens());
        List<Edit<String>> merged = annotator.merge(alignment.edits())
                .stream()
                .map(edit -> edit.map(Token::text))
                .collect(Collectors.toList());
        assertTrue(merged.contains(Edit.builder().substitute("to", "eat").with("eating").atPosition(2, 2)));
    }

    @Test
    public void testMergeInfinitivalSamePos2() {
        Doc source = annotator.parse("I eated dinner yesterday");
        Doc target = annotator.parse("I have eaten dinner yesterday");
        Alignment<Token> alignment = annotator.align(source.tokens(), target.tokens());
        List<Edit<String>> merged = annotator.merge(alignment.edits())
                .stream()
                .map(edit -> edit.map(Token::text))
                .collect(Collectors.toList());
        assertTrue(merged.contains(Edit.builder().substitute("eated").with("have", "eaten").atPosition(1, 1)));
    }
}
