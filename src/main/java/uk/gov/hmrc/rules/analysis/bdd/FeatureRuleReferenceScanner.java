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
            Pattern.compile("\\bBR\\d{3}_\\d{3,4}(?:_[A-Za-z0-9]+)?\\b");

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

        boolean matchedFromFileName =
                scanText(featureFile.toString(), featureFile, references);

        if (matchedFromFileName) {
            return;
        }

        try {
            String content = Files.readString(featureFile);

            scanText(content, featureFile, references);

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to scan feature file: " + featureFile,
                    exception
            );
        }
    }

    private boolean scanText(
            String text,
            Path featureFile,
            Set<BddRuleReference> references
    ) {

        boolean found = false;

        Matcher matcher = RULE_PATTERN.matcher(text);

        while (matcher.find()) {

            found = true;

            references.add(
                    new BddRuleReference(
                            matcher.group(),
                            featureFile
                    )
            );
        }

        return found;
    }
}