package edu.guym.errantj.core.classify;

public enum Category {

    ADJ("ADJ", "Adjective"),
    ADJ_FORM("ADJ:FORM", "Adjective Form"),
    ADV("ADVERB", "Adverb"),
    CONJ("CONJUNCTION", "Conjunction"),
    CONTR("CONTRACTION", "Contraction"),
    DET("DETERMINER", "Determiner"),
    MORPH("MORPHOLOGY", "Morphology"),
    NOUN("NOUN", "Noun"),
    NOUN_INFL("NOUN:INFL", "Noun Inflection"),
    NOUN_NUM("NOUN:NUMBER", "Noun Number"),
    NOUN_POSS("NOUN:POSSESSIVE", "Noun Possessive"),
    ORTH("ORTHOGRAPHY", "Orthography"),
    OTHER("OTHER", "Other"),
    PART("PARTICLE", "Particle"),
    PREP("PREPOSITION", "Preposition"),
    PRON("PRONOUN", "Pronoun"),
    PUNCT("PUNCTUATION", "Punctutation"),
    SPELL("SPELLING", "Spelling"),
    VERB("VERB", "Verb"),
    VERB_FORM("VERB:FORM", "Verb Form"),
    VERB_INFL("VERB:INFLECTION", "Verb Inflection"),
    VERB_SVA("VERB:SVA", "Subject-Verb Agreement"),
    VERB_TENSE("VERB:TENSE", "Verb Tense"),
    WO("WO", "Word Order");

    private final String tag;
    private final String name;

    Category(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }
}
