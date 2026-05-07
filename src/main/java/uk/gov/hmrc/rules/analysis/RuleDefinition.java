package uk.gov.hmrc.rules.analysis;

import java.util.List;

public record RuleDefinition(
        String sourceFile,
        String ruleName,
        List<RuleCondition> conditions
) {
}