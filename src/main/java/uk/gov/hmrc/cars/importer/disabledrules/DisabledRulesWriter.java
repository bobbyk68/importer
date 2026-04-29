package uk.gov.hmrc.cars.importer.disabledrules;

import java.nio.file.Path;
import java.util.Set;

public class DisabledRulesWriter {

    @Override
    public void write(Set<String> disabledRules, Path outputPath) {

        DisabledRulesExport export = new DisabledRulesExport(disabledRules);

        yamlMapper.writeValue(outputPath.toFile(), export);
    }
}
