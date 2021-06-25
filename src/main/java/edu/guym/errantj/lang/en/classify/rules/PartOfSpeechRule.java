package edu.guym.errantj.lang.en.classify.rules;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.grammar.GrammaticalError;
import edu.guym.errantj.lang.en.classify.Rule;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.Pos;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * In the majority of cases, an edit may hence be assigned a POS-based classify type if it meets the following conditions:
 * 1. All tokens on both sides of the edit have the same POS tag, and
 * 2. The edit does not meet any criteria for a more specific type.
 */
public class PartOfSpeechRule implements Rule {

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        Set<String> union = edit.stream().map(Token::pos).collect(Collectors.toSet());
        GrammaticalError.Category category = GrammaticalError.Category.OTHER;
        if (union.size() == 1) {
            String pos = union.iterator().next();
            category = mapPosToCategory(pos);
        }
        return GrammaticalError.of(edit, category);
    }

    private GrammaticalError.Category mapPosToCategory(String pos) {
        if (Pos.ADJ.matches(pos)) {
            return GrammaticalError.Category.ADJ;
        }
        if (Pos.ADP.matches(pos)) {
            return GrammaticalError.Category.PREP;
        }
        if (Pos.ADV.matches(pos)) {
            return GrammaticalError.Category.ADV;
        }
        if (Pos.AUX.matches(pos)) {
            return GrammaticalError.Category.VERB_TENSE;
        }
        if (Pos.CCONJ.matches(pos) || Pos.SCONJ.matches(pos)) {
            return GrammaticalError.Category.CONJ;
        }
        if (Pos.DET.matches(pos)) {
            return GrammaticalError.Category.DET;
        }
        if (Pos.NOUN.matches(pos) || Pos.PROPN.matches(pos)) {
            return GrammaticalError.Category.NOUN;
        }
        if (Pos.PART.matches(pos)) {
            return GrammaticalError.Category.PART;
        }
        if (Pos.PRON.matches(pos)) {
            return GrammaticalError.Category.PRON;
        }
        if (Pos.PUNCT.matches(pos)) {
            return GrammaticalError.Category.PUNCT;
        }
        if (Pos.VERB.matches(pos)) {
            return GrammaticalError.Category.VERB;
        }
        return GrammaticalError.Category.OTHER;
    }
}
