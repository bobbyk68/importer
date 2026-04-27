package uk.gov.hmrc.cars.importer.writer;

import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.writer.model.CodeListsExport;

import java.io.IOException;
import java.nio.file.Path;

public class ReferenceDataYamlWriter implements FileDataWriter<CodeLists> {

    @Override
    public void write(CodeLists data, Path path) {
        CodeListsExport export = mapper.toExport(data);

        try {
            yamlMapper.writeValue(path.toFile(), export);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write YAML file: " + path, e);
        }
    }
}