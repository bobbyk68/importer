package uk.gov.hmrc.rules.analysis.bdd;

public record InvalidFeatureReference(
        String featureFile,
        String expectedBaseRule,
        String referencedRule,
        String referencedBaseRule
) {
}