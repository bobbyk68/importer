package uk.gov.hmrc.rules.analysis.bdd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FeatureRuleReferenceScanner {

    private static final Pattern RULE_PATTERN =
            Pattern.compile("\\bBR\\d{3}-[A-Z0-9_-]+\\b");

    public Set<BddRuleReference> scan(Path featureDirectory) {

        Set<BddRuleReference> references = new HashSet<>();

        try (Stream<Path> paths = Files.walk(featureDirectory)) {

            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".feature"))
                    .forEach(path -> scanFeatureFile(path, references));

            return references;

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to scan feature directory: " + featureDirectory,
                    exception
            );
        }
    }

    private void scanFeatureFile(
            Path featureFile,
            Set<BddRuleReference> references
    ) {
        try {
            String content = Files.readString(featureFile);

            Matcher matcher = RULE_PATTERN.matcher(content);

            while (matcher.find()) {

                references.add(
                        new BddRuleReference(
                                matcher.group(),
                                featureFile
                        )
                );
            }

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to scan feature file: " + featureFile,
                    exception
            );
        }
    }
}