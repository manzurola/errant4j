package io.languagetoys.errant4j.lang.en.merge;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.annotator.MergeRule;
import io.languagetoys.errant4j.core.annotator.Merger;
import io.languagetoys.errant4j.lang.en.merge.rules.*;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.List;

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
public class EnMerger implements Merger {

    private final Merger impl;

    public EnMerger() {
        List<MergeRule> rules = List.of(
                new PunctuationAndCaseChangeMergeCondition(),
                new PossessiveSuffixMergeCondition(),
                new WhiteSpaceDifferenceMergeCondition(),
                new SamePosMergeCondition(),
                new ContentWordMergeCondition()
        );
        this.impl = Merger.rules(rules);
    }

    @Override
    public List<Edit<Token>> merge(List<Edit<Token>> edits) {
        return impl.merge(edits);
    }
}
