package edu.guym.errantj.lang.en.annotate;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.annotate.Annotation;
import edu.guym.errantj.core.annotate.Errant;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.EnglishAnnotatorPipeline;
import edu.guym.spacyj.api.Spacy;
import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.clients.corenlp.StanfordCoreNlpSpacyAdapter;
import edu.guym.spacyj.clients.spacyserver.HttpSpacyServerAdapter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class V1ErrantTest extends AnnotatorTestBase {

    @BeforeAll
    void setErrant() {
        Errant errant = Errant.create(
                EnglishAnnotatorPipeline.create(
                        Spacy.create(new StanfordCoreNlpSpacyAdapter())
                ));
        setErrant(errant);
    }
//
//    @BeforeAll
//    void setErrant() {
//        Errant errant = Errant.create(
//                EnglishAnnotatorPipeline.create(
//                        Spacy.create(new HttpSpacyServerAdapter())
//                ));
//        setErrant(errant);
//    }

    /**
     * This test fails with spacy-server client due to whitespaces classified as missing tokens.
     */
    @Test
    void posTier_Verb() {
        Doc source = nlp("  I   like consume food.");
        Doc target = nlp("I like to eat food.");
        Annotation<String> expected = Edit.builder()
                .substitute("consume")
                .with("to", "eat")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_VERB);
        assertSingleExpectedError(expected, source, target);
    }

    @Test
    void posTier_Part() {
        Doc source = nlp("I want in fly");
        Doc target = nlp("I want to fly");
        Annotation<String> expected = Edit.builder()
                .substitute("in")
                .with("to")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_PARTICLE);
        assertSingleExpectedError(expected, source, target);
    }

    @Test
    void posTier_PunctuationEffect() {
        Doc source = nlp("Because");
        Doc target = nlp(", because");
        Annotation<String> expected = Edit.builder()
                .substitute("Because")
                .with(",", "because")
                .atPosition(0, 0)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_PUNCTUATION);
        assertSingleExpectedError(expected, source, target);
    }

    @Test
    void tokenTier_Contractions() {
        Doc source = nlp("I've to go home.");
        Doc target = nlp("I have to go home.");
        Annotation<String> expected = Edit.builder()
                .substitute("'ve")
                .with("have")
                .atPosition(1, 1)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_CONTRACTION);
        assertSingleExpectedError(expected, source, target);
    }

    @Test
    void tokenTier_Orthography() {
        // case
        Doc source1 = nlp("My friend sleeps at Home.");
        Doc target1 = nlp("My friend sleeps at home.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("Home")
                .with("home")
                .atPosition(4, 4)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_ORTHOGRAPHY);
        assertSingleExpectedError(expected1, source1, target1);

        // whitespace
        Doc source2 = nlp("My friendsleeps at home.");
        Doc target2 = nlp("My friend sleeps at home.");
        Annotation<String> expected2 = Edit.builder()
                .substitute("friendsleeps")
                .with("friend", "sleeps")
                .atPosition(1, 1)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_ORTHOGRAPHY);
        assertSingleExpectedError(expected2, source2, target2);
    }

    @Test
    void tokenTier_Spelling() {
        // case
        Doc source1 = nlp("My frien sleeps at home.");
        Doc target1 = nlp("My friend sleeps at home.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("frien")
                .with("friend")
                .atPosition(1, 1)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_SPELLING);
        assertSingleExpectedError(expected1, source1, target1);
    }

    @Test
    void tokenTier_WordOrder() {
        // case
        Doc source1 = nlp("This dog is cute");
        Doc target1 = nlp("This is cute dog");
        Annotation<String> expected1 = Edit.builder()
                .transpose("dog", "is", "cute")
                .to("is", "cute", "dog")
                .atPosition(1, 1)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_WORD_ORDER);
        assertSingleExpectedError(expected1, source1, target1);

    }

    @Test
    void morphTier_AdjectiveForm() {
        // case
        Doc source1 = nlp("This is the most small computer I have ever seen!");
        Doc target1 = nlp("This is the smallest computer I have ever seen!");
        Annotation<String> expected2 = Edit.builder()
                .substitute("most", "small")
                .with("smallest")
                .atPosition(3, 3)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_ADJECTIVE_FORM);
        assertSingleExpectedError(expected2, source1, target1);
    }

    @Test
    void morphTier_AdjectiveForm2() {
        // case
        Doc source1 = nlp("This is the big computer.");
        Doc target1 = nlp("This is the biggest computer.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("big")
                .with("biggest")
                .atPosition(3, 3)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_ADJECTIVE_FORM);
        assertSingleExpectedError(expected1, source1, target1);
    }

    /**
     * This test fails on spacy-server due to REPLACEMENT_OTHER
     */
    @Test
    void morphTier_nounNumber() {
        Doc source1 = nlp("dog are cute.");
        Doc target1 = nlp("dogs are cute.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("Dog")
                .with("dogs")
                .atPosition(0, 0)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_NOUN_NUMBER);
        assertSingleExpectedError(expected1, source1, target1);
    }

    @Test
    void morphTier_nounPossessive() {
        Doc source1 = nlp("It is at the river edge");
        Doc target1 = nlp("It is at the river's edge");
        Annotation<String> expected1 = Edit.builder()
                .insert("'s")
                .atPosition(5, 5)
                .transform(Annotation::of)
                .withError(GrammaticalError.MISSING_NOUN_POSSESSIVE);
        assertSingleExpectedError(expected1, source1, target1);
        Doc source2 = nlp("It is at the rivers edge");
        Doc target2 = nlp("It is at the river's edge");
        Annotation<String> expected2 = Edit.builder()
                .substitute("rivers")
                .with("river", "'s")
                .atPosition(4, 4)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_NOUN_POSSESSIVE);
        assertSingleExpectedError(expected2, source2, target2);
    }

    @Disabled("there is a collision between tense and form rules")
    @Test
    public void morphTier_verbFormSubstitutionError() {
        Doc source1 = nlp("Brent would often became stunned");
        Doc target1 = nlp("Brent would often become stunned");
        Annotation<String> expected1 = Edit.builder()
                .substitute("became")
                .with("become")
                .atPosition(3, 3)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_VERB_FORM);
        assertSingleExpectedError(expected1, source1, target1);

        Doc source2 = nlp("is she go home?");
        Doc target2 = nlp("is she going home?");
        Annotation<String> expected2 = Edit.builder()
                .substitute("go")
                .with("going")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_VERB_FORM);
        assertSingleExpectedError(expected2, source2, target2);

        Doc source3 = nlp("is she went home");
        Doc target3 = nlp("is she going home");
        Annotation<String> expected3 = Edit.builder()
                .substitute("went")
                .with("going")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_VERB_FORM);
        assertSingleExpectedError(expected3, source3, target3);

        Doc source4 = nlp("is she goes home");
        Doc target4 = nlp("is she going home");
        Annotation<String> expected4 = Edit.builder()
                .substitute("goes")
                .with("going")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_VERB_FORM);
        assertSingleExpectedError(expected4, source4, target4);
    }

    @Test
    public void morphTier_verbAgreementSubstitutionError() {
        Doc source1 = nlp("I awaits your response.");
        Doc target1 = nlp("I await your response.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("awaits")
                .with("await")
                .atPosition(1, 1)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT);
        assertSingleExpectedError(expected1, source1, target1);

        Doc source2 = nlp("does she goes home?");
        Doc target2 = nlp("does she go home?");
        Annotation<String> expected2 = Edit.builder()
                .substitute("goes")
                .with("go")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT);
        assertSingleExpectedError(expected2, source2, target2);

        Doc source3 = nlp("He must tells him everything.");
        Doc target3 = nlp("He must tell him everything.");
        Annotation<String> expected3 = Edit.builder()
                .substitute("tells")
                .with("tell")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT);
        assertSingleExpectedError(expected3, source3, target3);
    }

    @Test
    public void morphTier_verbTenseError() {
        Doc source1 = nlp("I go to see him yesterday.");
        Doc target1 = nlp("I went to see him yesterday.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("go")
                .with("went")
                .atPosition(1, 1)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_VERB_TENSE);
        assertSingleExpectedError(expected1, source1, target1);
    }

    @Test
    public void morphTier_verbFormError_basic() {
        Doc source1 = nlp("I am eat dinner.");
        Doc target1 = nlp("I am eating dinner.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("eat")
                .with("eating")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_VERB_FORM);
        assertSingleExpectedError(expected1, source1, target1);
    }

    @Test
    public void morphTier_verbFormError_missingInfinitivalTo() {
        Doc source1 = nlp("I would like go home please!");
        Doc target1 = nlp("I would like to go home please!");
        Annotation<String> expected1 = Edit.builder()
                .insert("to")
                .atPosition(3, 3)
                .transform(Annotation::of)
                .withError(GrammaticalError.MISSING_VERB_FORM);
        assertSingleExpectedError(expected1, source1, target1);
    }

    @Test
    public void morphTier_verbFormError_unnecessaryInfinitivalTo() {
        Doc source1 = nlp("I must to eat now.");
        Doc target1 = nlp("I must eat now.");
        Annotation<String> expected1 = Edit.builder()
                .delete("to")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.UNNECESSARY_VERB_FORM);
        assertSingleExpectedError(expected1, source1, target1);
    }

    @Test
    public void morphTier_nounInflection() {
        Doc source1 = nlp("I have five childs.");
        Doc target1 = nlp("I have five children.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("childs")
                .with("children")
                .atPosition(3, 3)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_NOUN_INFLECTION);
        assertSingleExpectedError(expected1, source1, target1);
    }

    /**
     * This test fails on spacy-server with REPLACEMENT_VERB
     */
    @Test
    public void morphTier_verbInflection() {
        Doc source1 = nlp("I getted the money!");
        Doc target1 = nlp("I got the money!");
        Annotation<String> expected1 = Edit.builder()
                .substitute("getted")
                .with("got")
                .atPosition(1, 1)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_VERB_INFLECTION);
        assertSingleExpectedError(expected1, source1, target1);
    }

    /**
     * This test fails on spacy-server due to REPLACEMENT_VERB_TENSE
     */
    @Test
    public void morphTier_subjectVerbAgreement() {
        Doc source1 = nlp("I has the money!");
        Doc target1 = nlp("I have the money!");
        Annotation<String> expected1 = Edit.builder()
                .substitute("has")
                .with("have")
                .atPosition(1, 1)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT);
        assertSingleExpectedError(expected1, source1, target1);
    }

    @Test
    public void morphTier_subjectVerbAgreement2() {
        Doc source1 = nlp("Matt like fish.");
        Doc target1 = nlp("Matt likes fish.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("like")
                .with("likes")
                .atPosition(1, 1)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT);
        assertSingleExpectedError(expected1, source1, target1);
    }

    @Test
    void morphTier_subjectVerbAgreement3() {
        Doc source = nlp("If I was you, I would go home.");
        Doc target = nlp("If I were you, I would go home.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("was")
                .with("were")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT);
        assertSingleExpectedError(expected1, source, target);
    }

    /**
     * This test fails on spacy-server due to not+always+good not being merged.
     */
    @Test
    void oneWordDoc() {
        Doc source1 = nlp("are");
        Doc target1 = nlp("Students are not always good.");
        Annotation<String> expected1 = Edit.builder()
                .insert("Students")
                .atPosition(0, 0)
                .transform(Annotation::of)
                .withError(GrammaticalError.MISSING_NOUN);
        Annotation<String> expected2 = Edit.builder()
                .insert("not", "always", "good")
                .atPosition(1, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.MISSING_OTHER);
        Annotation<String> expected3 = Edit.builder()
                .insert(".")
                .atPosition(1, 5)
                .transform(Annotation::of)
                .withError(GrammaticalError.MISSING_PUNCTUATION);
        assertMultipleExpectedErrors(Arrays.asList(expected1, expected2, expected3), source1, target1);
    }

    @Test
    void punctuationOverSpelling() {
        Doc source1 = nlp("Am I early?");
        Doc target1 = nlp("I am not early.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("?")
                .with(".")
                .atPosition(3, 4)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_PUNCTUATION);

        assertContainsError(expected1, source1, target1);
    }

    /**
     * This test fails on spacy-server because wont is split to wo + nt.
     */
    @Test
    void contractionOnMissingApostrophe() {
        Doc source1 = nlp("I wont do that.");
        Doc target1 = nlp("I won't do that.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("wont")
                .with("won't")
                .atPosition(1, 1)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_ORTHOGRAPHY);

        assertContainsError(expected1, source1, target1);
    }

    @Test
    void orth() {
        Doc source1 = nlp("they will do no more");
        Doc target1 = nlp("they won't do anymore work");
        Annotation<String> expected1 = Edit.builder()
                .substitute("will")
                .with("won't")
                .atPosition(1, 1)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_OTHER);

        assertContainsError(expected1, source1, target1);
    }

}
