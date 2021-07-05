package io.languagetoys.errant4j.lang.en;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.Errant;
import io.languagetoys.errant4j.core.annotator.Annotator;
import io.languagetoys.errant4j.core.tools.mark.CharOffset;
import io.languagetoys.errant4j.core.tools.mark.ErrorMarker;
import io.languagetoys.spacy4j.adapters.corenlp.CoreNLPAdapter;
import io.languagetoys.spacy4j.api.SpaCy;
import io.languagetoys.spacy4j.api.containers.Doc;
import io.languagetoys.spacy4j.api.containers.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ErrorMarkerTest {

    private final static Annotator annotator = Errant.en(SpaCy.create(CoreNLPAdapter.create()));

    @Test
    void missingWordHasBeforeAndAfter() {
        Doc source = annotator.parse("My name guy.");
        Doc target = annotator.parse("My name is guy.");
        Edit<Token> edit = Edit.builder()
                .insert("is")
                .atPosition(2, 2)
                .transform(e -> tokenize(e, source, target));

        ErrorMarker marker = new ErrorMarker(source.tokens());
        CharOffset actual = edit.accept(marker);
        CharOffset expected = CharOffset.of(3, 11);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void missingWordEmptySentence() {
        Doc source = annotator.parse("");
        Doc target = annotator.parse("My name is guy.");
        Edit<Token> edit = Edit.builder()
                .insert("is")
                .atPosition(2, 2)
                .transform(e -> tokenize(e, source, target));

        ErrorMarker marker = new ErrorMarker(source.tokens());
        CharOffset actual = edit.accept(marker);
        CharOffset expected = CharOffset.of(0, 0);
        Assertions.assertEquals(expected, actual);
    }

    private Edit<Token> tokenize(Edit<String> edit, Doc sourceDoc, Doc targetDoc) {
        return edit.mapSegments(
                source -> source.mapWithIndex(sourceDoc::token),
                target -> target.mapWithIndex(targetDoc::token)
        );
    }
}
