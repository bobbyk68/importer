package uk.gov.hmrc.rules.analysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DslrRuleExtractor {

    public List<ParsedDslrRule> extractRules(Path dslrFile) {
        List<String> lines = readLines(dslrFile);
        List<ParsedDslrRule> rules = new ArrayList<>();

        String currentRuleName = null;
        List<String> whenLines = new ArrayList<>();
        List<String> thenLines = new ArrayList<>();

        boolean insideRule = false;
        boolean insideWhen = false;
        boolean insideThen = false;

        for (String line : lines) {
            String trimmed = line.trim();

            if (trimmed.startsWith("rule ")) {
                currentRuleName = extractRuleName(trimmed);
                whenLines = new ArrayList<>();
                thenLines = new ArrayList<>();

                insideRule = true;
                insideWhen = false;
                insideThen = false;

                continue;
            }

            if (!insideRule) {
                continue;
            }

            if ("when".equals(trimmed)) {
                insideWhen = true;
                insideThen = false;
                continue;
            }

            if ("then".equals(trimmed)) {
                insideWhen = false;
                insideThen = true;
                continue;
            }

            if ("end".equals(trimmed)) {
                rules.add(new ParsedDslrRule(
                        dslrFile.getFileName().toString(),
                        currentRuleName,
                        List.copyOf(whenLines),
                        List.copyOf(thenLines)
                ));

                insideRule = false;
                insideWhen = false;
                insideThen = false;
                currentRuleName = null;

                continue;
            }

            if (insideWhen && !trimmed.isBlank()) {
                whenLines.add(trimmed);
            }

            if (insideThen && !trimmed.isBlank()) {
                thenLines.add(trimmed);
            }
        }

        return rules;
    }

    private List<String> readLines(Path dslrFile) {
        try {
            return Files.readAllLines(dslrFile);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read DSLR file: " + dslrFile, exception);
        }
    }

    private String extractRuleName(String ruleLine) {
        int firstQuote = ruleLine.indexOf('"');
        int lastQuote = ruleLine.lastIndexOf('"');

        if (firstQuote >= 0 && lastQuote > firstQuote) {
            return ruleLine.substring(firstQuote + 1, lastQuote);
        }

        return ruleLine.replace("rule", "").trim();
    }
}