package uk.gov.hmrc.rules.analysis;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleConditionExtractor {

    public RuleDefinition extract(ParsedDslrRule parsedRule) {

        List<RuleCondition> conditions = parsedRule.whenLines().stream()
                .map(this::extractCondition)
                .filter(Objects::nonNull)
                .toList();

        return new RuleDefinition(
                parsedRule.sourceFile(),
                parsedRule.ruleName(),
                conditions
        );
    }

    private RuleCondition extractCondition(String line) {

        String trimmed = line.trim();

        RuleCondition equalsCondition = tryExtractEqualsCondition(trimmed);

        if (equalsCondition != null) {
            return equalsCondition;
        }

        RuleCondition existsCondition = tryExtractExistsCondition(trimmed);

        if (existsCondition != null) {
            return existsCondition;
        }

        return new RuleCondition(
                trimmed,
                "UNKNOWN",
                ConditionOperator.UNKNOWN,
                Set.of()
        );
    }

    private RuleCondition tryExtractExistsCondition(String line) {

        if (!line.contains(" exists")) {
            return null;
        }

        String fieldPath = line
                .replace("- with ", "")
                .replace(" exists", "")
                .trim();

        fieldPath = normaliseFieldPath(fieldPath);

        return new RuleCondition(
                line,
                fieldPath,
                ConditionOperator.EXISTS,
                Set.of()
        );
    }

    private RuleCondition tryExtractEqualsCondition(String line) {

        Pattern pattern = Pattern.compile(
                "- with (.+?) equals \"([^\"]+)\""
        );

        Matcher matcher = pattern.matcher(line);

        if (!matcher.find()) {
            return null;
        }

        String rawField = matcher.group(1).trim();
        String value = matcher.group(2).trim();

        String fieldPath = normaliseFieldPath(rawField);

        return new RuleCondition(
                line,
                fieldPath,
                ConditionOperator.EQUALS,
                Set.of(value)
        );
    }
    private String normaliseFieldPath(String rawField) {

        return rawField
                .replace("requested procedure code", "requestedProcedure.code")
                .replace("previous document type code", "previousDocument.typeCode")
                .replace("declaration type code", "declaration.typeCode")
                .replace("procedure category", "procedureCategory")
                .replace(" type code", ".typeCode")
                .replace(" code", ".code")
                .replace(' ', '.');
    }
}