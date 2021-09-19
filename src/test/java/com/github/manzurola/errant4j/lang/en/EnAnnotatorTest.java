package com.github.manzurola.errant4j.lang.en;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.Annotation;
import com.github.manzurola.errant4j.core.Annotator;
import com.github.manzurola.errant4j.core.GrammaticalError;
import com.github.manzurola.errant4j.lang.en.classify.EnClassifier;
import com.github.manzurola.errant4j.lang.en.merge.EnMerger;
import com.github.manzurola.spacy4j.adapters.corenlp.CoreNLPAdapter;
import com.github.manzurola.spacy4j.api.SpaCy;
import com.github.manzurola.spacy4j.api.containers.Doc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnAnnotatorTest {

    private static final Logger logger = LoggerFactory.getLogger(EnAnnotatorTest.class);
    private Annotator annotator;

    @BeforeAll
    void setup() {
        SpaCy spacy = SpaCy.create(CoreNLPAdapter.create());
        this.annotator = Annotator.of(spacy, new EnMerger(), new EnClassifier());
    }

    @Test
    void posTier_Verb() {
        Doc source = nlp("  I   like consume food.");
        Doc target = nlp("I like to eat food.");
        Annotation expected = Edit.builder()
                .substitute("consume")
                .with("to", "eat")
                .atPosition(2, 2)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_VERB));
        List<Annotation> actual = annotate(source, target);
        assertEquals(List.of(expected), actual);
    }

    @Test
    void posTier_Part() {
        Doc source = nlp("I want in fly");
        Doc target = nlp("I want to fly");
        Annotation expected = Edit.builder()
                .substitute("in")
                .with("to")
                .atPosition(2, 2)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_PARTICLE));
        assertSingleError(expected, source, target);
    }

    @Test
    void posTier_PunctuationEffect() {
        Doc source = nlp("Because");
        Doc target = nlp(", because");
        Annotation expected = Edit.builder()
                .substitute("Because")
                .with(",", "because")
                .atPosition(0, 0)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_PUNCTUATION));
        assertSingleError(expected, source, target);
    }

    @Test
    void tokenTier_Contractions() {
        Doc source = nlp("I've to go home.");
        Doc target = nlp("I have to go home.");
        Annotation expected = Edit.builder()
                .substitute("'ve")
                .with("have")
                .atPosition(1, 1)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_CONTRACTION));
        assertSingleError(expected, source, target);
    }

    @Test
    void tokenTier_Orthography_case() {
        Doc source = nlp("My friend sleeps at Home.");
        Doc target = nlp("My friend sleeps at home.");
        Annotation expected1 = Edit.builder()
                .substitute("Home")
                .with("home")
                .atPosition(4, 4)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_ORTHOGRAPHY));
        assertSingleError(expected1, source, target);
    }

    @Test
    void tokenTier_Orthography_whitespace() {
        Doc source = nlp("My friendsleeps at home.");
        Doc target = nlp("My friend sleeps at home.");
        Annotation expected2 = Edit.builder()
                .substitute("friendsleeps")
                .with("friend", "sleeps")
                .atPosition(1, 1)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_ORTHOGRAPHY));
        assertSingleError(expected2, source, target);
    }

    @Test
    void tokenTier_Spelling() {
        Doc source = nlp("My frien sleeps at home.");
        Doc target = nlp("My friend sleeps at home.");
        Annotation expected1 = Edit.builder()
                .substitute("frien")
                .with("friend")
                .atPosition(1, 1)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_SPELLING));
        assertSingleError(expected1, source, target);
    }

    @Test
    void tokenTier_WordOrder() {
        Doc source = nlp("This dog is cute");
        Doc target = nlp("This is cute dog");
        Annotation expected1 = Edit.builder()
                .transpose("dog", "is", "cute")
                .to("is", "cute", "dog")
                .atPosition(1, 1)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_WORD_ORDER));
        assertSingleError(expected1, source, target);
    }

    @Test
    void morphTier_AdjectiveForm() {
        Doc source = nlp("This is the most small computer I have ever seen!");
        Doc target = nlp("This is the smallest computer I have ever seen!");
        Annotation expected2 = Edit.builder()
                .substitute("most", "small")
                .with("smallest")
                .atPosition(3, 3)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_ADJECTIVE_FORM));
        assertSingleError(expected2, source, target);
    }

    @Test
    void morphTier_AdjectiveForm2() {
        Doc source = nlp("This is the big computer.");
        Doc target = nlp("This is the biggest computer.");
        Annotation expected1 = Edit.builder()
                .substitute("big")
                .with("biggest")
                .atPosition(3, 3)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_ADJECTIVE_FORM));
        assertSingleError(expected1, source, target);
    }

    @Test
    void morphTier_nounNumber() {
        Doc source = nlp("Dog are cute.");
        Doc target = nlp("dogs are cute.");
        Annotation expected1 = Edit.builder()
                .substitute("Dog")
                .with("dogs")
                .atPosition(0, 0)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_NOUN_NUMBER));
        assertSingleError(expected1, source, target);
    }

    @Test
    void morphTier_nounPossessive_singular() {
        Doc source = nlp("It is at the river edge");
        Doc target = nlp("It is at the river's edge");
        Annotation expected1 = Edit.builder()
                .insert("'s")
                .atPosition(5, 5)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.MISSING_NOUN_POSSESSIVE));
        assertSingleError(expected1, source, target);
    }

    @Test
    void morphTier_nounPossessive_plural() {
        Doc source = nlp("It is at the rivers edge");
        Doc target = nlp("It is at the river's edge");
        Annotation expected2 = Edit.builder()
                .substitute("rivers")
                .with("river", "'s")
                .atPosition(4, 4)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_NOUN_POSSESSIVE));
        assertSingleError(expected2, source, target);
    }

    @Test
    public void morphTier_verbFormSubstitutionError() {
        Doc source2 = nlp("is she go home?");
        Doc target2 = nlp("is she going home?");
        Annotation expected2 = Edit.builder()
                .substitute("go")
                .with("going")
                .atPosition(2, 2)
                .project(source2.tokens(), target2.tokens())
                .transform(edit2 -> Annotation.of(edit2, GrammaticalError.REPLACEMENT_VERB_FORM));
        assertSingleError(expected2, source2, target2);
    }

    @Test
    public void morphTier_verbAgreementSubstitutionError_1() {
        Doc source = nlp("I awaits your response.");
        Doc target = nlp("I await your response.");
        Annotation expected1 = Edit.builder()
                .substitute("awaits")
                .with("await")
                .atPosition(1, 1)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT));
        assertSingleError(expected1, source, target);
    }

    @Test
    public void morphTier_verbAgreementSubstitutionError_2() {
        Doc source = nlp("does she goes home?");
        Doc target = nlp("does she go home?");
        Annotation expected2 = Edit.builder()
                .substitute("goes")
                .with("go")
                .atPosition(2, 2)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT));
        assertSingleError(expected2, source, target);
    }

    @Test
    public void morphTier_verbAgreementSubstitutionError_3() {
        Doc source = nlp("He must tells him everything.");
        Doc target = nlp("He must tell him everything.");
        Annotation expected3 = Edit.builder()
                .substitute("tells")
                .with("tell")
                .atPosition(2, 2)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT));
        assertSingleError(expected3, source, target);
    }

    @Test
    public void morphTier_verbAgreementSubstitutionError_4() {
        Doc source = nlp("is she goes home");
        Doc target = nlp("is she going home");
        Annotation expected = Edit.builder()
                .substitute("goes")
                .with("going")
                .atPosition(2, 2)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT));
        assertSingleError(expected, source, target);
    }

    @Test
    public void morphTier_verbTenseError() {
        Doc source = nlp("I go to see him yesterday.");
        Doc target = nlp("I went to see him yesterday.");
        Annotation expected1 = Edit.builder()
                .substitute("go")
                .with("went")
                .atPosition(1, 1)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_VERB_TENSE));
        assertSingleError(expected1, source, target);
    }

    @Test
    public void morphTier_verbTenseError2() {
        Doc source = nlp("Brent would often became stunned");
        Doc target = nlp("Brent would often become stunned");
        Annotation expected = Edit.builder()
                .substitute("became")
                .with("become")
                .atPosition(3, 3)
                .project(source.tokens(), target.tokens())
                .transform(edit3 -> Annotation.of(edit3, GrammaticalError.REPLACEMENT_VERB_TENSE));
        assertSingleError(expected, source, target);
    }

    @Test
    public void morphTier_verbFormError_basic() {
        Doc source = nlp("I am eat dinner.");
        Doc target = nlp("I am eating dinner.");
        Annotation expected1 = Edit.builder()
                .substitute("eat")
                .with("eating")
                .atPosition(2, 2)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_VERB_FORM));
        assertSingleError(expected1, source, target);
    }

    @Test
    public void morphTier_verbFormError_missingInfinitivalTo() {
        Doc source = nlp("I would like go home please!");
        Doc target = nlp("I would like to go home please!");
        Annotation expected1 = Edit.builder()
                .insert("to")
                .atPosition(3, 3)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.MISSING_VERB_FORM));
        assertSingleError(expected1, source, target);
    }

    @Test
    public void morphTier_verbFormError_unnecessaryInfinitivalTo() {
        Doc source = nlp("I must to eat now.");
        Doc target = nlp("I must eat now.");
        Annotation expected1 = Edit.builder()
                .delete("to")
                .atPosition(2, 2)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.UNNECESSARY_VERB_FORM));
        assertSingleError(expected1, source, target);
    }

    @Test
    public void morphTier_nounInflection() {
        Doc source = nlp("I have five childs.");
        Doc target = nlp("I have five children.");
        Annotation expected1 = Edit.builder()
                .substitute("childs")
                .with("children")
                .atPosition(3, 3)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_NOUN_INFLECTION));
        assertSingleError(expected1, source, target);
    }

    /**
     * This test fails on spacy-server with REPLACEMENT_VERB, which may actually make sense
     */
    @Test
    public void morphTier_verbInflection() {
        Doc source = nlp("I getted the money!");
        Doc target = nlp("I got the money!");
        Annotation expected1 = Edit.builder()
                .substitute("getted")
                .with("got")
                .atPosition(1, 1)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_VERB_INFLECTION));
        assertSingleError(expected1, source, target);
    }

    /**
     * This test fails on spacy-server due to REPLACEMENT_VERB_TENSE
     */
    @Test
    public void morphTier_subjectVerbAgreement() {
        Doc source = nlp("I has the money!");
        Doc target = nlp("I have the money!");
        Annotation expected1 = Edit.builder()
                .substitute("has")
                .with("have")
                .atPosition(1, 1)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT));
        assertSingleError(expected1, source, target);
    }

    @Test
    public void morphTier_subjectVerbAgreement2() {
        Doc source = nlp("Matt like fish.");
        Doc target = nlp("Matt likes fish.");
        Annotation expected1 = Edit.builder()
                .substitute("like")
                .with("likes")
                .atPosition(1, 1)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT));
        assertSingleError(expected1, source, target);
    }

    @Test
    void morphTier_subjectVerbAgreement3() {
        Doc source = nlp("If I was you, I would go home.");
        Doc target = nlp("If I were you, I would go home.");
        Annotation expected1 = Edit.builder()
                .substitute("was")
                .with("were")
                .atPosition(2, 2)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_SUBJECT_VERB_AGREEMENT));
        assertSingleError(expected1, source, target);
    }

    /**
     * This test fails on spacy-server due to not+always+good not being merged.
     */
    @Test
    void oneWordDoc() {
        Doc source = nlp("are");
        Doc target = nlp("Students are not always good.");
        Annotation expected1 = Edit.builder()
                .insert("Students")
                .atPosition(0, 0)
                .project(source.tokens(), target.tokens())
                .transform(edit2 -> Annotation.of(edit2, GrammaticalError.MISSING_NOUN));
        Annotation expected2 = Edit.builder()
                .insert("not", "always", "good")
                .atPosition(1, 2)
                .project(source.tokens(), target.tokens())
                .transform(edit1 -> Annotation.of(edit1, GrammaticalError.MISSING_OTHER));
        Annotation expected3 = Edit.builder()
                .insert(".")
                .atPosition(1, 5)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.MISSING_PUNCTUATION));
        assertAllErrors(Arrays.asList(expected1, expected2, expected3), source, target);
    }

    @Test
    void punctuationOverSpelling() {
        Doc source = nlp("Am I early?");
        Doc target = nlp("I am not early.");
        Annotation expected1 = Edit.builder()
                .substitute("?")
                .with(".")
                .atPosition(3, 4)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_PUNCTUATION));
        assertContainsError(expected1, source, target);
    }

    /**
     * This test fails on spacy-server because wont is split to wo + nt.
     */
    @Test
    void contractionOnMissingApostrophe() {
        Doc source = nlp("I wont do that.");
        Doc target = nlp("I won't do that.");
        Annotation expected1 = Edit.builder()
                .substitute("wont")
                .with("won't")
                .atPosition(1, 1)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_ORTHOGRAPHY));
        assertContainsError(expected1, source, target);
    }

    @Test
    void orth() {
        Doc source = nlp("they will do no more");
        Doc target = nlp("they won't do anymore work");
        Annotation expected1 = Edit.builder()
                .substitute("will")
                .with("won't")
                .atPosition(1, 1)
                .project(source.tokens(), target.tokens())
                .transform(edit -> Annotation.of(edit, GrammaticalError.REPLACEMENT_OTHER));

        assertContainsError(expected1, source, target);
    }

    void assertSingleError(Annotation expected, Doc source, Doc target) {
        List<Annotation> actual = annotate(source, target);
        assertEquals(List.of(expected), actual);
    }

    void assertContainsError(Annotation expected, Doc source, Doc target) {
        List<Annotation> actual = annotate(source, target);
        if (actual.isEmpty()) {
            throw new AssertionError("Could not matchError expected " +
                                     expected +
                                     ".\nSource: " +
                                     source +
                                     "\nTarget: " +
                                     target);
        }
        try {
            assertTrue(actual.contains(expected));
        } catch (AssertionError e) {
            logger.info(source.toString());
            logger.info(target.toString());
            throw e;
        }
    }

    void assertAllErrors(List<Annotation> expected, Doc source, Doc target) {
        List<Annotation> actual = annotate(source, target);
        if (actual.isEmpty()) {
            throw new AssertionError("Could not matchError expected " +
                                     expected +
                                     ".\nSource: " +
                                     source +
                                     "\nTarget: " +
                                     target);
        }
        try {
            assertEquals(expected, actual);
        } catch (AssertionError e) {
            logger.info(source.toString());
            logger.info(target.toString());
            throw e;
        }
    }

    final Doc nlp(String text) {
        return annotator.parse(text);
    }

    private List<Annotation> annotate(Doc source, Doc target) {
        return annotator
                .annotate(source.tokens(), target.tokens())
                .stream()
                .filter(annotation -> !annotation.grammaticalError().isNone())
                .filter(annotation -> !annotation.grammaticalError().isIgnored())
                .collect(Collectors.toList());
    }

}
