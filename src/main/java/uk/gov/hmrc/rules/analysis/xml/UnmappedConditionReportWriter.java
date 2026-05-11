package uk.gov.hmrc.rules.analysis.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UnmappedConditionReportWriter {

    public void write(
            Path outputDirectory,
            List<UnmappedCondition> unmappedConditions
    ) {
        try {
            Files.createDirectories(outputDirectory);

            Path reportFile = outputDirectory.resolve("unmapped-fields-report.csv");

            List<String> lines = new ArrayList<>();
            lines.add("ruleName,conditionPath,operator,value,originalLine");

            for (UnmappedCondition condition : unmappedConditions) {
                lines.add(toCsvLine(condition));
            }

            Files.write(reportFile, lines);

            System.out.println("Unmapped report:");
            System.out.println("  " + reportFile);

        } catch (IOException exception) {
            throw new IllegalStateException("Failed to write unmapped condition report", exception);
        }
    }

    private String toCsvLine(UnmappedCondition condition) {
        return csv(condition.ruleName()) + ","
                + csv(condition.conditionPath()) + ","
                + csv(condition.operator().name()) + ","
                + csv(condition.value()) + ","
                + csv(condition.originalLine());
    }

    private String csv(String value) {
        if (value == null) {
            return "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}