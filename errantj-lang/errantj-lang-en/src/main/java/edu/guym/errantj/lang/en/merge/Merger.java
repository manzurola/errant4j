package edu.guym.errantj.lang.en.merge;

import edu.guym.aligner.edit.Edit;
import edu.guym.aligner.edit.EqualEdit;
import edu.guym.aligner.edit.TransposeEdit;
import edu.guym.errantj.lang.en.merge.conditions.*;
import edu.guym.spacyj.api.containers.Token;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * <p>As described in <a href="https://github.com/chrisjbryant/errant/blob/master/errant/en/merger.py">
 * https://github.com/chrisjbryant/errant/blob/master/errant/en/merger.py</a></p>
 *
 * <p>Follows these rules:</p>
 * 1. Split a sequence into subsequences of matches (M) and non-matches and only process the non-matches; e.g. MDDSMMTMSI becomes DDS, T and SI.
 * <br>
 * 2. Merge any adjacent operations that consist of punctuation followed by a case change; e.g. [,→.] +[we→We]=[,we→. We].
 * <br>
 * 3. Split transpositions (T) from all other sequences of non-matches; e.g. IITSS becomes II, T and SS.
 * <br>
 * 4. Merge any adjacent operations that consist of a possessive suffix preceded by anything else; e.g. [friends → friend] + [ε → ’s] = [friends → friend ’s].
 * <br>
 * 5. Merge any adjacent operations that differ only in terms of whitespace; e.g. [sub → subway] + [way → ε] = [sub way → subway].
 * <br>
 * 6. Split substitutions (S) that share > 70% of the same characters, e.g. [writting → writing], unless they have the same POS as the previous alignment. This exception prevents edits such as [eated → have eaten] being split into [ε → have] + [eated → eaten].
 * <br>
 * 7. Split substitutions that are preceded by other substitutions; e.g. DDSSII becomes DDS, S and II.
 * <br>
 * 8. Merge any consecutive operations that involve at least one content word; e.g. [On → In] + [the → ε] + [other → ε] + [hand → addition] = [On the other hand → In addition].
 * <br>
 * 9. Merge any consecutive operations that involve tokens with the same POS; e.g. [(look) at → (look) up] + [ε → to] = [(look) at → (look) up to].
 * <br>
 * 10. Split any determiner edits at the end of a sequence; e.g. [saw → seen the] becomes [saw → seen] + [ε → the].
 * <br>
 */
public class Merger {

    public List<Edit<Token>> merge(List<Edit<Token>> edits) {
        List<Edit<Token>> unmergeable = new ArrayList<>();
        List<Edit<Token>> result = new ArrayList<>(edits);
        boolean moreToMerge = true;
        while (moreToMerge) {
            moreToMerge = false;
            unmergeable.addAll(filterUnmergeable(result));
            result.removeAll(unmergeable);

            List<Edit<Token>> applied = applyConditions(result);
            if (!equalsUnique(result, applied)) {
                result = applied;
                moreToMerge = true;
            }
        }
        result.addAll(unmergeable);
        return sort(result);
    }

    private boolean equalsUnique(List<Edit<Token>> a, List<Edit<Token>> b) {
        return new HashSet<>(a).equals(new HashSet<>(b));
    }

    private List<Edit<Token>> filterUnmergeable(List<Edit<Token>> edits) {
        return edits.stream()
                .filter(e -> e instanceof TransposeEdit || e instanceof EqualEdit)
                .collect(Collectors.toList());
    }

    public List<Edit<Token>> applyConditions(List<Edit<Token>> edits) {

        if (edits.isEmpty() || edits.size() == 1) {
            return edits;
        }

        LinkedList<Edit<Token>> sorted = sort(edits);
        List<Edit<Token>> merged = new ArrayList<>();
        Edit<Token> current = sorted.pop();

        BiPredicate<Edit<Token>, Edit<Token>> conditions = getConditions();
        while (!sorted.isEmpty()) {
            Edit<Token> next = sorted.pop();
            if (current.isLeftSiblingOf(next) && conditions.test(current, next)) {
                current = current.mergeWith(next);
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);
        return merged;
    }

    private BiPredicate<Edit<Token>, Edit<Token>> getConditions() {
        return new PunctuationAndCaseChangeMergeCondition()
                .or(
                        new PossessiveSuffixMergeCondition())
                .or(
                        new WhiteSpaceDifferenceMergeCondition())
                .or(
                        new SamePosMergeCondition())
                .or(
                        new ContentWordMergeCondition()
                );
    }

    private LinkedList<Edit<Token>> sort(List<Edit<Token>> edits) {
        LinkedList<Edit<Token>> sorted = new LinkedList<>(edits);
        Collections.sort(sorted);
        return sorted;
    }

}
