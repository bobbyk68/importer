package uk.gov.hmrc.rules.analysis.report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RuleCoverageReportWriter {

    public void write(Path outputDirectory, List<RuleCoverageResult> results) {
        try {
            Files.createDirectories(outputDirectory);

            Path outputFile = outputDirectory.resolve("rule-coverage-report.csv");

            List<String> lines = new ArrayList<>();
            lines.add("ruleName,dslrFile,tested,bddReferences");

            for (RuleCoverageResult result : results) {
                lines.add(toCsvLine(result));
            }

            Files.write(outputFile, lines);

            System.out.println("Coverage report written:");
            System.out.println("  " + outputFile);

        } catch (IOException exception) {
            throw new IllegalStateException("Failed to write rule coverage report", exception);
        }
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