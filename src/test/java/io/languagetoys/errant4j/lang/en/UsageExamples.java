package io.languagetoys.errant4j.lang.en;

import io.languagetoys.errant4j.core.Annotation;
import io.languagetoys.errant4j.core.Errant;
import io.languagetoys.spacy4j.adapters.corenlp.CoreNLPAdapter;
import io.languagetoys.spacy4j.api.SpaCy;
import io.languagetoys.spacy4j.api.containers.Doc;
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
        Errant errant = Errant.en(spacy);

        // Parse source and target sentences
        Doc source = errant.parse("Yesterday I went to see my therapist.");
        Doc target = errant.parse("Yesterday I go to see my therapist.");

        // Annotate grammatical errors
        List<Annotation> annotations = errant.annotate(source.tokens(), target.tokens());

        // Inspect annotations
        for (Annotation annotation : annotations) {
            System.out.printf("Error: %s, sourceText: %s, targetText: %s%n",
                              annotation.grammaticalError(), annotation.sourceText(), annotation.targetText());
        }
    }
}
