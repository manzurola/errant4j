package errant.core.annotate;

import edu.guym.errantj.lang.en.annotate.ErrantEn;
import errant.TestTools;
import edu.guym.errantj.core.annotate.Annotation;
import edu.guym.errantj.core.annotate.Annotator;
import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.spacyj.api.containers.Doc;
import io.squarebunny.aligner.edit.Edit;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class V1AnnotatorTest extends AnnotatorTestBase {

    @Override
    protected Annotator getAnnotator() {
        return ErrantEn.create(TestTools.getSpacy());
    }
    

    @Test
    void posTier_Verb() {
        Doc source = TestTools.parse("  I   like consume food.");
        Doc target = TestTools.parse("I like to eat food.");
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
        Doc source = TestTools.parse("I want in fly");
        Doc target = TestTools.parse("I want to fly");
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
        Doc source = TestTools.parse("Because");
        Doc target = TestTools.parse(", because");
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
        Doc source = TestTools.parse("I 've to go home.");
        Doc target = TestTools.parse("I have to go home.");
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
        Doc source1 = TestTools.parse("My friend sleeps at Home.");
        Doc target1 = TestTools.parse("My friend sleeps at home.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("Home")
                .with("home")
                .atPosition(4, 4)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_ORTHOGRAPHY);
        assertSingleExpectedError(expected1, source1, target1);

        // whitespace
        Doc source2 = TestTools.parse("My friendsleeps at home.");
        Doc target2 = TestTools.parse("My friend sleeps at home.");
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
        Doc source1 = TestTools.parse("My frien sleeps at home.");
        Doc target1 = TestTools.parse("My friend sleeps at home.");
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
        Doc source1 = TestTools.parse("This dog is cute");
        Doc target1 = TestTools.parse("This is cute dog");
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
        Doc source1 = TestTools.parse("This is the most small computer I have ever seen!");
        Doc target1 = TestTools.parse("This is the smallest computer I have ever seen!");
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
        Doc source1 = TestTools.parse("This is the big computer.");
        Doc target1 = TestTools.parse("This is the biggest computer.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("big")
                .with("biggest")
                .atPosition(3, 3)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_ADJECTIVE_FORM);
        assertSingleExpectedError(expected1, source1, target1);
    }

    @Test
    void morphTier_nounNumber() {
        Doc source1 = TestTools.parse("Dog are cute.");
        Doc target1 = TestTools.parse("dogs are cute.");
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
        Doc source1 = TestTools.parse("It is at the river edge");
        Doc target1 = TestTools.parse("It is at the river's edge");
        Annotation<String> expected1 = Edit.builder()
                .insert("'s")
                .atPosition(5, 5)
                .transform(Annotation::of)
                .withError(GrammaticalError.MISSING_NOUN_POSSESSIVE);
        assertSingleExpectedError(expected1, source1, target1);
        Doc source2 = TestTools.parse("It is at the rivers edge");
        Doc target2 = TestTools.parse("It is at the river's edge");
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
        Doc source1 = TestTools.parse("Brent would often became stunned");
        Doc target1 = TestTools.parse("Brent would often become stunned");
        Annotation<String> expected1 = Edit.builder()
                .substitute("became")
                .with("become")
                .atPosition(3, 3)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_VERB_FORM);
        assertSingleExpectedError(expected1, source1, target1);

        Doc source2 = TestTools.parse("is she go home?");
        Doc target2 = TestTools.parse("is she going home?");
        Annotation<String> expected2 = Edit.builder()
                .substitute("go")
                .with("going")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_VERB_FORM);
        assertSingleExpectedError(expected2, source2, target2);

        Doc source3 = TestTools.parse("is she went home");
        Doc target3 = TestTools.parse("is she going home");
        Annotation<String> expected3 = Edit.builder()
                .substitute("went")
                .with("going")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_VERB_FORM);
        assertSingleExpectedError(expected3, source3, target3);

        Doc source4 = TestTools.parse("is she goes home");
        Doc target4 = TestTools.parse("is she going home");
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
        Doc source1 = TestTools.parse("I awaits your response.");
        Doc target1 = TestTools.parse("I await your response.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("awaits")
                .with("await")
                .atPosition(1, 1)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT);
        assertSingleExpectedError(expected1, source1, target1);

        Doc source2 = TestTools.parse("does she goes home?");
        Doc target2 = TestTools.parse("does she go home?");
        Annotation<String> expected2 = Edit.builder()
                .substitute("goes")
                .with("go")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT);
        assertSingleExpectedError(expected2, source2, target2);

        Doc source3 = TestTools.parse("He must tells him everything.");
        Doc target3 = TestTools.parse("He must tell him everything.");
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
        Doc source1 = TestTools.parse("I go to see him yesterday.");
        Doc target1 = TestTools.parse("I went to see him yesterday.");
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
        Doc source1 = TestTools.parse("I am eat dinner.");
        Doc target1 = TestTools.parse("I am eating dinner.");
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
        Doc source1 = TestTools.parse("I would like go home please!");
        Doc target1 = TestTools.parse("I would like to go home please!");
        Annotation<String> expected1 = Edit.builder()
                .insert("to")
                .atPosition(3, 3)
                .transform(Annotation::of)
                .withError(GrammaticalError.MISSING_VERB_FORM);
        assertSingleExpectedError(expected1, source1, target1);
    }

    @Test
    public void morphTier_verbFormError_unnecessaryInfinitivalTo() {
        Doc source1 = TestTools.parse("I must to eat now.");
        Doc target1 = TestTools.parse("I must eat now.");
        Annotation<String> expected1 = Edit.builder()
                .delete("to")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.UNNECESSARY_VERB_FORM);
        assertSingleExpectedError(expected1, source1, target1);
    }

    @Test
    public void morphTier_nounInflection() {
        Doc source1 = TestTools.parse("I have five childs.");
        Doc target1 = TestTools.parse("I have five children.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("childs")
                .with("children")
                .atPosition(3, 3)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_NOUN_INFLECTION);
        assertSingleExpectedError(expected1, source1, target1);
    }

    @Test
    public void morphTier_verbInflection() {
        Doc source1 = TestTools.parse("I getted the money!");
        Doc target1 = TestTools.parse("I got the money!");
        Annotation<String> expected1 = Edit.builder()
                .substitute("getted")
                .with("got")
                .atPosition(1, 1)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_VERB_INFLECTION);
        assertSingleExpectedError(expected1, source1, target1);
    }

    @Test
    public void morphTier_subjectVerbAgreement() {
        Doc source1 = TestTools.parse("I has the money!");
        Doc target1 = TestTools.parse("I have the money!");
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
        Doc source1 = TestTools.parse("Matt like fish.");
        Doc target1 = TestTools.parse("Matt likes fish.");
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
        Doc source = TestTools.parse("If I was you, I would go home.");
        Doc target = TestTools.parse("If I were you, I would go home.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("was")
                .with("were")
                .atPosition(2, 2)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT);
        assertSingleExpectedError(expected1, source, target);
    }

    @Test
    void oneWordDoc() {
        Doc source1 = TestTools.parse("are");
        Doc target1 = TestTools.parse("Students are not always good.");
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
        Doc source1 = TestTools.parse("Am I early?");
        Doc target1 = TestTools.parse("I am not early.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("?")
                .with(".")
                .atPosition(3, 4)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_PUNCTUATION);

        printResult(source1, target1);
        assertContainsError(expected1, source1, target1);
    }

    @Test
    void assertOtherOverVerbTenseWhenUnrecognizedWord() {
        Doc source1 = TestTools.parse("I wont do that.");
        Doc target1 = TestTools.parse("I won't do that.");
        Annotation<String> expected1 = Edit.builder()
                .substitute("?")
                .with(".")
                .atPosition(3, 4)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_PUNCTUATION);

        printResult(source1, target1);
//        assertContainsError(expected1, source1, target1);
    }

    @Test
    void contractionOnMissingApostrophe() {
        Doc source1 = TestTools.parse("I wont do that.");
        Doc target1 = TestTools.parse("I won't do that.");
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
        Doc source1 = TestTools.parse("they will do no more");
        Doc target1 = TestTools.parse("they won't do anymore work");
        Annotation<String> expected1 = Edit.builder()
                .substitute("will")
                .with("won't")
                .atPosition(1, 1)
                .transform(Annotation::of)
                .withError(GrammaticalError.REPLACEMENT_OTHER);

        assertContainsError(expected1, source1, target1);
    }

    @Test
    public void test() {
        Doc source1 = TestTools.parse("Does she swim well...?");
        Doc target1 = TestTools.parse("She doesn't swim well.");
        printResult(source1, target1);
    }

}
