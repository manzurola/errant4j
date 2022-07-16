package io.github.manzurola.errant4j.core.align;

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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnAlignerTest {

    private static Annotator annotator;

    @BeforeAll
    static void setup() {
        annotator = new EnAnnotatorFactory().create(SpaCy.create(CoreNLPAdapter.forEnglish()));
    }

    @Test
    public void align() {
        Doc source = annotator.parse("the guy am");
        Doc target = annotator.parse("am guy the");

        List<Edit<String>> expected = new ArrayList<>();
        expected.add(Edit.builder().substitute("the").with("am").atPosition(0, 0));
        expected.add(Edit.builder().equal("guy").and("guy").atPosition(1, 1));
        expected.add(Edit.builder().substitute("am").with("the").atPosition(2, 2));

        List<Edit<String>> actual = annotator
                .align(source.tokens(), target.tokens())
                .stream()
                .map(edit -> edit.map(Token::text))
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void alignWhitespace() {
        Doc source = annotator.parse("  I   like consume food.");
        Doc target = annotator.parse("I like to eat food.");

        Alignment<Token> align = annotator.align(source.tokens(), target.tokens());
        System.out.println(align);
    }

}
