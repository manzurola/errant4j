package edu.guym.errantj.lang.en.merge.conditions;

import io.squarebunny.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;

import java.util.function.BiPredicate;

public interface EditMergeCondition extends BiPredicate<Edit<Token>, Edit<Token>> {
}
