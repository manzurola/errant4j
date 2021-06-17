package edu.guym.errantj.lang.en.align;

import edu.guym.aligner.alignment.Alignment;
import edu.guym.errantj.lang.en.utils.lemmatize.Lemmatizer;
import edu.guym.errantj.lang.en.utils.lemmatize.WordNetLemmatizer;
import edu.guym.spacyj.adapters.spacyserver.SpacyServerAdapter;
import edu.guym.spacyj.api.Spacy;
import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.adapters.corenlp.CoreNlpAdapter;
import edu.guym.aligner.Aligner;
import edu.guym.aligner.edit.Edit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrantAlignerTest {

    private final static Spacy spacy = Spacy.create(new SpacyServerAdapter());
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

    @Test
    public void alignWhitespace() {
        Doc source = parse("  I   like consume food.");
        Doc target = parse("I like to eat food.");

        Aligner<Token> aligner = AlignerSupplier.create(lemmatizer).get();

        Alignment<Token> align = aligner.align(source.tokens(), target.tokens());
        System.out.println(align);


    }

    private Doc parse(String text) {
        return spacy.nlp(text);
    }

}
