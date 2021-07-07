package io.languagetoys.errant4j.lang.en.classify.rules;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.GrammaticalError;
import io.languagetoys.errant4j.core.annotate.Classifier;
import io.languagetoys.errant4j.lang.en.classify.rules.common.Predicates;
import io.languagetoys.spacy4j.api.containers.Token;
import io.languagetoys.spacy4j.api.features.Dependency;
import io.languagetoys.spacy4j.api.features.Pos;

import java.util.List;
import java.util.Optional;

/**
 * The following special rule differentiates between determiners and pronouns that have the same surface form; e.g. ‘His
 * book’ (DET) vs. ‘This book is his’ (PRON).
 */
public class DetPronRule implements Classifier.Rule {

    /**
     * 1. There is exactly one token on both sides of the edit, and 2. The set of POS tags for these tokens is DET and
     * PRON, and 3. (a) The corrected token dependency label is poss (possessive determiner); i.e. DET, or (b) The
     * corrected token dependency label is nsubj or nsubjpass (nominal subject), dobj (direct object), or pobj
     * (prepositional object); i.e. PRON because determiners cannot be subjects or objects.
     *
     * @return
     */
    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        Optional<Token> target = edit
                .filter(Predicates.isSubstitute())
                .filter(Predicates.ofSizeOneToOne())
                .filter(Predicates.PosTagSetEquals(Pos.DET, Pos.PRON))
                .map(e -> e.target().first());

        if (target.isEmpty()) {
            return GrammaticalError.unknown(edit);
        }

        if (target.filter(Predicates.matchDependency(Dependency.NMOD)).isPresent()) {
            return GrammaticalError.of(edit, GrammaticalError.Category.DET);
        }

        if (target
                .filter(Predicates.matchAnyDependency(List.of(Dependency.NSUBJ_PASS, Dependency.NSUBJ, Dependency.OBJ)))
                .isPresent()) {
            return GrammaticalError.of(edit, GrammaticalError.Category.PRON);
        }

        return GrammaticalError.unknown(edit);
    }

}
