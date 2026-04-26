package uk.gov.hmrc.cars.importer.writer.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.writer.ReferenceDataWriter;
import uk.gov.hmrc.cars.importer.writer.mapper.CodeListsExportMapper;
import uk.gov.hmrc.cars.importer.writer.model.CodeListsExport;

import java.io.IOException;
import java.nio.file.Path;

public class JsonReferenceDataWriter implements ReferenceDataWriter {

    private final Path outputPath;
    private final ObjectMapper jsonMapper;
    private final CodeListsExportMapper exportMapper;

    public JsonReferenceDataWriter(
            Path outputPath,
            ObjectMapper jsonMapper,
            CodeListsExportMapper exportMapper
    ) {
        this.outputPath = outputPath;
        this.jsonMapper = jsonMapper;
        this.exportMapper = exportMapper;
    }

    @Override
    public void write(CodeLists codeLists) {
        CodeListsExport exportModel = exportMapper.toExport(codeLists);

        try {
            jsonMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(outputPath.toFile(), exportModel);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write JSON file: " + outputPath, e);
        }
    }
}
