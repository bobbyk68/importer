package uk.gov.hmrc.cars.importer.writer;

import uk.gov.hmrc.cars.importer.DisabledRulesYamlWriter;
import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.writer.support.ObjectMapperFactory;

import java.util.Set;
import java.util.function.Supplier;

public class WriterRouter {

    private final WriterFactory factory;
    private final Supplier<CodeLists> codeListsSupplier;
    private final Supplier<Set<String>> disabledRulesSupplier;

    public WriterRouter(
            WriterFactory factory,
            Supplier<CodeLists> codeListsSupplier,
            Supplier<Set<String>> disabledRulesSupplier
    ) {
        this.factory = factory;
        this.codeListsSupplier = codeListsSupplier;
        this.disabledRulesSupplier = disabledRulesSupplier;
    }

    public static WriterRouter createDefault() {
        WriterFactory factory = new WriterFactory();

        ReferenceDataExportMapper mapper = new ReferenceDataExportMapper();

        factory.registerReferenceDataFileWriter(
                "yaml",
                new ReferenceDataYamlWriter(
                        ObjectMapperFactory.yamlMapper(),
                        mapper
                )
        );

        factory.registerReferenceDataFileWriter(
                "json",
                new ReferenceDataJsonWriter(
                        ObjectMapperFactory.jsonMapper(),
                        mapper
                )
        );

        factory.registerReferenceDataDataWriter(
                "db",
                new ReferenceDataDbWriter(repository())
        );

        factory.registerDisabledRulesFileWriter(
                "yaml",
                new DisabledRulesYamlWriter(
                        ObjectMapperFactory.yamlMapper()
                )
        );

        return new WriterRouter(
                factory,
                WriterRouter::loadReferenceData,
                WriterRouter::loadDisabledRules
        );
    }

    public void route(WriteRequest request) {
        switch (request.dataType()) {
            case REFERENCE_DATA -> writeReferenceData(request);
            case DISABLED_RULES -> writeDisabledRules(request);
        }
    }

    private void writeReferenceData(WriteRequest request) {
        CodeLists data = codeListsSupplier.get();

        if (request.format() == OutputFormat.DB) {
            factory.referenceDataDataWriter("db").write(data);
            return;
        }

        factory.referenceDataFileWriter(request.format().cliValue())
                .write(data, request.outputPath());
    }

    private void writeDisabledRules(WriteRequest request) {
        Set<String> data = disabledRulesSupplier.get();

        factory.disabledRulesFileWriter(request.format().cliValue())
                .write(data, request.outputPath());
    }

    private static CodeLists loadReferenceData() {
        throw new UnsupportedOperationException("Load CodeLists here");
    }

    private static Set<String> loadDisabledRules() {
        throw new UnsupportedOperationException("Load disabled rules here");
    }

    private static ReferenceDataRepository repository() {
        throw new UnsupportedOperationException("Create repository here");
    }
}