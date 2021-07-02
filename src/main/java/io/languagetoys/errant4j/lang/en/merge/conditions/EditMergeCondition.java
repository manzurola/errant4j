package io.languagetoys.errant4j.lang.en.merge.conditions;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.function.BiPredicate;

public interface EditMergeCondition extends BiPredicate<Edit<Token>, Edit<Token>> {
}
