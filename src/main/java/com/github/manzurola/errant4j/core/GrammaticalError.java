package com.github.manzurola.errant4j.core;

import com.github.manzurola.aligner.edit.Edit;

public enum GrammaticalError {

    MISSING_ADJECTIVE(Type.MISSING, Category.ADJ),
    UNNECESSARY_ADJECTIVE(Type.UNNECESSARY, Category.ADJ),
    REPLACEMENT_ADJECTIVE(Type.REPLACEMENT, Category.ADJ),
    MISSING_ADVERB(Type.MISSING, Category.ADV),
    UNNECESSARY_ADVERB(Type.UNNECESSARY, Category.ADV),
    REPLACEMENT_ADVERB(Type.REPLACEMENT, Category.ADV),
    MISSING_CONJUNCTION(Type.MISSING, Category.CONJ),
    UNNECESSARY_CONJUNCTION(Type.UNNECESSARY, Category.CONJ),
    REPLACEMENT_CONJUNCTION(Type.REPLACEMENT, Category.CONJ),
    MISSING_DETERMINER(Type.MISSING, Category.DET),
    UNNECESSARY_DETERMINER(Type.UNNECESSARY, Category.DET),
    REPLACEMENT_DETERMINER(Type.REPLACEMENT, Category.DET),
    MISSING_NOUN(Type.MISSING, Category.NOUN),
    UNNECESSARY_NOUN(Type.UNNECESSARY, Category.NOUN),
    REPLACEMENT_NOUN(Type.REPLACEMENT, Category.NOUN),
    MISSING_PARTICLE(Type.MISSING, Category.PART),
    UNNECESSARY_PARTICLE(Type.UNNECESSARY, Category.PART),
    REPLACEMENT_PARTICLE(Type.REPLACEMENT, Category.PART),
    MISSING_PREPOSITION(Type.MISSING, Category.PREP),
    UNNECESSARY_PREPOSITION(Type.UNNECESSARY, Category.PREP),
    REPLACEMENT_PREPOSITION(Type.REPLACEMENT, Category.PREP),
    MISSING_PRONOUN(Type.MISSING, Category.PRON),
    UNNECESSARY_PRONOUN(Type.UNNECESSARY, Category.PRON),
    REPLACEMENT_PRONOUN(Type.REPLACEMENT, Category.PRON),
    MISSING_PUNCTUATION(Type.MISSING, Category.PUNCT),
    UNNECESSARY_PUNCTUATION(Type.UNNECESSARY, Category.PUNCT),
    REPLACEMENT_PUNCTUATION(Type.REPLACEMENT, Category.PUNCT),
    MISSING_VERB(Type.MISSING, Category.VERB),
    UNNECESSARY_VERB(Type.UNNECESSARY, Category.VERB),
    REPLACEMENT_VERB(Type.REPLACEMENT, Category.VERB),

    MISSING_CONTRACTION(Type.MISSING, Category.CONTR),
    UNNECESSARY_CONTRACTION(Type.UNNECESSARY, Category.CONTR),
    REPLACEMENT_CONTRACTION(Type.REPLACEMENT, Category.CONTR),
    REPLACEMENT_MORPHOLOGY(Type.REPLACEMENT, Category.MORPH),
    REPLACEMENT_ORTHOGRAPHY(Type.REPLACEMENT, Category.ORTH),

    MISSING_OTHER(Type.MISSING, Category.OTHER),
    UNNECESSARY_OTHER(Type.UNNECESSARY, Category.OTHER),
    REPLACEMENT_OTHER(Type.REPLACEMENT, Category.OTHER),
    TRANSPOSE_OTHER(Type.REPLACEMENT, Category.OTHER),

    REPLACEMENT_SPELLING(Type.REPLACEMENT, Category.SPELL),
    REPLACEMENT_WORD_ORDER(Type.REPLACEMENT, Category.WO),

    REPLACEMENT_ADJECTIVE_FORM(Type.REPLACEMENT, Category.ADJ_FORM),
    REPLACEMENT_NOUN_INFLECTION(Type.REPLACEMENT, Category.NOUN_INFL),
    REPLACEMENT_NOUN_NUMBER(Type.REPLACEMENT, Category.NOUN_NUM),
    MISSING_NOUN_POSSESSIVE(Type.MISSING, Category.NOUN_POSS),
    UNNECESSARY_NOUN_POSSESSIVE(Type.UNNECESSARY, Category.NOUN_POSS),
    REPLACEMENT_NOUN_POSSESSIVE(Type.REPLACEMENT, Category.NOUN_POSS),
    MISSING_VERB_FORM(Type.MISSING, Category.VERB_FORM),
    UNNECESSARY_VERB_FORM(Type.UNNECESSARY, Category.VERB_FORM),
    REPLACEMENT_VERB_FORM(Type.REPLACEMENT, Category.VERB_FORM),
    REPLACEMENT_VERB_INFLECTION(Type.REPLACEMENT, Category.VERB_INFL),
    REPLACEMENT_SUBJECT_VERB_AGREEMENT(Type.REPLACEMENT, Category.VERB_SVA),
    MISSING_VERB_TENSE(Type.MISSING, Category.VERB_TENSE),
    UNNECESSARY_VERB_TENSE(Type.UNNECESSARY, Category.VERB_TENSE),
    REPLACEMENT_VERB_TENSE(Type.REPLACEMENT, Category.VERB_TENSE),

    MISSING_IGNORED(Type.MISSING, Category.IGNORED),
    UNNECESSARY_IGNORED(Type.UNNECESSARY, Category.IGNORED),
    REPLACEMENT_IGNORED(Type.REPLACEMENT, Category.IGNORED),

    NONE(Type.NONE, Category.OTHER);

    private final Category category;
    private final Type type;

    GrammaticalError(Type type, Category category) {
        this.category = category;
        this.type = type;
    }

    public static GrammaticalError unknown(Edit<?> edit) {
        return of(edit, Category.OTHER);
    }

    public static GrammaticalError of(Edit<?> edit, Category category) {
        GrammaticalError.Type type;
        switch (edit.operation()) {
            case INSERT:
                type = GrammaticalError.Type.MISSING;
                break;
            case DELETE:
                type = GrammaticalError.Type.UNNECESSARY;
                break;
            case SUBSTITUTE:
            case TRANSPOSE:
                type = GrammaticalError.Type.REPLACEMENT;
                break;
            case EQUAL:
            default:
                type = GrammaticalError.Type.NONE;
        }

        return of(type, category);
    }

    public static GrammaticalError of(Type type, Category category) {
        for (GrammaticalError value : GrammaticalError.values()) {
            if (value.type().equals(type) && value.category().equals(category)) {
                return value;
            }
        }

        throw new RuntimeException(String.format(
                "Could not match Grammatical Error for Op[%s] and Cat[%s]", type, category
        ));
    }

    public static GrammaticalError none() {
        return GrammaticalError.NONE;
    }

    public boolean of(Category category) {
        return category().equals(category);
    }

    public String tag() {
        return String.format("%s:%s", type.tag(), category.tag());
    }

    public Category category() {
        return category;
    }

    public Type type() {
        return type;
    }

    public boolean isNone() {
        return GrammaticalError.NONE.equals(this);
    }

    public boolean isOther() {
        return Category.OTHER.equals(category);
    }

    public boolean isIgnored() {
        return Category.IGNORED.equals(category);
    }

    public boolean isNoneOrIgnored() {
        return isNone() || isIgnored();
    }

    public enum Type {
        MISSING("M"),
        UNNECESSARY("U"),
        REPLACEMENT("R"),
        NONE("E");

        private final String tag;

        Type(String tag) {
            this.tag = tag;
        }

        public String tag() {
            return tag;
        }
    }

    public enum Category {

        ADJ("ADJ"),
        ADJ_FORM("ADJ:FORM"),
        ADV("ADVERB"),
        CONJ("CONJUNCTION"),
        CONTR("CONTRACTION"),
        DET("DETERMINER"),
        IGNORED("IGNORED"),
        MORPH("MORPHOLOGY"),
        NOUN("NOUN"),
        NOUN_INFL("NOUN:INFL"),
        NOUN_NUM("NOUN:NUMBER"),
        NOUN_POSS("NOUN:POSSESSIVE"),
        ORTH("ORTHOGRAPHY"),
        OTHER("OTHER"),
        PART("PARTICLE"),
        PREP("PREPOSITION"),
        PRON("PRONOUN"),
        PUNCT("PUNCTUATION"),
        SPELL("SPELLING"),
        VERB("VERB"),
        VERB_FORM("VERB:FORM"),
        VERB_INFL("VERB:INFLECTION"),
        VERB_SVA("VERB:SVA"),
        VERB_TENSE("VERB:TENSE"),
        WO("WO");

        private final String tag;

        Category(String tag) {
            this.tag = tag;
        }

        public String tag() {
            return tag;
        }
    }
}
