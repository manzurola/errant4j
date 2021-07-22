package com.github.manzurola.errant4j.core.tools.mark;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.spacy4j.adapters.corenlp.CoreNLPAdapter;
import com.github.manzurola.spacy4j.api.SpaCy;
import com.github.manzurola.spacy4j.api.containers.Doc;
import com.github.manzurola.spacy4j.api.containers.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ErrorMarkerTest {

    private SpaCy spacy = SpaCy.create(CoreNLPAdapter.create());

    @BeforeAll
    void setup() {
        this.spacy = SpaCy.create(CoreNLPAdapter.create());
    }

    @Test
    void missingWordHasBeforeAndAfter() {
        Doc source = spacy.nlp("My name guy.");
        Doc target = spacy.nlp("My name is guy.");
        Edit<Token> edit = Edit.builder()
                .insert("is")
                .atPosition(2, 2)
                .project(source.tokens(), target.tokens());

        ErrorMarker marker = new ErrorMarker(source.tokens());
        CharOffset actual = edit.accept(marker);
        CharOffset expected = CharOffset.of(3, 11);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void missingWordEmptySentence() {
        Doc source = spacy.nlp("");
        Doc target = spacy.nlp("My name is guy.");
        Edit<Token> edit = Edit.builder()
                .insert("is")
                .atPosition(2, 2)
                .project(source.tokens(), target.tokens());

        ErrorMarker marker = new ErrorMarker(source.tokens());
        CharOffset actual = edit.accept(marker);
        CharOffset expected = CharOffset.of(0, 0);
        Assertions.assertEquals(expected, actual);
    }

}
