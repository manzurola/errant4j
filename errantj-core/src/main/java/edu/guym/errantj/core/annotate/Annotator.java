package edu.guym.errantj.core.annotate;

import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.api.containers.Token;

import java.util.List;

public interface Annotator {

    Doc nlp(String value);

    List<Annotation<Token>> annotate(List<Token> source, List<Token> target);

}
