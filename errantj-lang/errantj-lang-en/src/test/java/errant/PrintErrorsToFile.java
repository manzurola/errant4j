package errant;

import edu.guym.errantj.core.errors.GrammaticalError;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PrintErrorsToFile {

    @Test
    void name() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Path.of("grammatical_errors.txt"));


        for (GrammaticalError error : GrammaticalError.values()) {
            writer.write(error.name());
            writer.newLine();
        }

        writer.close();
    }
}
