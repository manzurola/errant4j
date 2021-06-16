package edu.guym.errantj.core.tools;

import edu.guym.spacyj.api.containers.Token;
import edu.guym.aligner.edit.Edit;

import java.util.List;
import java.util.function.Function;

public final class EditTokenizer implements Function<Edit<? super String>, Edit<Token>> {

    private final List<Token> source;
    private final List<Token> target;

    private EditTokenizer(List<Token> source, List<Token> target) {
        this.source = source;
        this.target = target;
    }

    public static EditTokenizer create(List<Token> source, List<Token> target) {
        return new EditTokenizer(source, target);
    }

    @Override
    public Edit<Token> apply(Edit<? super String> edit) {
        return edit.mapSegments(
                source -> source.mapWithIndex(this.source::get),
                target -> target.mapWithIndex(this.target::get)
        );
    }

}
