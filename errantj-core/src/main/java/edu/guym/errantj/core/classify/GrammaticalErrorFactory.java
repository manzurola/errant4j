package edu.guym.errantj.core.classify;

import io.squarebunny.aligner.edit.Edit;
import io.squarebunny.aligner.edit.Operation;

public final class GrammaticalErrorFactory {

    private enum SingletonFactory {
        INSTANCE(new GrammaticalErrorFactory());

        private final GrammaticalErrorFactory factory;

        SingletonFactory(GrammaticalErrorFactory factory) {
            this.factory = factory;
        }

        public final GrammaticalErrorFactory factory() {
            return factory;
        }
    }

    public final GrammaticalError create(Category category, Edit<?> edit) {
        return create(category, translateEditOperation(edit.operation()));
    }

    public final GrammaticalError createUnknown(Edit<?> edit) {
        return create(Category.OTHER, edit);
    }

    public static GrammaticalErrorFactory getInstance() {
        return SingletonFactory.INSTANCE.factory;
    }

    private GrammaticalError.Type translateEditOperation(Operation operation) {
        switch (operation) {
            case INSERT:
                return GrammaticalError.Type.MISSING;
            case DELETE:
                return GrammaticalError.Type.UNNECESSARY;
            case SUBSTITUTE:
            case TRANSPOSE:
                return GrammaticalError.Type.REPLACEMENT;
            case EQUAL:
            default:
                return GrammaticalError.Type.NONE;
        }
    }

    public final GrammaticalError create(Category category, GrammaticalError.Type type) {
        for (GrammaticalError value : GrammaticalError.values()) {
            if (value.type().equals(type) && value.category().equals(category)) {
                return value;
            }
        }
        throw new RuntimeException(String.format(
                "Could not match Grammatical Error for Op[%s] and Cat[%s]", type, category
        ));
    }
}
