package uk.gov.hmrc.cars.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.hmrc.cars.importer.writer.FileDataWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public class DisabledRulesYamlWriter implements FileDataWriter<Set<String>> {

    private final ObjectMapper yamlMapper;

    public DisabledRulesYamlWriter(ObjectMapper yamlMapper) {
        this.yamlMapper = yamlMapper;
    }

    @Override
    public void write(Set<String> disabledRules, Path path) {
        DisabledRulesExport export = new DisabledRulesExport(disabledRules);

        try {
            yamlMapper.writeValue(path.toFile(), export);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write disabled rules YAML file: " + path, e);
        }
    }
}