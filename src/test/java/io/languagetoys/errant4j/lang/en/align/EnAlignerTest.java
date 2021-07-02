package io.languagetoys.errant4j.lang.en.align;

import io.languagetoys.aligner.Aligner;
import io.languagetoys.aligner.Alignment;
import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.lang.en.utils.lemmatize.WordNetLemmatizer;
import io.languagetoys.spacy4j.adapters.corenlp.CoreNLPAdapter;
import io.languagetoys.spacy4j.api.SpaCy;
import io.languagetoys.spacy4j.api.containers.Doc;
import io.languagetoys.spacy4j.api.containers.Token;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnAlignerTest {

    private SpaCy spacy;
    private Aligner<Token> aligner;

    @BeforeAll
    void setup() {
        this.spacy = SpaCy.create(CoreNLPAdapter.create());
        this.aligner = new EnAligner(new WordNetLemmatizer());
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