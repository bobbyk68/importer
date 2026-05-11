package uk.gov.hmrc.rules.analysis.xml;

import java.util.List;

public record XmlUpdateResult(
        String ruleName,
        String xml,
        List<UnmappedCondition> unmappedConditions
) {
}