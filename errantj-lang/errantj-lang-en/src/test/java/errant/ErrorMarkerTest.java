package errant;

import edu.guym.errantj.core.annotate.Annotator;
import edu.guym.errantj.core.mark.CharOffset;
import edu.guym.errantj.core.mark.ErrorMarker;
import edu.guym.errantj.lang.en.annotate.ErrantEn;
import edu.guym.spacyj.api.Spacy;
import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.clients.corenlp.StanfordCoreNlpSpacyClient;
import io.squarebunny.aligner.edit.Edit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ErrorMarkerTest {

    private final static Spacy spacy = Spacy.create(new StanfordCoreNlpSpacyClient());
    private final static Annotator annotator = ErrantEn.create(spacy);

    @Test
    void missingWordHasBeforeAndAfter() {
        Doc source = annotator.nlp("My name guy.");
        Doc target = annotator.nlp("My name is guy.");
        Edit<Token> edit = Edit.builder()
                .insert("is")
                .atPosition(2, 2)
                .transform(e -> tokenize(e, source, target));

        ErrorMarker converter = new ErrorMarker();
        CharOffset actual = converter.markEdit(edit, source.tokens());
        CharOffset expected = CharOffset.of(3, 11);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void missingWordEmptySentence() {
        Doc source = annotator.nlp("");
        Doc target = annotator.nlp("My name is guy.");
        Edit<Token> edit = Edit.builder()
                .insert("is")
                .atPosition(2, 2)
                .transform(e -> tokenize(e, source, target));

        ErrorMarker converter = new ErrorMarker();
        CharOffset actual = converter.markEdit(edit, source.tokens());
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
