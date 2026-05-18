package uk.gov.hmrc.rules.analysis.bdd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BddRuleReferenceScanner {

    private static final Pattern RULE_PATTERN =
            Pattern.compile("\\bBR\\d{3}-[A-Z0-9_-]+\\b");

    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of(
            ".feature", ".json", ".xml", ".java", ".csv", ".txt", ".yml", ".yaml"
    );

    public Set<BddRuleReference> scan(Path bddDirectory) {
        Set<BddRuleReference> references = new HashSet<>();

        try (Stream<Path> paths = Files.walk(bddDirectory)) {
            List<Path> files = paths
                    .filter(Files::isRegularFile)
                    .filter(this::isSupportedFile)
                    .toList();

            for (Path file : files) {
                String content = Files.readString(file);

                Matcher matcher = RULE_PATTERN.matcher(content);

                while (matcher.find()) {
                    references.add(new BddRuleReference(
                            matcher.group(),
                            file
                    ));
                }
            }

            return references;

        } catch (IOException exception) {
            throw new IllegalStateException("Failed to scan BDD directory: " + bddDirectory, exception);
        }
    }

    private boolean isSupportedFile(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();

        return SUPPORTED_EXTENSIONS.stream()
                .anyMatch(fileName::endsWith);
    }
}