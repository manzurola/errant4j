package io.github.manzurola.errant4j.core.errors;

import io.github.manzurola.aligner.edit.Edit;

public enum GrammaticalError {

    MISSING_ADJECTIVE(ErrorType.MISSING, ErrorCategory.ADJ),
    UNNECESSARY_ADJECTIVE(ErrorType.UNNECESSARY, ErrorCategory.ADJ),
    REPLACEMENT_ADJECTIVE(ErrorType.REPLACEMENT, ErrorCategory.ADJ),
    MISSING_ADVERB(ErrorType.MISSING, ErrorCategory.ADV),
    UNNECESSARY_ADVERB(ErrorType.UNNECESSARY, ErrorCategory.ADV),
    REPLACEMENT_ADVERB(ErrorType.REPLACEMENT, ErrorCategory.ADV),
    MISSING_CONJUNCTION(ErrorType.MISSING, ErrorCategory.CONJ),
    UNNECESSARY_CONJUNCTION(ErrorType.UNNECESSARY, ErrorCategory.CONJ),
    REPLACEMENT_CONJUNCTION(ErrorType.REPLACEMENT, ErrorCategory.CONJ),
    MISSING_DETERMINER(ErrorType.MISSING, ErrorCategory.DET),
    UNNECESSARY_DETERMINER(ErrorType.UNNECESSARY, ErrorCategory.DET),
    REPLACEMENT_DETERMINER(ErrorType.REPLACEMENT, ErrorCategory.DET),
    MISSING_NOUN(ErrorType.MISSING, ErrorCategory.NOUN),
    UNNECESSARY_NOUN(ErrorType.UNNECESSARY, ErrorCategory.NOUN),
    REPLACEMENT_NOUN(ErrorType.REPLACEMENT, ErrorCategory.NOUN),
    MISSING_PARTICLE(ErrorType.MISSING, ErrorCategory.PART),
    UNNECESSARY_PARTICLE(ErrorType.UNNECESSARY, ErrorCategory.PART),
    REPLACEMENT_PARTICLE(ErrorType.REPLACEMENT, ErrorCategory.PART),
    MISSING_PREPOSITION(ErrorType.MISSING, ErrorCategory.PREP),
    UNNECESSARY_PREPOSITION(ErrorType.UNNECESSARY, ErrorCategory.PREP),
    REPLACEMENT_PREPOSITION(ErrorType.REPLACEMENT, ErrorCategory.PREP),
    MISSING_PRONOUN(ErrorType.MISSING, ErrorCategory.PRON),
    UNNECESSARY_PRONOUN(ErrorType.UNNECESSARY, ErrorCategory.PRON),
    REPLACEMENT_PRONOUN(ErrorType.REPLACEMENT, ErrorCategory.PRON),
    MISSING_PUNCTUATION(ErrorType.MISSING, ErrorCategory.PUNCT),
    UNNECESSARY_PUNCTUATION(ErrorType.UNNECESSARY, ErrorCategory.PUNCT),
    REPLACEMENT_PUNCTUATION(ErrorType.REPLACEMENT, ErrorCategory.PUNCT),
    MISSING_VERB(ErrorType.MISSING, ErrorCategory.VERB),
    UNNECESSARY_VERB(ErrorType.UNNECESSARY, ErrorCategory.VERB),
    REPLACEMENT_VERB(ErrorType.REPLACEMENT, ErrorCategory.VERB),

    MISSING_CONTRACTION(ErrorType.MISSING, ErrorCategory.CONTR),
    UNNECESSARY_CONTRACTION(ErrorType.UNNECESSARY, ErrorCategory.CONTR),
    REPLACEMENT_CONTRACTION(ErrorType.REPLACEMENT, ErrorCategory.CONTR),
    REPLACEMENT_MORPHOLOGY(ErrorType.REPLACEMENT, ErrorCategory.MORPH),
    REPLACEMENT_ORTHOGRAPHY(ErrorType.REPLACEMENT, ErrorCategory.ORTH),

    MISSING_OTHER(ErrorType.MISSING, ErrorCategory.OTHER),
    UNNECESSARY_OTHER(ErrorType.UNNECESSARY, ErrorCategory.OTHER),
    REPLACEMENT_OTHER(ErrorType.REPLACEMENT, ErrorCategory.OTHER),
    TRANSPOSE_OTHER(ErrorType.REPLACEMENT, ErrorCategory.OTHER),

    REPLACEMENT_SPELLING(ErrorType.REPLACEMENT, ErrorCategory.SPELL),
    REPLACEMENT_WORD_ORDER(ErrorType.REPLACEMENT, ErrorCategory.WO),

    REPLACEMENT_ADJECTIVE_FORM(ErrorType.REPLACEMENT, ErrorCategory.ADJ_FORM),
    REPLACEMENT_NOUN_INFLECTION(ErrorType.REPLACEMENT, ErrorCategory.NOUN_INFL),
    REPLACEMENT_NOUN_NUMBER(ErrorType.REPLACEMENT, ErrorCategory.NOUN_NUM),
    MISSING_NOUN_POSSESSIVE(ErrorType.MISSING, ErrorCategory.NOUN_POSS),
    UNNECESSARY_NOUN_POSSESSIVE(ErrorType.UNNECESSARY, ErrorCategory.NOUN_POSS),
    REPLACEMENT_NOUN_POSSESSIVE(ErrorType.REPLACEMENT, ErrorCategory.NOUN_POSS),
    MISSING_VERB_FORM(ErrorType.MISSING, ErrorCategory.VERB_FORM),
    UNNECESSARY_VERB_FORM(ErrorType.UNNECESSARY, ErrorCategory.VERB_FORM),
    REPLACEMENT_VERB_FORM(ErrorType.REPLACEMENT, ErrorCategory.VERB_FORM),
    REPLACEMENT_VERB_INFLECTION(ErrorType.REPLACEMENT, ErrorCategory.VERB_INFL),
    REPLACEMENT_SUBJECT_VERB_AGREEMENT(ErrorType.REPLACEMENT, ErrorCategory.VERB_SVA),
    MISSING_VERB_TENSE(ErrorType.MISSING, ErrorCategory.VERB_TENSE),
    UNNECESSARY_VERB_TENSE(ErrorType.UNNECESSARY, ErrorCategory.VERB_TENSE),
    REPLACEMENT_VERB_TENSE(ErrorType.REPLACEMENT, ErrorCategory.VERB_TENSE),

    MISSING_IGNORED(ErrorType.MISSING, ErrorCategory.IGNORED),
    UNNECESSARY_IGNORED(ErrorType.UNNECESSARY, ErrorCategory.IGNORED),
    REPLACEMENT_IGNORED(ErrorType.REPLACEMENT, ErrorCategory.IGNORED),

    NONE(ErrorType.NONE, ErrorCategory.OTHER);

    private final ErrorCategory category;
    private final ErrorType type;

    GrammaticalError(ErrorType type, ErrorCategory category) {
        this.category = category;
        this.type = type;
    }

    public static GrammaticalError unknown(Edit<?> edit) {
        return of(edit, ErrorCategory.OTHER);
    }

    public static GrammaticalError of(Edit<?> edit, ErrorCategory category) {
        ErrorType type;
        switch (edit.operation()) {
            case INSERT:
                type = ErrorType.MISSING;
                break;
            case DELETE:
                type = ErrorType.UNNECESSARY;
                break;
            case SUBSTITUTE:
            case TRANSPOSE:
                type = ErrorType.REPLACEMENT;
                break;
            case EQUAL:
            default:
                type = ErrorType.NONE;
        }

        return of(type, category);
    }

    public static GrammaticalError of(ErrorType type, ErrorCategory category) {
        for (GrammaticalError value : GrammaticalError.values()) {
            if (value.type().equals(type) && value.category().equals(category)) {
                return value;
            }
        }

        throw new RuntimeException(String.format(
            "Could not match Grammatical Error for Op[%s] and Cat[%s]",
            type,
            category
        ));
    }

    public static GrammaticalError none() {
        return GrammaticalError.NONE;
    }

    public boolean of(ErrorCategory category) {
        return category().equals(category);
    }

    public String tag() {
        return String.format("%s:%s", type.tag(), category.tag());
    }

    public ErrorCategory category() {
        return category;
    }

    public ErrorType type() {
        return type;
    }

    public boolean isNone() {
        return GrammaticalError.NONE.equals(this);
    }

    public boolean isOther() {
        return ErrorCategory.OTHER.equals(category);
    }

    public boolean isIgnored() {
        return ErrorCategory.IGNORED.equals(category);
    }

    public boolean matchesCategory(ErrorCategory category) {
        return this.category.equals(category);
    }

    public boolean matchesType(ErrorType type) {
        return this.type.equals(type);
    }
}
