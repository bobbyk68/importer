package uk.gov.hmrc.rules.analysis.bdd;
import uk.gov.hmrc.rules.analysis.dslr.ParsedDslrRule;
import uk.gov.hmrc.rules.analysis.report.RuleCoverageResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FeatureRuleReferenceScanner {

    private static final Pattern RULE_PATTERN =
            Pattern.compile(
                    "(?<![A-Za-z0-9_])BR\\d{3}_\\d{3}(?:_[A-Za-z0-9_]+)?(?![A-Za-z0-9_])"
            );

    private final List<InvalidFeatureReference> invalidReferences =
            new ArrayList<>();

    public RuleCoverageResult findCoverage(
            ParsedDslrRule rule,
            Path featureDirectory
    ) {

        String fullRuleName = rule.ruleName();

        String baseRuleKey = baseRuleKey(fullRuleName);

        List<String> matchingFeatureFiles = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(featureDirectory)) {

            List<Path> featureFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".feature"))
                    .toList();

            /*
             * STEP 1
             * Search feature filenames first using base rule key
             */
            for (Path featureFile : featureFiles) {

                String featureFileName =
                        featureFile.getFileName().toString();

                if (featureFileName.contains(baseRuleKey)) {

                    matchingFeatureFiles.add(featureFile.toString());

                    validateFeatureContents(
                            featureFile,
                            baseRuleKey
                    );

                    return new RuleCoverageResult(
                            fullRuleName,
                            rule.sourceFile(),
                            true,
                            matchingFeatureFiles
                    );
                }
            }

            /*
             * STEP 2
             * Fallback:
             * search feature contents using FULL rule name
             */
            for (Path featureFile : featureFiles) {

                String content = Files.readString(featureFile);

                if (content.contains(fullRuleName)) {

                    matchingFeatureFiles.add(featureFile.toString());

                    validateFeatureContents(
                            featureFile,
                            baseRuleKey
                    );

                    return new RuleCoverageResult(
                            fullRuleName,
                            rule.sourceFile(),
                            true,
                            matchingFeatureFiles
                    );
                }
            }

            /*
             * STEP 3
             * No coverage found
             */
            return new RuleCoverageResult(
                    fullRuleName,
                    rule.sourceFile(),
                    false,
                    List.of()
            );

        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Failed during feature coverage scan",
                    exception
            );
        }
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
}