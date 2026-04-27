package uk.gov.hmrc.cars.importer.writer.json;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import uk.gov.hmrc.cars.importer.ReferenceDataDbCommand;
import uk.gov.hmrc.cars.importer.ReferenceDataYamlCommand;
import uk.gov.hmrc.cars.importer.WriterCommand;
import uk.gov.hmrc.cars.importer.WriterRegistry;
import uk.gov.hmrc.cars.importer.model.CodeList;
import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.model.Value;
import uk.gov.hmrc.cars.importer.writer.DataWriter;
import uk.gov.hmrc.cars.importer.writer.FileDataWriter;
import uk.gov.hmrc.cars.importer.writer.ReferenceDataDbWriter;
import uk.gov.hmrc.cars.importer.writer.ReferenceDataYamlWriter;
import uk.gov.hmrc.cars.importer.writer.mapper.CodeListsExportMapper;
import uk.gov.hmrc.cars.importer.writer.support.ObjectMapperFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JsonReferenceDataWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldWriteOnlyExportFieldsToJsonAndKeepNullEndDate() throws IOException {

        WriterRegistry registry = new WriterRegistry();

        registry.register("yaml",
                new ReferenceDataYamlWriter(
                        ObjectMapperFactory.yamlMapper(),
                        new CodeListsExportMapper()
                ));

        registry.register("json",
                new ReferenceDataJsonWriter(
                        ObjectMapperFactory.jsonMapper(),
                        new CodeListsExportMapper()
                ));

        registry.register("db",
                new ReferenceDataDbWriter(repository()));

        registry.register("disabled-rules-yaml",
                new DisabledRulesYamlWriter(
                        ObjectMapperFactory.yamlMapper()
                ));

        String writerType = args[0];
        Object writer = registry.get(writerType);

        if (writer instanceof FileDataWriter<CodeLists> fileWriter) {
            Path path = Path.of(args[1]);
            fileWriter.write(codeLists, path);
        }
        else if (writer instanceof DataWriter<CodeLists> dataWriter) {
            dataWriter.write(codeLists);
        }
        else if (writer instanceof FileDataWriter<Set<String>> rulesWriter) {
            Path path = Path.of(args[1]);
            rulesWriter.write(disabledRules, path);
        }

        Map<String, WriterCommand> commands = Map.of(
                "yaml", new ReferenceDataYamlCommand(yamlWriter, this::loadCodeLists),
                "json", new ReferenceDataYamlCommand(jsonWriter, this::loadCodeLists),
                "db", new ReferenceDataDbCommand(dbWriter, this::loadCodeLists),
                "disabled-rules-yaml", new DisabledRulesYamlCommand(rulesWriter, this::loadRules)
        );

        String writerType = args[0];

        WriterCommand command = commands.get(writerType);

        if (command == null) {
            throw new IllegalArgumentException("Unknown writer: " + writerType);
        }

        command.execute(args);


        Path outputPath = tempDir.resolve("reference-data.json");

        JsonReferenceDataWriter writer = new JsonReferenceDataWriter(
                outputPath,
                ObjectMapperFactory.jsonMapper(),
                new CodeListsExportMapper()
        );

        writer.write(buildCodeLists());

        String json = Files.readString(outputPath);

        assertThat(json).contains("\"codeLists\"");
        assertThat(json).contains("\"AdditionalInformationTypes\"");
        assertThat(json).contains("\"ACA\"");
        assertThat(json).contains("\"AES\"");
        assertThat(json).contains("\"startDate\" : \"2001-01-01T00:00:00\"");
        assertThat(json).contains("\"endDate\" : null");
        assertThat(json).contains("\"description\" : \"Specification of the documentary requirements\"");
        assertThat(json).contains("\"description\" : \"Textual Explanation\"");

        assertThat(json).doesNotContain("internalId");
        assertThat(json).doesNotContain("sourceSystem");
        assertThat(json).doesNotContain("internalOwner");
    }

    private CodeLists buildCodeLists() {
        CodeLists codeLists = new CodeLists();

        CodeList additionalInformationTypes = new CodeList(
                "AdditionalInformationTypes",
                "CDS",
                "internal-owner-not-for-output"
        );

        additionalInformationTypes.addValues(
                "ACA",
                List.of(new Value(
                        LocalDateTime.of(2001, 1, 1, 0, 0),
                        null,
                        "Specification of the documentary requirements",
                        "internal-aca-id-not-for-output",
                        "CDS"
                ))
        );

        additionalInformationTypes.addValues(
                "AES",
                List.of(new Value(
                        LocalDateTime.of(2001, 1, 1, 0, 0),
                        null,
                        "Textual Explanation",
                        "internal-aes-id-not-for-output",
                        "CDS"
                ))
        );

        codeLists.addCodeList("AdditionalInformationTypes", additionalInformationTypes);

        return codeLists;
    }
}
