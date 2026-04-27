package uk.gov.hmrc.cars.importer;

import java.util.Set;

public record DisabledRulesExport(
        Set<String> disabledRules
) {
}