package edu.guym.errantj.api.annotate;

import edu.guym.aligner.alignment.Alignment;
import edu.guym.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.api.containers.Token;

import java.util.List;
import java.util.stream.Collectors;

public interface Errant {

    Doc parse(String text);

    List<Annotation<Token>> annotate(List<Token> source, List<Token> target);

    static Errant create(AnnotatorPipeline pipeline) {
        return new Errant() {
            @Override
            public Doc parse(String text) {
                return pipeline.parse(text);
            }

            @Override
            public List<Annotation<Token>> annotate(List<Token> source, List<Token> target) {
                Alignment<Token> alignment = pipeline.align(source, target);
                List<Edit<Token>> merged = pipeline.merge(alignment.edits());
                return merged.stream()
                        .map(edit -> Annotation.of(edit).withError(pipeline.classify(edit)))
                        .collect(Collectors.toList());
            }
        };
    }

}
