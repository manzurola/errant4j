# Errant4J 🧑🏻‍🏫📝

An unofficial Java port of [ERRANT](https://github.com/chrisjbryant/errant)

![maven](https://github.com/manzurola/errant-java/actions/workflows/maven.yml/badge.svg)

From the offical docs:

> The main aim of ERRANT is to automatically annotate parallel English sentences with error type information. Specifically, given an original and corrected sentence pair, ERRANT will extract the edits that transform the former to the latter and classify them according to a rule-based error type framework.

## Quick Start:

```java
// Get a spaCy instance (from spacy-java)
SpaCy spacy = SpaCy.create(CoreNLPAdapter.create());

// Create an english annotator
Annotator annotator = Errant.en(spacy);

// Parse source and target sentences
Doc source = annotator.parse("Yesterday I went to see my therapist.");
Doc target = annotator.parse("Yesterday I go to see my therapist.");

// Annotate grammatical errors
List<Annotation> annotations = annotator.annotate(source.tokens(), target.tokens());

// Inspect annotations
for (Annotation annotation : annotations) {
    System.out.printf("Error: %s, sourceText: %s, targetText: %s%n",
            annotation.grammaticalError(), annotation.sourceText(), annotation.targetText());
}
```
