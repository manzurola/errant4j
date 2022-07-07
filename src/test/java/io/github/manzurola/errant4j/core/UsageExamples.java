package io.github.manzurola.errant4j.core;

import com.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.errors.GrammaticalError;
import io.github.manzurola.errant4j.lang.en.classify.EnClassifier;
import io.github.manzurola.errant4j.lang.en.merge.EnMerger;
import io.github.manzurola.spacy4j.adapters.corenlp.CoreNLPAdapter;
import io.github.manzurola.spacy4j.api.SpaCy;
import io.github.manzurola.spacy4j.api.containers.Doc;
import io.github.manzurola.spacy4j.api.containers.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UsageExamples {

    @Disabled
    @Test
    void annotateEnglish() {
        // Get a spaCy instance (from spacy4j)
        SpaCy spacy = SpaCy.create(CoreNLPAdapter.forEnglish());

        // Create an english annotator
        Annotator annotator = Errant.forEnglish(spacy);

        // Parse source and target sentences
        Doc source = annotator.parse("Yesterday I went to see my therapist.");
        Doc target = annotator.parse("Yesterday I go to see my therapist.");

        // Annotate grammatical errors
        List<Annotation> annotations = annotator.annotate(
            source.tokens(),
            target.tokens()
        );

        // Inspect annotations
        for (Annotation annotation : annotations) {
            GrammaticalError error = annotation.error();
            String sourceText = annotation.sourceText();
            String targetText = annotation.targetText();
            System.out.printf(
                "Error: %s, sourceText: %s, targetText: %s%n",
                error,
                sourceText,
                targetText
            );

            Edit<Token> edit = annotation.edit();
            // Inspect the classified edit...
        }
    }

    @Disabled
    @Test
    void testAndDevelopNewAnnotator() {

        // Get a spaCy instance (from spacy4j)
        SpaCy spacy = SpaCy.create(CoreNLPAdapter.forEnglish());

        // Create an english annotator
        Annotator annotator = Annotator.of(
            spacy,
            new EnMerger(),
            new EnClassifier()
        );

        // Prepare source and target docs
        Doc source = annotator.parse("I am eat dinner.");
        Doc target = annotator.parse("I am eating dinner.");

        // We create an expected string edit and transform it to a
        // Token edit.
        // The string tokens "eat" and "eating" are unnecessary
        // since the Edit projects
        // tokens based on index positions and ignores the values.
        // But it helps with
        // visibility.
        Edit<Token> edit = Edit
            .builder()
            .substitute("eat")
            .with("eating")
            .atPosition(2, 2)
            .project(source.tokens(), target.tokens());

        // Create the expected annotation containing the Edit and
        // GrammaticalError
        Annotation expected = Annotation.of(
            edit,
            GrammaticalError.REPLACEMENT_VERB_FORM
        );


        // Annotated source and target for grammatical errors
        List<Annotation> actual = annotator.annotate(
            source.tokens(),
            target.tokens()
        );

        // Assert that the actual annotations contain our expected
        // error
        Assertions.assertTrue(actual.contains(expected));
    }
}
