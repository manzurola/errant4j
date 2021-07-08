package io.languagetoys.errant4j.core;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.Annotation;
import io.languagetoys.errant4j.core.Errant;
import io.languagetoys.errant4j.core.GrammaticalError;
import io.languagetoys.errant4j.lang.en.EnPipeline;
import io.languagetoys.spacy4j.adapters.corenlp.CoreNLPAdapter;
import io.languagetoys.spacy4j.api.SpaCy;
import io.languagetoys.spacy4j.api.containers.Doc;
import io.languagetoys.spacy4j.api.containers.Token;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UsageExamples {

    @Disabled
    @Test
    void annotateParallelEnglishSentences() {
        // Get a spaCy instance (from spacy4j)
        SpaCy spacy = SpaCy.create(CoreNLPAdapter.create());

        // Create an english annotator
        Errant errant = Errant.of(spacy, new EnPipeline());

        // Parse source and target sentences
        Doc source = errant.parse("Yesterday I went to see my therapist.");
        Doc target = errant.parse("Yesterday I go to see my therapist.");

        // Annotate grammatical errors
        List<Annotation> annotations = errant.annotate(source.tokens(), target.tokens());

        // Inspect annotations
        for (Annotation annotation : annotations) {
            GrammaticalError error = annotation.grammaticalError();
            String sourceText = annotation.sourceText();
            String targetText = annotation.targetText();
            System.out.printf("Error: %s, sourceText: %s, targetText: %s%n",
                              error,
                              sourceText,
                              targetText);

            Edit<Token> edit = annotation.edit();
            // Inspect the classified edit...
        }
    }
}
