package uk.gov.hmrc.cars.importer.writer;

import uk.gov.hmrc.cars.importer.DisabledRulesYamlWriter;
import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.writer.mapper.CodeListsExportMapper;
import uk.gov.hmrc.cars.importer.writer.support.ObjectMapperFactory;

import java.nio.file.Path;
import java.util.Set;

public class Demo {

    public static void main(String[] args) {
        WriterFactory factory = new WriterFactory();

        WriteRequest request = WriteRequestParser.parse(args);
        WriterRouter router = new WriterRouter(factory);

        switch (request.dataType()) {
            case REFERENCE_DATA -> {
                CodeLists codeLists = loadCodeLists();
                router.writeReferenceData(codeLists, request);
            }

            case DISABLED_RULES -> {
                Set<String> disabledRules = loadDisabledRules();
                router.writeDisabledRules(disabledRules, request);
            }
        }

        WriterFactory factory = WriterFactoryProvider.create(
                ObjectMapperFactory.yamlMapper(),
                ObjectMapperFactory.jsonMapper(),
                new ReferenceDataExportMapper(),
                new ReferenceDataDbWriter(repository)
        );

        WriterRouter router = new WriterRouter(factory);

        WriterFactory factory = new WriterFactory();

        factory.registerReferenceDataFileWriter("yaml", new ReferenceDataYamlWriter(yamlMapper, mapper));
        factory.registerReferenceDataFileWriter("json", new ReferenceDataJsonWriter(jsonMapper, mapper));
        factory.registerReferenceDataDataWriter("db", new ReferenceDataDbWriter(repository));
        factory.registerDisabledRulesFileWriter("yaml", new DisabledRulesYamlWriter(yamlMapper));
    }
}
