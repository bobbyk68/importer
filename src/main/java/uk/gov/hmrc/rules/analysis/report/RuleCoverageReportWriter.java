package uk.gov.hmrc.rules.analysis.report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RuleCoverageReportWriter {

    public void write(
            Path outputDirectory,
            List<RuleCoverageResult> results
    ) {
        try {
            Files.createDirectories(outputDirectory);

            List<RuleCoverageResult> testedRules = results.stream()
                    .filter(RuleCoverageResult::tested)
                    .toList();

            List<RuleCoverageResult> missingRules = results.stream()
                    .filter(result -> !result.tested())
                    .toList();

            writeReport(
                    outputDirectory.resolve("tested-rules-report.csv"),
                    testedRules
            );

            writeReport(
                    outputDirectory.resolve("missing-rules-report.csv"),
                    missingRules
            );

            System.out.println();
            System.out.println("==================================================");
            System.out.println("BDD RULE COVERAGE SUMMARY");
            System.out.println("==================================================");
            System.out.printf("%-35s -> %5d%n", "TOTAL DSLR RULES", results.size());
            System.out.printf("%-35s -> %5d%n", "RULES WITH FEATURE COVERAGE", testedRules.size());
            System.out.printf("%-35s -> %5d%n", "RULES MISSING FEATURE COVERAGE", missingRules.size());
            System.out.println("==================================================");
            System.out.println();

            System.out.println("Reports written:");
            System.out.println("  " + outputDirectory.resolve("tested-rules-report.csv"));
            System.out.println("  " + outputDirectory.resolve("missing-rules-report.csv"));
            System.out.println();

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to write coverage reports",
                    exception
            );
        }
    }

    private void writeReport(
            Path outputFile,
            List<RuleCoverageResult> results
    ) throws IOException {
        List<String> lines = new ArrayList<>();

        lines.add("ruleName,dslrFile,tested,featureFiles");

        for (RuleCoverageResult result : results) {
            lines.add(toCsvLine(result));
        }

        Files.write(outputFile, lines);
    }

    private String toCsvLine(RuleCoverageResult result) {
        return csv(result.ruleName()) + ","
                + csv(result.dslrFile()) + ","
                + result.tested() + ","
                + csv(String.join("|", result.bddReferences()));
    }

    private String csv(String value) {
        if (value == null) {
            return "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}