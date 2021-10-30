package com.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.classify.ClassificationRule;
import com.github.manzurola.errant4j.core.errors.ErrorCategory;
import com.github.manzurola.errant4j.core.errors.GrammaticalError;
import com.github.manzurola.spacy4j.api.containers.Token;
import com.github.manzurola.spacy4j.api.features.Pos;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * In the majority of cases, an edit may hence be assigned a POS-based classify type if it meets the following
 * conditions: 1. All tokens on both sides of the edit have the same POS tag, and 2. The edit does not meet any criteria
 * for a more specific type.
 */
public class PartOfSpeechRule implements ClassificationRule {

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        Set<String> union = edit.stream().map(Token::pos).collect(Collectors.toSet());
        ErrorCategory category = ErrorCategory.OTHER;
        if (union.size() == 1) {
            String pos = union.iterator().next();
            category = mapPosToCategory(pos);
        }
        return GrammaticalError.of(edit, category);
    }

    private ErrorCategory mapPosToCategory(String pos) {
        if (Pos.ADJ.matches(pos)) {
            return ErrorCategory.ADJ;
        }
        if (Pos.ADP.matches(pos)) {
            return ErrorCategory.PREP;
        }
        if (Pos.ADV.matches(pos)) {
            return ErrorCategory.ADV;
        }
        if (Pos.AUX.matches(pos)) {
            return ErrorCategory.VERB_TENSE;
        }
        if (Pos.CCONJ.matches(pos) || Pos.SCONJ.matches(pos)) {
            return ErrorCategory.CONJ;
        }
        if (Pos.DET.matches(pos)) {
            return ErrorCategory.DET;
        }
        if (Pos.NOUN.matches(pos) || Pos.PROPN.matches(pos)) {
            return ErrorCategory.NOUN;
        }
        if (Pos.PART.matches(pos)) {
            return ErrorCategory.PART;
        }
        if (Pos.PRON.matches(pos)) {
            return ErrorCategory.PRON;
        }
        if (Pos.PUNCT.matches(pos)) {
            return ErrorCategory.PUNCT;
        }
        if (Pos.VERB.matches(pos)) {
            return ErrorCategory.VERB;
        }
        return ErrorCategory.OTHER;
    }
}
