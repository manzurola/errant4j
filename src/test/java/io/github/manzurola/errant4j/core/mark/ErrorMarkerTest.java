package io.github.manzurola.errant4j.core.mark;

import com.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.Annotation;
import io.github.manzurola.errant4j.core.errors.GrammaticalError;
import io.github.manzurola.spacy4j.adapters.corenlp.CoreNLPAdapter;
import io.github.manzurola.spacy4j.api.SpaCy;
import io.github.manzurola.spacy4j.api.containers.Doc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ErrorMarkerTest {

    private static SpaCy spacy = SpaCy.create(CoreNLPAdapter.forEnglish());

    @BeforeAll
    static void setup() {
        spacy = SpaCy.create(CoreNLPAdapter.forEnglish());
    }

    @Test
    void missingWordHasBeforeAndAfter() {
        Doc source = spacy.nlp("My name guy.");
        Doc target = spacy.nlp("My name is guy.");
        Annotation annotation = Annotation.of(
            Edit
                .builder()
                .insert("is")
                .atPosition(2, 2)
                .project(source.tokens(), target.tokens()),
            GrammaticalError.NONE
        );

        ErrorMarker marker = new NeighbourIncludingErrorMarker();
        MarkedError actual = marker.markError(annotation, source.tokens());
        MarkedError expected = new MarkedError(
            3,
            11,
            "name guy",
            "name is guy"
        );
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void missingWordEmptySentence() {
        Doc source = spacy.nlp("");
        Doc target = spacy.nlp("My name is guy.");
        Annotation annotation = Annotation.of(
            Edit
                .builder()
                .insert("is")
                .atPosition(0, 2)
                .project(source.tokens(), target.tokens()),
            GrammaticalError.NONE
        );

        ErrorMarker marker = new NeighbourIncludingErrorMarker();
        MarkedError actual = marker.markError(annotation, source.tokens());
        MarkedError expected = new MarkedError(0, 0, "", "is");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void missingWordWithoutNeighbours() {
        Doc source = spacy.nlp("My name guy");
        Doc target = spacy.nlp("My name is guy.");
        Annotation annotation = Annotation.of(
            Edit
                .builder()
                .insert("is")
                .atPosition(2, 2)
                .project(source.tokens(), target.tokens()),
            GrammaticalError.NONE
        );

        ErrorMarker marker = new CursorErrorMarker();
        MarkedError actual = marker.markError(annotation, source.tokens());
        MarkedError expected = new MarkedError(8, 8, "", "is");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void emptySourceCursorMarker() {
        Doc source = spacy.nlp("");
        Doc target = spacy.nlp("My name is guy.");
        Annotation annotation = Annotation.of(
            Edit
                .builder()
                .insert("is")
                .atPosition(0, 2)
                .project(source.tokens(), target.tokens()),
            GrammaticalError.NONE
        );

        ErrorMarker marker = new CursorErrorMarker();
        MarkedError actual = marker.markError(annotation, source.tokens());
        MarkedError expected = new MarkedError(0, 0, "", "is");
        Assertions.assertEquals(expected, actual);
    }

}
