package uk.gov.hmrc.rules.analysis.bdd;
import uk.gov.hmrc.rules.analysis.dslr.ParsedDslrRule;
import uk.gov.hmrc.rules.analysis.report.RuleCoverageResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FeatureRuleReferenceScanner {

    private static final Pattern RULE_PATTERN =
            Pattern.compile(
                    "(?<![A-Za-z0-9_])BR\\d{3}_\\d{3}(?:_[A-Za-z0-9_]+)?(?![A-Za-z0-9_])"
            );
    private List<Path> cachedFeatureFiles;

    private final Map<Path, String> contentCache = new HashMap<>();


    private final List<InvalidFeatureReference> invalidReferences =
            new ArrayList<>();

    public RuleCoverageResult findCoverage(
            ParsedDslrRule rule,
            Path featureDirectory
    ) {
        String fullRuleName = rule.ruleName();
        String baseRuleKey = baseRuleKey(fullRuleName);

        List<String> matchingFeatureFiles = new ArrayList<>();

        List<Path> featureFiles = featureFiles(featureDirectory);

        for (Path featureFile : featureFiles) {
            String featureFilePath = featureFile.toString();

            if (featureFilePath.contains(baseRuleKey)
                    || featureFilePath.contains(fullRuleName)) {

                matchingFeatureFiles.add(featureFile.toString());

                validateFeatureContents(featureFile, baseRuleKey);

                return new RuleCoverageResult(
                        fullRuleName,
                        rule.sourceFile(),
                        true,
                        matchingFeatureFiles
                );
            }
        }

        for (Path featureFile : featureFiles) {
            String content = readFeatureContent(featureFile);

            if (content.contains(fullRuleName)) {
                matchingFeatureFiles.add(featureFile.toString());

                validateFeatureContents(featureFile, baseRuleKey);

                return new RuleCoverageResult(
                        fullRuleName,
                        rule.sourceFile(),
                        true,
                        matchingFeatureFiles
                );
            }
        }

        return new RuleCoverageResult(
                fullRuleName,
                rule.sourceFile(),
                false,
                List.of()
        );
    }

    private void validateFeatureContents(
            Path featureFile,
            String expectedBaseRule
    ) {

        try {

            String content = Files.readString(featureFile);

            Matcher matcher = RULE_PATTERN.matcher(content);

            while (matcher.find()) {

                String referencedRule =
                        matcher.group();

                String referencedBaseRule =
                        baseRuleKey(referencedRule);

                if (!referencedBaseRule.equals(expectedBaseRule)) {

                    invalidReferences.add(
                            new InvalidFeatureReference(
                                    featureFile.toString(),
                                    expectedBaseRule,
                                    referencedRule,
                                    referencedBaseRule
                            )
                    );
                }
            }

        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Failed validating feature contents: " + featureFile,
                    exception
            );
        }
    }

    private String baseRuleKey(String ruleName) {

        String[] parts = ruleName.split("_");

        if (parts.length >= 2) {
            return parts[0] + "_" + parts[1];
        }

        return ruleName;
    }

    public List<InvalidFeatureReference> invalidReferences() {
        return invalidReferences;
    }

    private String readFeatureContent(Path featureFile) {
        return contentCache.computeIfAbsent(featureFile, path -> {
            try {
                return Files.readString(path);
            } catch (IOException exception) {
                throw new IllegalStateException(
                        "Failed to read feature file: " + path,
                        exception
                );
            }
        });
    }

    private List<Path> featureFiles(Path featureDirectory) {
        if (cachedFeatureFiles != null) {
            return cachedFeatureFiles;
        }

        try (Stream<Path> paths = Files.walk(featureDirectory)) {
            cachedFeatureFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".feature"))
                    .toList();

            System.out.println("Feature files found:");
            System.out.println("  " + cachedFeatureFiles.size());
            System.out.println();

            return cachedFeatureFiles;

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to scan feature directory: " + featureDirectory,
                    exception
            );
        }
    }
}