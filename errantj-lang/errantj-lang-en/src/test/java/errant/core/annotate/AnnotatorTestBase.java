package errant.core.annotate;

import edu.guym.errantj.core.annotate.Annotation;
import edu.guym.errantj.core.annotate.Annotator;
import edu.guym.spacyj.api.containers.Doc;
import edu.guym.spacyj.api.containers.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AnnotatorTestBase {

    private static final Logger logger = LoggerFactory.getLogger(AnnotatorTestBase.class);
    private final Annotator annotator;

    public AnnotatorTestBase(Annotator annotator) {
        this.annotator = annotator;
    }

    protected final Doc nlp(String text) {
        return annotator.nlp(text);
    }

    protected void assertSingleExpectedError(Annotation<String> expected, Doc source, Doc target) {
        logger.info("Asserting single expected grammatical classify: " + expected);
        List<Annotation<String>> actual = annotate(source, target);
        if (actual.isEmpty()) {
            throw new AssertionError("Expected one classification but non were found");
        }
        if (actual.size() > 1) {
            throw new AssertionError("Expected one classification but found multiple: " + actual);
        }
        assertEquals(Collections.singletonList(expected), actual);
    }

    protected List<Annotation<String>> annotate(Doc source, Doc target) {
        logger.info("Running classifier...");
        logger.info("Source sentence: " + source);
        logger.info("Target sentence: " + target);
        return annotator
                .annotate(source.tokens(), target.tokens())
                .stream()
                .map(annotation -> annotation.map(edit -> edit.map(Token::text)))
                .filter(Annotation::hasError)
                .collect(Collectors.toList());
    }

    protected void printResult(Doc source, Doc target) {
        List<Annotation<String>> annotations = annotate(source, target);
        logger.info("Results: " + annotations);
    }

    protected void assertMultipleExpectedErrors(List<Annotation<String>> expected, Doc source, Doc target) {
        List<Annotation<String>> actual = annotate(source, target);
        if (actual.isEmpty()) {
            throw new AssertionError("Could not matchError expected " + expected + ".\nSource: " + source + "\nTarget: " + target);
        }
        try {
            assertEquals(expected, actual);
        } catch (AssertionError e) {
            logger.info(source.toString());
            logger.info(target.toString());
            throw e;
        }
    }

    protected void assertContainsError(Annotation<String> expected, Doc source, Doc target) {
        List<Annotation<String>> actual = annotate(source, target);
        logger.info(actual.toString());
        if (actual.isEmpty()) {
            throw new AssertionError("Could not matchError expected " + expected + ".\nSource: " + source + "\nTarget: " + target);
        }
        try {
            assertTrue(actual.contains(expected));
        } catch (AssertionError e) {
            logger.info(source.toString());
            logger.info(target.toString());
            throw e;
        }
    }

    protected void assertGrammaticalErrorDoesNotExist(Annotation<String> notExpected, Doc source, Doc target) {
        logger.info("Asserting grammatical classify does no exist: " + notExpected);
        List<Annotation<String>> actual = annotate(source, target);
        if (!actual.isEmpty()) {
            assertNotEquals(Arrays.asList(notExpected), actual);
        }
    }

}
