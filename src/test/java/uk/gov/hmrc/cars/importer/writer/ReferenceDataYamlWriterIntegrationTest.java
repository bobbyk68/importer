package uk.gov.hmrc.cars.importer.writer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import uk.gov.hmrc.cars.importer.ReferenceDataWriterConfiguration;
import uk.gov.hmrc.cars.importer.model.CodeLists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest(
        classes = ReferenceDataWriterConfiguration.class
)
class ReferenceDataYamlWriterIntegrationTest {

    @TempDir
    Path tempDir;

    @Autowired
    private FileDataWriter<CodeLists> writer;

    @Test
    void shouldWriteYamlUsingSpringWiredWriter() throws IOException {
        Path outputPath = tempDir.resolve("reference-data.yml");

        CodeLists codeLists = buildCodeLists();

        writer.write(codeLists, outputPath);

        String output = Files.readString(outputPath);

        assertThat(writer).isInstanceOf(ReferenceDataYamlWriter.class);
        assertThat(output).contains("codeLists:");
        assertThat(output).contains("AdditionalInformationTypes:");
        assertThat(output).contains("endDate: null");
    }
}