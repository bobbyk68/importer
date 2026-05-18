package uk.gov.hmrc.rules.analysis.dslr;

import uk.gov.hmrc.rules.analysis.RuleCondition;

import java.util.List;

public record RuleDefinition(
        String sourceFile,
        String ruleName,
        List<RuleCondition> conditions
) {
}