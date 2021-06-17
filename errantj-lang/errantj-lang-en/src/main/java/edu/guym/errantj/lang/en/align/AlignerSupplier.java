package edu.guym.errantj.lang.en.align;

import edu.guym.aligner.Aligner;
import edu.guym.errantj.lang.en.utils.lemmatize.Lemmatizer;
import edu.guym.spacyj.api.containers.Token;

import java.util.Comparator;
import java.util.function.Supplier;

public final class AlignerSupplier implements Supplier<Aligner<Token>> {

    private final Aligner<Token> aligner;

    public static AlignerSupplier create(Lemmatizer lemmatizer) {
        return new AlignerSupplier(lemmatizer);
    }

    private AlignerSupplier(Lemmatizer lemmatizer) {
        this.aligner = Aligner.damerauLevenshtein(
                (source, target) -> source.text().equals(target.text()),
                Comparator.comparing(Token::lower),
                new TokenSubstituteCost(lemmatizer)
        );
    }

    @Override
    public Aligner<Token> get() {
        return aligner;
    }
}
