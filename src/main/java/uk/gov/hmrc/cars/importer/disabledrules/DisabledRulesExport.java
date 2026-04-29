package uk.gov.hmrc.cars.importer.disabledrules;

import java.util.Set;

public record DisabledRulesExport(
        Set<String> disabledRules
) {
}