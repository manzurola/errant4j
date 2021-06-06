package edu.guym.errantj.core.merge;

import io.squarebunny.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;

import java.util.List;

/**
 * An annotation pipeline element responsible for merging adjacent edits.
 */
public interface Merger {

    /**
     * Merges applicable consecutive edits according to the policy defined by implementations.
     * @param edits the target list of edits to merge.
     * @return a list of (possibly) merged edits, or the original list if no edits were merged.
     */
    List<Edit<Token>> merge(List<Edit<Token>> edits);
}
