package io.languagetoys.errant4j.core.annotator;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.function.BiPredicate;

public interface MergeRule extends BiPredicate<Edit<Token>, Edit<Token>> {

    boolean test(Edit<Token> left, Edit<Token> right);
}
