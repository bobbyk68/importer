package uk.gov.hmrc.rules.analysis.xml;

import uk.gov.hmrc.rules.analysis.ConditionOperator;

public record UnmappedCondition(
        String ruleName,
        String conditionPath,
        ConditionOperator operator,
        String value,
        String originalLine
) {
}