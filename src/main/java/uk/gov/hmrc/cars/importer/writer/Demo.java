package uk.gov.hmrc.cars.importer.writer;

import uk.gov.hmrc.cars.importer.DisabledRulesYamlWriter;
import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.writer.mapper.CodeListsExportMapper;

import java.nio.file.Path;
import java.util.Set;

public class Demo {

    public static void main(String[] args) {
        WriterFactory factory = new WriterFactory();

        factory.registerReferenceDataFileWriter(
                "yaml",
                new ReferenceDataYamlWriter(yamlMapper, new CodeListsExportMapper())
        );

        factory.registerReferenceDataFileWriter(
                "json",
                new ReferenceDataJsonWriter(jsonMapper, new CodeListsExportMapper())
        );

        factory.registerReferenceDataDataWriter(
                "db",
                new ReferenceDataDbWriter(repository)
        );

        factory.registerDisabledRulesFileWriter(
                "yaml",
                new DisabledRulesYamlWriter(yamlMapper)
        );

        String dataType = args[0];   // reference-data / disabled-rules
        String format = args[1];     // yaml / json / db

        switch (dataType) {

            case "reference-data" -> {
                CodeLists codeLists = loadCodeLists();

                if ("db".equals(format)) {
                    factory.referenceDataDataWriter(format)
                            .write(codeLists);
                } else {
                    Path outputPath = Path.of(args[2]);

                    factory.referenceDataFileWriter(format)
                            .write(codeLists, outputPath);
                }
            }

            case "disabled-rules" -> {
                Set<String> disabledRules = loadDisabledRules();
                Path outputPath = Path.of(args[2]);

                factory.disabledRulesFileWriter(format)
                        .write(disabledRules, outputPath);
            }

            default -> throw new IllegalArgumentException("Unknown data type: " + dataType);
        }

    }
}
