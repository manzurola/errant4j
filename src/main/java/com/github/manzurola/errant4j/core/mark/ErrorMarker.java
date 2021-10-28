package com.github.manzurola.errant4j.core.mark;

import com.github.manzurola.errant4j.core.Annotation;
import com.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;

public interface ErrorMarker {
    MarkedError markError(Annotation annotation, List<Token> source);
}
