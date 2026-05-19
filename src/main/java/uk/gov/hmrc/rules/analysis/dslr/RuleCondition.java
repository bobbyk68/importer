package uk.gov.hmrc.rules.analysis.dslr;

import uk.gov.hmrc.rules.analysis.ConditionOperator;

import java.util.Set;

public record RuleCondition(
        String originalLine,
        String fieldPath,
        ConditionOperator operator,
        Set<String> values
) {
}