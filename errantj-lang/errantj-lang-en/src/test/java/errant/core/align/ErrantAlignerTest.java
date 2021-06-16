package errant.core.align;

import edu.guym.errantj.lang.en.align.AlignerSupplier;
import edu.guym.errantj.lang.en.lemmatize.Lemmatizer;
import edu.guym.errantj.lang.en.lemmatize.WordNetLemmatizer;
import edu.guym.spacyj.api.Spacy;
import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.clients.corenlp.StanfordCoreNlpSpacyClient;
import edu.guym.aligner.Aligner;
import edu.guym.aligner.edit.Edit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrantAlignerTest {

    private final static Spacy spacy = Spacy.create(new StanfordCoreNlpSpacyClient());
    private final Lemmatizer lemmatizer = new WordNetLemmatizer();

    @Test
    public void align() {
        Doc source = parse("the guy am");
        Doc target = parse("am guy the");

        Aligner<Token> aligner = AlignerSupplier.create(lemmatizer).get();
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

    private Doc parse(String text) {
        return spacy.nlp(text);
    }

}
