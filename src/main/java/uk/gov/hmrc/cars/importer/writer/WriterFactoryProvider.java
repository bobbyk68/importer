package uk.gov.hmrc.cars.importer.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.hmrc.cars.importer.DisabledRulesYamlWriter;

public final class WriterFactoryProvider {

    private WriterFactoryProvider() {
    }

    public static WriterFactory create(
            ObjectMapper yamlMapper,
            ObjectMapper jsonMapper,
            ReferenceDataExportMapper referenceDataExportMapper,
            ReferenceDataDbWriter referenceDataDbWriter
    ) {
        WriterFactory factory = new WriterFactory();

        factory.registerReferenceDataFileWriter(
                "yaml",
                new ReferenceDataYamlWriter(yamlMapper, referenceDataExportMapper)
        );

        factory.registerReferenceDataFileWriter(
                "json",
                new ReferenceDataJsonWriter(jsonMapper, referenceDataExportMapper)
        );

        factory.registerReferenceDataDataWriter(
                "db",
                referenceDataDbWriter
        );

        factory.registerDisabledRulesFileWriter(
                "yaml",
                new DisabledRulesYamlWriter(yamlMapper)
        );

        return factory;
    }
}