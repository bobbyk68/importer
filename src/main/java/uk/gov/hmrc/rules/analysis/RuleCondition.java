package uk.gov.hmrc.rules.analysis;

import java.util.Set;

public record RuleCondition(
        String originalLine,
        String fieldPath,
        ConditionOperator operator,
        Set<String> values
) {
}