package uk.gov.hmrc.rules.analysis.bdd;

import java.nio.file.Path;

public record BddRuleReference(
        String ruleName,
        Path sourceFile
) {
}