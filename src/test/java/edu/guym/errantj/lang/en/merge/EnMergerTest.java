package edu.guym.errantj.lang.en.merge;

import edu.guym.aligner.Aligner;
import edu.guym.aligner.Alignment;
import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.annotator.Merger;
import edu.guym.errantj.lang.en.align.EnAligner;
import edu.guym.errantj.lang.en.utils.lemmatize.WordNetLemmatizer;
import edu.guym.spacyj.adapters.corenlp.CoreNlpAdapter;
import edu.guym.spacyj.api.SpaCy;
import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.api.containers.Token;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnMergerTest {

    private SpaCy spacy;
    private Aligner<Token> aligner;
    private Merger merger;

    @BeforeAll
    void setup() {
        this.spacy = SpaCy.create(CoreNlpAdapter.create());
        this.aligner = new EnAligner(new WordNetLemmatizer());
        this.merger = new EnMerger();
    }

    @Test
    public void testMergeInfinitivalSamePos() {
        Doc source = spacy.nlp("I like to eat food.");
        Doc target = spacy.nlp("I like eating food.");
        Alignment<Token> alignment = aligner.align(source.tokens(), target.tokens());
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
        Alignment<Token> alignment = aligner.align(source.tokens(), target.tokens());
        List<Edit<String>> merged = merger.merge(alignment.edits())
                .stream()
                .map(edit -> edit.map(Token::text))
                .collect(Collectors.toList());
        assertTrue(merged.contains(Edit.builder().substitute("eated").with("have", "eaten").atPosition(1, 1)));
    }
}
