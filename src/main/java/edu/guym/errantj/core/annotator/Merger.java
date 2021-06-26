package edu.guym.errantj.core.annotator;

import edu.guym.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;

import java.util.List;

public interface Merger {

    /**
     * Merge a list of Edits, producing a new list of equal all smaller size.
     */
    List<Edit<Token>> merge(List<Edit<Token>> edits);
}
