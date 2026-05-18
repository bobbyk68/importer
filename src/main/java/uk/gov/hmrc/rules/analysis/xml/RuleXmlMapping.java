package uk.gov.hmrc.rules.analysis.xml;

import java.util.List;

public record RuleXmlMapping(
        String conditionPath,
        List<String> requiredFragments,
        String targetXmlPath
) {
}