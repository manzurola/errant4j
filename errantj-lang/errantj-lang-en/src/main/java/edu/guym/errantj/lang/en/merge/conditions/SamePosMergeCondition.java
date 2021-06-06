package edu.guym.errantj.lang.en.merge.conditions;

import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.UdPos;
import io.squarebunny.aligner.edit.Edit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Merge any consecutive operations that involve tokens with the same POS;
 * e.g. [(look) at → (look) up] + [ε → to] = [(look) at → (look) up to].
 */
public class SamePosMergeCondition implements EditMergeCondition {

    private static final Set<UdPos> expectedPos = new HashSet<>(Arrays.asList(UdPos.AUX, UdPos.PART, UdPos.VERB));

    @Override
    public boolean test(Edit<Token> left, Edit<Token> right) {
        Set<UdPos> leftPos = left.stream()
                .map(Token::pos)
                .map(UdPos::ofTag)
                .collect(Collectors.toSet());
        Set<UdPos> rightPos = right.stream()
                .map(Token::pos)
                .map(UdPos::ofTag)
                .collect(Collectors.toSet());
        Set<UdPos> posSet = new HashSet<>(leftPos);
        posSet.addAll(rightPos);

        if (posSet.size() == 1) { //edits share same pos
            return true;
        }
        // if set is subset of expected
        int count = 0;
        for (UdPos expected : expectedPos) {
            if (posSet.contains(expected)) count++;
        }
        return count == posSet.size();
    }

}
