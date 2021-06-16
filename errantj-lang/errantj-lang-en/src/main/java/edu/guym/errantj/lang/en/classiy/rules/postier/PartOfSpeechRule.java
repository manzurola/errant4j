package edu.guym.errantj.lang.en.classiy.rules.postier;

import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classiy.rules.core.Rule;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.UdPos;
import edu.guym.aligner.edit.Edit;

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
        return GrammaticalError.create(edit, category);
    }

    private GrammaticalError.Category mapPosToCategory(String pos) {
        if (UdPos.ADJ.matches(pos)) {
            return GrammaticalError.Category.ADJ;
        }
        if (UdPos.ADP.matches(pos)) {
            return GrammaticalError.Category.PREP;
        }
        if (UdPos.ADV.matches(pos)) {
            return GrammaticalError.Category.ADV;
        }
        if (UdPos.AUX.matches(pos)) {
            return GrammaticalError.Category.VERB_TENSE;
        }
        if (UdPos.CCONJ.matches(pos) || UdPos.SCONJ.matches(pos)) {
            return GrammaticalError.Category.CONJ;
        }
        if (UdPos.DET.matches(pos)) {
            return GrammaticalError.Category.DET;
        }
        if (UdPos.NOUN.matches(pos) || UdPos.PROPN.matches(pos)) {
            return GrammaticalError.Category.NOUN;
        }
        if (UdPos.PART.matches(pos)) {
            return GrammaticalError.Category.PART;
        }
        if (UdPos.PRON.matches(pos)) {
            return GrammaticalError.Category.PRON;
        }
        if (UdPos.PUNCT.matches(pos)) {
            return GrammaticalError.Category.PUNCT;
        }
        if (UdPos.VERB.matches(pos)) {
            return GrammaticalError.Category.VERB;
        }
        return GrammaticalError.Category.OTHER;
    }
}
