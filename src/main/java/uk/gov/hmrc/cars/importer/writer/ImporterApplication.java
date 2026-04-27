package uk.gov.hmrc.cars.importer.writer;

import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.writer.mapper.CodeListsExportMapper;
import uk.gov.hmrc.cars.importer.writer.support.ObjectMapperFactory;

import java.nio.file.Path;

public class ImporterApplication {

    public static void main(String[] args) {
        String writerType = args[0];

        CodeLists codeLists = loadCodeLists();

        switch (writerType) {
            case "yaml" -> {
                Path outputPath = Path.of(args[1]);

                FileDataWriter<CodeLists> writer =
                        new ReferenceDataYamlWriter(
                                ObjectMapperFactory.yamlMapper(),
                                new CodeListsExportMapper()
                        );

                writer.write(codeLists, outputPath);
            }

            case "json" -> {
                Path outputPath = Path.of(args[1]);

                FileDataWriter<CodeLists> writer =
                        new ReferenceDataJsonWriter(
                                ObjectMapperFactory.jsonMapper(),
                                new CodeListsExportMapper()
                        );

                writer.write(codeLists, outputPath);
            }

            case "db" -> {
                DataWriter<CodeLists> writer =
                        new ReferenceDataDbWriter(repository());

                writer.write(codeLists);
            }

            default -> throw new IllegalArgumentException("Unknown writer type: " + writerType);
        }
    }

    private static CodeLists loadCodeLists() {
        // load/build CodeLists here
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private static ReferenceDataRepository repository() {
        // build repository/db connection here
        throw new UnsupportedOperationException("Not implemented yet");
    }
}