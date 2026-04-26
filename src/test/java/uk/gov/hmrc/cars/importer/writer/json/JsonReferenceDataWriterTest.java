package uk.gov.hmrc.cars.importer.writer.json;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import uk.gov.hmrc.cars.importer.model.CodeList;
import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.model.Value;
import uk.gov.hmrc.cars.importer.writer.mapper.CodeListsExportMapper;
import uk.gov.hmrc.cars.importer.writer.support.ObjectMapperFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JsonReferenceDataWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldWriteOnlyExportFieldsToJsonAndKeepNullEndDate() throws IOException {
        Path outputPath = tempDir.resolve("reference-data.json");

        JsonReferenceDataWriter writer = new JsonReferenceDataWriter(
                outputPath,
                ObjectMapperFactory.jsonMapper(),
                new CodeListsExportMapper()
        );

        writer.write(buildCodeLists());

        String json = Files.readString(outputPath);

        assertThat(json).contains(""codeLists"");
        assertThat(json).contains(""AdditionalInformationTypes"");
        assertThat(json).contains(""ACA"");
        assertThat(json).contains(""AES"");
        assertThat(json).contains(""startDate" : "2001-01-01T00:00:00"");
        assertThat(json).contains(""endDate" : null");
        assertThat(json).contains(""description" : "Specification of the documentary requirements"");
        assertThat(json).contains(""description" : "Textual Explanation"");

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
