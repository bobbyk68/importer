package uk.gov.hmrc.rules.analysis;

import java.util.List;

public record ParsedDslrRule(
        String sourceFile,
        String ruleName,
        List<String> whenLines,
        List<String> thenLines
) {
}