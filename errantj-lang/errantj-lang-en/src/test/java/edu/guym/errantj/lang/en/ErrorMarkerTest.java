package edu.guym.errantj.lang.en;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.annotate.Errant;
import edu.guym.errantj.core.tools.mark.CharOffset;
import edu.guym.errantj.core.tools.mark.ErrorMarker;
import edu.guym.spacyj.adapters.corenlp.CoreNlpAdapter;
import edu.guym.spacyj.api.Spacy;
import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.api.containers.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ErrorMarkerTest {

    private final static Spacy spacy = Spacy.create(CoreNlpAdapter.create());
    private final static Errant errant = Errant.create(EnglishAnnotatorPipeline.create(spacy));

    @Test
    void missingWordHasBeforeAndAfter() {
        Doc source = errant.parse("My name guy.");
        Doc target = errant.parse("My name is guy.");
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
        Doc source = errant.parse("");
        Doc target = errant.parse("My name is guy.");
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
                source -> source.mapWithIndex(sourceDoc::getToken),
                target -> target.mapWithIndex(targetDoc::getToken)
        );
    }
}
