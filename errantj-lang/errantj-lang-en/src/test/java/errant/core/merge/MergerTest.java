package errant.core.merge;

import edu.guym.errantj.lang.en.align.AlignerSupplier;
import edu.guym.errantj.lang.en.lemmatize.Lemmatizer;
import edu.guym.errantj.lang.en.lemmatize.WordNetLemmatizer;
import edu.guym.errantj.lang.en.merge.Merger;
import edu.guym.spacyj.api.Spacy;
import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.clients.corenlp.StanfordCoreNlpSpacyClient;
import edu.guym.aligner.Aligner;
import edu.guym.aligner.alignment.Alignment;
import edu.guym.aligner.edit.Edit;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MergerTest {

    private final Lemmatizer lemmatizer = new WordNetLemmatizer();
    private final Spacy spacy = Spacy.create(new StanfordCoreNlpSpacyClient());

    @Test
    public void testMergeInfinitivalSamePos() {
        Doc source = spacy.nlp("I like to eat food.");
        Doc target = spacy.nlp("I like eating food.");
        Aligner<Token> aligner = AlignerSupplier.create(lemmatizer).get();
        Alignment<Token> alignment = aligner.align(source.tokens(), target.tokens());
        Merger merger = new Merger();
        List<Edit<String>> merged = merger.merge(alignment.edits())
                .stream()
                .map(edit -> edit.map(Token::text))
                .collect(Collectors.toList());
        assertTrue(merged.contains(Edit.builder().substitute("to", "eat").with("eating").atPosition(2, 2)));
    }

    @Test
    public void testMergeInfinitivalSamePos2() {
        Doc source = spacy.nlp("I eated dinner yesterday");
        Doc target = spacy.nlp("I have eaten dinner yesterday");
        Aligner<Token> aligner = AlignerSupplier.create(lemmatizer).get();
        Alignment<Token> alignment = aligner.align(source.tokens(), target.tokens());
        Merger merger = new Merger();
        List<Edit<String>> merged = merger.merge(alignment.edits())
                .stream()
                .map(edit -> edit.map(Token::text))
                .collect(Collectors.toList());
        assertTrue(merged.contains(Edit.builder().substitute("eated").with("have", "eaten").atPosition(1, 1)));
    }
}
