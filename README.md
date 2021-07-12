# Errant4J 🧑🏻‍🏫📝

Errant4J is an unofficial [ERRANT](https://github.com/chrisjbryant/errant) port that allows nlp practitioners to annotate grammatical mistakes in parallel text in Java using [spaCy4J](https://github.com/LanguageToys/spacy4j).

From the official docs:
> The main aim of ERRANT is to automatically annotate parallel English sentences with error type information. Specifically, given an original and corrected sentence pair, ERRANT will extract the edits that transform the former to the latter and classify them according to a rule-based error type framework.

![maven](https://github.com/manzurola/errant-java/actions/workflows/maven.yml/badge.svg)

## Prerequisits

Before you begin, ensure you have met the following requirements:

* You have Java 11 installed.
* You have the required dependencies and prerequisites for using [spaCy4J](https://github.com/LanguageToys/spacy4j).

## Installing Errant4J

Available as Maven dependencies via [GitHub Packages](https://github.com/LanguageToys/errant4j/packages).

See GitHub documentation on [installing a package](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#installing-a-package).

## Using Errant4J

To use Errant4J in code, follow these steps:

```java
// Get a spaCy instance (from spaCy4j)
SpaCy spacy = SpaCy.create(CoreNLPAdapter.create());

// Create an english annotator
Annotator annotator = Errant.create().annotator("en", spacy);

// Parse source and target sentences
Doc source = annotator.parse("Yesterday I go to see my therapist.");
Doc target = annotator.parse("Yesterday I went to see my therapist.");

// Annotate grammatical errors
List<Annotation> annotations = annotator.annotate(source.tokens(), target.tokens());

// Inspect annotations
for (Annotation annotation : annotations) {
    GrammaticalError error = annotation.grammaticalError();
    String sourceText = annotation.sourceText();
    String targetText = annotation.targetText();
    System.out.printf("Error: %s, sourceText: %s, targetText: %s%n",
                      error,
                      sourceText,
                      targetText);

    // Inspect the classified edit
    Edit<Token> edit = annotation.edit();
    // ...
}
```

Errant4J is currently available only for English.

## Developing Errant4J

If you wish to develop Errant4J for another language, start with the reference [English implementation](https://github.com/languagetoys/errant4j/tree/main/src/main/java/io/languagetoys/errant4j/lang/en).
I suggest you copy that to a new package, i.e. `lang.he` for hebrew, as well as the relevant [test package](https://github.com/languagetoys/errant4j/tree/main/src/test/java/io/languagetoys/errant4j/lang/en).

As per the current design, you will be required to implement a custom [Merger](https://github.com/languagetoys/errant4j/blob/21139f09d0f53a3f91a995b07df3ef9870e4646d/src/main/java/io/languagetoys/errant4j/core/merge/Merger.java) and [Classifier](https://github.com/languagetoys/errant4j/blob/21139f09d0f53a3f91a995b07df3ef9870e4646d/src/main/java/io/languagetoys/errant4j/core/classify/Classifier.java). 
These two will then be provided via the base [Pipeline](https://github.com/languagetoys/errant4j/blob/main/src/main/java/io/languagetoys/errant4j/core/Pipeline.java) which provides a preconfigured [TokenAligner](https://github.com/languagetoys/errant4j/blob/main/src/main/java/io/languagetoys/errant4j/core/align/TokenAligner.java) as the first step in the pipeline.

I recommend starting with tests and then slowly develop the merger and classifier until they pass, like so:
```java

// Create a custom in-development Annotator.
// The first pipeline component, the Token Aligner, comes preconfigured in the created Annotator.
Annotator annotator = Annotator.of(new EnMerger(), new EnClassifier());

// Prepare source and target docs
Doc source = annotator.parse("I am eat dinner.");
Doc target = annotator.parse("I am eating dinner.");

// Create an expected string edit and transform it to a Token edit 
Edit<Token> edit = Edit.builder()
        .substitute("eat")
        .with("eating")
        .atPosition(2, 2)
        .project(source.tokens(), target.tokens());

// Create the expected annotation containing the Edit and GrammaticalError
Annotation expected = Annotation.of(edit, GrammaticalError.REPLACEMENT_VERB_FORM);

// Run Errant for the given source and target
List<Annotation> actual = annotator.annotate(source.tokens(), target.tokens());

// Assert that the actual errors contain our expected error
Assertions.assertTrue(actual.contains(expected));

```

Alternatively, contact me directly and I'll help you get started fast.


## Contributions

To contribute to Errant4J, follow these steps:

1. Fork this repository.
2. Create a branch: `git checkout -b <branch_name>`.
3. Make your changes and commit them: `git commit -m '<commit_message>'`
4. Push to the original branch: `git push origin <project_name>/<location>`
5. Create the pull request.

Alternatively see the GitHub documentation on [creating a pull request](https://docs.github.com/en/github/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request).

        
## Contributors
        
Thanks to the following people who have contributed to this project:
        
* [@manzurola](https://github.com/manzurola) 🐈        

## Contact

If you want to contact me you can reach me at [guy.manzurola@gmail.com](guy.manzurola@gmail.com).

## License
        
This project uses the following license: [MIT](https://github.com/LanguageToys/aligner/blob/555fd35e842feb8d899d7197a1965ea01bc74c95/LICENSE).
