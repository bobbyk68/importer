package uk.gov.hmrc.rules.analysis.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GeneratedPayloadWriter {

    public void write(
            Path outputDirectory,
            String ruleName,
            String xml
    ) {
        try {
            Files.createDirectories(outputDirectory);

            String safeFileName = ruleName
                    .replaceAll("[^a-zA-Z0-9._-]", "_")
                    + ".xml";

            Path outputFile = outputDirectory.resolve(safeFileName);

            Files.writeString(outputFile, xml);

            System.out.println("Generated payload:");
            System.out.println("  " + outputFile);

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to write generated XML payload for rule: " + ruleName,
                    exception
            );
        }
    }
}