package errant;
import edu.guym.spacyj.api.Spacy;
import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.clients.corenlp.StanfordCoreNlpSpacyClient;

public class TestTools {

    private static final Spacy spacy =  Spacy.create(new StanfordCoreNlpSpacyClient());

    public static Doc parse(String text) {
        return spacy.fromText(text);
    }

    public static Spacy getSpacy() {
        return spacy;
    }
}
