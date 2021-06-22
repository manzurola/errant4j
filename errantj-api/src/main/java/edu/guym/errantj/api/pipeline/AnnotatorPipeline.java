package edu.guym.errantj.api.pipeline;

import edu.guym.aligner.alignment.Alignment;
import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.api.errors.GrammaticalError;
import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.api.containers.Token;

import java.util.List;

public interface AnnotatorPipeline {

    Doc parse(String text);

    Alignment<Token> align(List<Token> source, List<Token> target);

    List<Edit<Token>> merge(List<Edit<Token>> edits);

    GrammaticalError classify(Edit<Token> edit);

}
