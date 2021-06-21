package edu.guym.errantj.api.tools.mark;

import edu.guym.aligner.edit.*;
import edu.guym.spacyj.api.containers.Token;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ErrorMarker implements EditVisitor<Token, CharOffset> {

    private final List<Token> source;

    public ErrorMarker(List<Token> source) {
        this.source = source;
    }

    @Override
    public CharOffset visit(EqualEdit<Token> edit) {
        return markNotInsert(edit);
    }

    @Override
    public CharOffset visit(InsertEdit<Token> edit) {
        return markInsert(edit);
    }

    @Override
    public CharOffset visit(DeleteEdit<Token> edit) {
        return markNotInsert(edit);
    }

    @Override
    public CharOffset visit(SubstituteEdit<Token> edit) {
        return markNotInsert(edit);
    }

    @Override
    public CharOffset visit(TransposeEdit<Token> edit) {
        return markNotInsert(edit);
    }

    private CharOffset markNotInsert(Edit<Token> edit) {
        Segment<Token> source = edit.source();
        int start = source.first().charStart();
        int end = source.last().charEnd();
        return new CharOffset(start, end);
    }

    private CharOffset markInsert(Edit<Token> edit) {
        // source in edit is empty
        int beforeIndex = edit.source().position() - 1;
        int afterIndex = edit.source().position();

        Optional<Token> before = getOptionalToken(beforeIndex, source);
        Optional<Token> after = getOptionalToken(afterIndex, source);

        // if has before and whitespaceAfter tokens, mark both
        // else if has before, get before
        // else if has whitespaceAfter, get whitespaceAfter
        // else - empty source sentence, return 0,0

        if (before.isPresent() && after.isPresent()) {
            return new CharOffset(
                    before.get().charStart(),
                    after.get().charEnd()
            );
        } else if (before.isPresent()) {
            return new CharOffset(
                    before.get().charStart(),
                    before.get().charEnd()
            );
        } else if (after.isPresent()) {
            return new CharOffset(
                    after.get().charStart(),
                    after.get().charEnd()
            );
        } else {
            // edge case, insert into empty sentence
            return new CharOffset(0, 0);
        }
    }

    private Optional<Token> getOptionalToken(int index, List<Token> tokens) {
        try {
            Objects.checkIndex(index, tokens.size());
            return Optional.of(tokens.get(index));
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }
}
