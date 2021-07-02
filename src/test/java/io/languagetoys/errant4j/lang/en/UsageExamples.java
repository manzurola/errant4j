package io.languagetoys.errant4j.lang.en;

import io.languagetoys.errant4j.Errant;
import io.languagetoys.errant4j.core.annotator.Annotation;
import io.languagetoys.errant4j.core.annotator.Annotator;
import io.languagetoys.spacy4j.adapters.corenlp.CoreNLPAdapter;
import io.languagetoys.spacy4j.api.SpaCy;
import io.languagetoys.spacy4j.api.containers.Doc;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UsageExamples {

    @Test
    void annotateParallelEnglishSentences() {
        // Get a spaCy instance (from spacy-java)
        SpaCy spacy = SpaCy.create(CoreNLPAdapter.create());
        // Create an english annotator
        Annotator annotator = Errant.en(spacy);

        // Parse source and target sentences
        Doc source = annotator.parse("Yesterday I went to see my therapist.");
        Doc target = annotator.parse("Yesterday I go to see my therapist.");

        // Annotate grammatical errors
        List<Annotation> annotations = annotator.annotate(source.tokens(), target.tokens());

        // Inspect annotations
        for (Annotation annotation : annotations) {
            System.out.printf("Error: %s, sourceText: %s, targetText: %s%n",
                    annotation.grammaticalError(), annotation.sourceText(), annotation.targetText());
        }
    }
}
