package uk.gov.hmrc.cars.importer.writer.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.writer.ReferenceDataWriter;
import uk.gov.hmrc.cars.importer.writer.mapper.CodeListsExportMapper;
import uk.gov.hmrc.cars.importer.writer.model.CodeListsExport;

import java.io.IOException;
import java.nio.file.Path;

public class YamlReferenceDataWriter implements ReferenceDataWriter {

    private final Path outputPath;
    private final ObjectMapper yamlMapper;
    private final CodeListsExportMapper exportMapper;

    public YamlReferenceDataWriter(
            Path outputPath,
            ObjectMapper yamlMapper,
            CodeListsExportMapper exportMapper
    ) {
        this.outputPath = outputPath;
        this.yamlMapper = yamlMapper;
        this.exportMapper = exportMapper;
    }

    @Override
    public void write(CodeLists codeLists) {
        CodeListsExport exportModel = exportMapper.toExport(codeLists);

        try {
            yamlMapper.writeValue(outputPath.toFile(), exportModel);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write YAML file: " + outputPath, e);
        }
    }
}
