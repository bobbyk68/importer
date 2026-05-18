package uk.gov.hmrc.rules.analysis.report;

import java.util.List;

public record RuleCoverageResult(
        String ruleName,
        String dslrFile,
        boolean tested,
        List<String> bddReferences
) {
}