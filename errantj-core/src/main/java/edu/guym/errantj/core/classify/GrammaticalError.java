package edu.guym.errantj.core.classify;

public enum GrammaticalError {

    MISSING_ADJECTIVE(Category.ADJ, Type.MISSING, "Missing Adjective"),
    UNNECESSARY_ADJECTIVE(Category.ADJ, Type.UNNECESSARY, "Unnecessary Adjective"),
    REPLACEMENT_ADJECTIVE(Category.ADJ, Type.REPLACEMENT, "Replacement Adjective"),
    MISSING_ADVERB(Category.ADV, Type.MISSING, "Missing Adverb"),
    UNNECESSARY_ADVERB(Category.ADV, Type.UNNECESSARY, "Unnecessary Adverb"),
    REPLACEMENT_ADVERB(Category.ADV, Type.REPLACEMENT, "Replacement Adverb"),
    MISSING_CONJUNCTION(Category.CONJ, Type.MISSING, "Missing Conjunction"),
    UNNECESSARY_CONJUNCTION(Category.CONJ, Type.UNNECESSARY, "Unnecessary Conjunction"),
    REPLACEMENT_CONJUNCTION(Category.CONJ, Type.REPLACEMENT, "Replacement Conjunction"),
    MISSING_DETERMINER(Category.DET, Type.MISSING, "Missing Determiner"),
    UNNECESSARY_DETERMINER(Category.DET, Type.UNNECESSARY, "Unnecessary Determiner"),
    REPLACEMENT_DETERMINER(Category.DET, Type.REPLACEMENT, "Replacement Determiner"),
    MISSING_NOUN(Category.NOUN, Type.MISSING, " Missing Noun"),
    UNNECESSARY_NOUN(Category.NOUN, Type.UNNECESSARY, "Unnecessary Noun"),
    REPLACEMENT_NOUN(Category.NOUN, Type.REPLACEMENT, "Replacement Noun"),
    MISSING_PARTICLE(Category.PART, Type.MISSING, "Missing Part"),
    UNNECESSARY_PARTICLE(Category.PART, Type.UNNECESSARY, "Unnecessary Particle"),
    REPLACEMENT_PARTICLE(Category.PART, Type.REPLACEMENT, "Replacement Particle"),
    MISSING_PREPOSITION(Category.PREP, Type.MISSING, "Missing Preposition"),
    UNNECESSARY_PREPOSITION(Category.PREP, Type.UNNECESSARY, "Unnecessary Preposition"),
    REPLACEMENT_PREPOSITION(Category.PREP, Type.REPLACEMENT, "Replacement Preposition"),
    MISSING_PRONOUN(Category.PRON, Type.MISSING, "Missing Pronoun"),
    UNNECESSARY_PRONOUN(Category.PRON, Type.UNNECESSARY, "Unnecessary Pronoun"),
    REPLACEMENT_PRONOUN(Category.PRON, Type.REPLACEMENT, "Replacement Pronoun"),
    MISSING_PUNCTUATION(Category.PUNCT, Type.MISSING, "Missing Punctuation"),
    UNNECESSARY_PUNCTUATION(Category.PUNCT, Type.UNNECESSARY, "Unnecessary Punctuation"),
    REPLACEMENT_PUNCTUATION(Category.PUNCT, Type.REPLACEMENT, "Replacement Punctuation"),
    MISSING_VERB(Category.VERB, Type.MISSING, "Missing Verb"),
    UNNECESSARY_VERB(Category.VERB, Type.UNNECESSARY, "Unnecessary Verb"),
    REPLACEMENT_VERB(Category.VERB, Type.REPLACEMENT, "Replacement Verb"),

    MISSING_CONTRACTION(Category.CONTR, Type.MISSING, "Missing Contraction"),
    UNNECESSARY_CONTRACTION(Category.CONTR, Type.UNNECESSARY, "Unnecessary Contraction"),
    REPLACEMENT_CONTRACTION(Category.CONTR, Type.REPLACEMENT, "Replacement Contraction"),
    REPLACEMENT_MORPHOLOGY(Category.MORPH, Type.REPLACEMENT, "Replacement Morphology"),
    REPLACEMENT_ORTHOGRAPHY(Category.ORTH, Type.REPLACEMENT, "Replacement Orthography"),

    MISSING_OTHER(Category.OTHER, Type.MISSING, "Missing Other"),
    UNNECESSARY_OTHER(Category.OTHER, Type.UNNECESSARY, "Unnecessary Other"),
    REPLACEMENT_OTHER(Category.OTHER, Type.REPLACEMENT, "Replacement Other"),
    TRANSPOSE_OTHER(Category.OTHER, Type.REPLACEMENT, "Transpose Other"),

    REPLACEMENT_SPELLING(Category.SPELL, Type.REPLACEMENT, "Replacement Spelling"),
    REPLACEMENT_WORD_ORDER(Category.WO, Type.REPLACEMENT, "Replacement Word Order"),

    REPLACEMENT_ADJECTIVE_FORM(Category.ADJ_FORM, Type.REPLACEMENT, "Replacement Adjective Form"),
    REPLACEMENT_NOUN_INFLECTION(Category.NOUN_INFL, Type.REPLACEMENT, "Replacement Noun Inflection"),
    REPLACEMENT_NOUN_NUMBER(Category.NOUN_NUM, Type.REPLACEMENT, "Replacement Noun Number"),
    MISSING_NOUN_POSSESSIVE(Category.NOUN_POSS, Type.MISSING, "Missing Noun Possessive"),
    UNNECESSARY_NOUN_POSSESSIVE(Category.NOUN_POSS, Type.UNNECESSARY, "Unnecessary Noun Possessive"),
    REPLACEMENT_NOUN_POSSESSIVE(Category.NOUN_POSS, Type.REPLACEMENT, "Replacement Noun Possessive"),
    MISSING_VERB_FORM(Category.VERB_FORM, Type.MISSING, "Missing Verb Form"),
    UNNECESSARY_VERB_FORM(Category.VERB_FORM, Type.UNNECESSARY, "Unnecessary Verb Form"),
    REPLACEMENT_VERB_FORM(Category.VERB_FORM, Type.REPLACEMENT, "Replacement Verb Form"),
    REPLACEMENT_VERB_INFLECTION(Category.VERB_INFL, Type.REPLACEMENT, "Replacement Verb Inflection"),
    REPLACEMENT_SUBJECT_VERB_AGREEMENT(Category.VERB_SVA, Type.REPLACEMENT, "Replacement Subject Verb Agreement"),
    MISSING_VERB_TENSE(Category.VERB_TENSE, Type.MISSING, "Missing Verb Tense"),
    UNNECESSARY_VERB_TENSE(Category.VERB_TENSE, Type.UNNECESSARY, "Unnecessary Verb Tense"),
    REPLACEMENT_VERB_TENSE(Category.VERB_TENSE, Type.REPLACEMENT, "Replacement Verb Tense"),

    NONE(Category.OTHER, Type.NONE, "None");

    private final Category category;
    private final Type type;
    private final String tag;

    public enum Type {
        MISSING,
        UNNECESSARY,
        REPLACEMENT,
        NONE
    }

    GrammaticalError(Category category, Type type, String tag) {
        this.category = category;
        this.type = type;
        this.tag = tag;
    }

    public boolean ofCategory(Category category) {
        return category().equals(category);
    }

    public Category category() {
        return category;
    }

    public Type type() {
        return type;
    }

    public String tag() {
        return tag;
    }

    public boolean isNone() {
        return this.equals(GrammaticalError.NONE);
    }

}
