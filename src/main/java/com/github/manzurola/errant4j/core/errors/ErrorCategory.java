package com.github.manzurola.errant4j.core.errors;

public enum ErrorCategory {

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
    VERB_SVA("VERB" + ":SVA"),
    VERB_TENSE("VERB:TENSE"),
    WO("WO");

    private final String tag;

    ErrorCategory(String tag) {
        this.tag = tag;
    }

    public String tag() {
        return tag;
    }
}
