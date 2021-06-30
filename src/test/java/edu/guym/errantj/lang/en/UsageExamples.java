package edu.guym.errantj.lang.en;

import edu.guym.errantj.Errant;
import edu.guym.errantj.core.annotator.Annotation;
import edu.guym.errantj.core.annotator.Annotator;
import edu.guym.spacyj.adapters.corenlp.CoreNlpAdapter;
import edu.guym.spacyj.api.SpaCy;
import edu.guym.spacyj.api.containers.Doc;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UsageExamples {

    @Test
    void annotateParallelEnglishSentences() {
        // Get a spaCy instance (from spacy-java)
        SpaCy spacy = SpaCy.create(CoreNlpAdapter.create());
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
