# Errant4J 🧑🏻‍🏫📝

Errant4J is an unofficial [ERRANT](https://github.com/chrisjbryant/errant) port that allows nlp practitioners to annotate grammatical mistakes in parallel text in Java using [SpaCy4J](https://github.com/LanguageToys/spacy4j).

![maven](https://github.com/manzurola/errant-java/actions/workflows/maven.yml/badge.svg)

About ERRANT (from the offical docs):

> The main aim of ERRANT is to automatically annotate parallel English sentences with error type information. Specifically, given an original and corrected sentence pair, ERRANT will extract the edits that transform the former to the latter and classify them according to a rule-based error type framework.

## Prerequisits

Before you begin, ensure you have met the following requirements:

* You have Java 11 installed.
* You have the required dependencies and prerequisites for using [SpaCy4J](https://github.com/LanguageToys/spacy4j).

## Installing Errant4J

Available as Maven dependencies via [GitHub Packages](https://github.com/LanguageToys/errant4j/packages).

See GitHub documentation on [installing a package](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#installing-a-package).

## Using Errant4J

To use Errant4J in code, follow these steps:

```java
// Get a spaCy instance (from spacy4j)
SpaCy spacy = SpaCy.create(CoreNLPAdapter.create());

// Create an English Errant
Errant errant = Errant.en(spacy);

// Parse source and target sentences
Doc source = errant.parse("Yesterday I went to see my therapist.");
Doc target = errant.parse("Yesterday I go to see my therapist.");

// Annotate grammatical errors
List<Annotation> annotations = errant.annotate(source.tokens(), target.tokens());

// Inspect annotations
for (Annotation annotation : annotations) {
    GrammaticalError error = annotation.grammaticalError();
    String sourceText = annotation.sourceText();
    String targetText = annotation.targetText();
    System.out.printf("Error: %s, sourceText: %s, targetText: %s%n",
                      error,
                      sourceText,
                      targetText);
    
    // Inspect the underlying edit
    Edit<Token> edit = annotation.edit();
    // ...
}
```

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
