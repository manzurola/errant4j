package edu.guym.errantj.lang.en.classiy.rules.postier;

import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classiy.rules.core.Rule;
import edu.guym.spacyj.api.features.Dependency;
import edu.guym.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.UdPos;

import java.util.List;
import java.util.Optional;

import static edu.guym.errantj.lang.en.classiy.common.TokenEditPredicates.udPosTagSetEquals;
import static edu.guym.errantj.lang.en.classiy.common.TokenPredicates.matchAnyDependency;
import static edu.guym.errantj.lang.en.classiy.common.TokenPredicates.matchDependency;
import static edu.guym.aligner.edit.predicates.EditPredicates.isSubstitute;
import static edu.guym.aligner.edit.predicates.EditPredicates.ofSizeOneToOne;

/**
 * The following special rule differentiates between determiners and pronouns that have the same surface form;
 * e.g. ‘His book’ (DET) vs. ‘This book is his’ (PRON).
 */
public class DetPronRule implements Rule {

    /**
     * 1. There is exactly one token on both sides of the edit, and
     * 2. The set of POS tags for these tokens is DET and PRON, and
     * 3. (a) The corrected token dependency label is poss (possessive determiner); i.e. DET, or
     * (b) The corrected token dependency label is nsubj or nsubjpass (nominal subject), dobj (direct object),
     * or pobj (prepositional object); i.e. PRON because determiners cannot be subjects or objects.
     *
     * @return
     */
    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        Optional<Token> target = edit
                .filter(isSubstitute())
                .filter(ofSizeOneToOne())
                .filter(udPosTagSetEquals(UdPos.DET, UdPos.PRON))
                .map(e -> e.target().first());

        if (target.isEmpty()) {
            return GrammaticalError.unknown(edit);
        }

        if (target.filter(matchDependency(Dependency.NMOD)).isPresent()) {
            return GrammaticalError.create(edit, GrammaticalError.Category.DET);
        }

        if (target.filter(matchAnyDependency(List.of(Dependency.NSUBJ_PASS, Dependency.NSUBJ, Dependency.OBJ)))
                .isPresent()) {
            return GrammaticalError.create(edit, GrammaticalError.Category.PRON);
        }

        return GrammaticalError.unknown(edit);
    }

}
