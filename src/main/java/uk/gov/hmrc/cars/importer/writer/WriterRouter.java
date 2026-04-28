package uk.gov.hmrc.cars.importer.writer;

import uk.gov.hmrc.cars.importer.DisabledRulesYamlWriter;
import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.writer.support.ObjectMapperFactory;

import java.util.Set;
import java.util.function.Supplier;
public class WriterRouter {

    private final WriterFactory factory;

    public WriterRouter(WriterFactory factory) {
        this.factory = factory;
    }

    public void writeReferenceData(CodeLists codeLists, WriterRequest request) {
        if (request.dataType() != DataType.REFERENCE_DATA) {
            throw new IllegalArgumentException("Expected reference-data request");
        }

        if (request.format() == OutputFormat.DB) {
            factory.referenceDataDataWriter("db")
                    .write(codeLists);
            return;
        }

        factory.referenceDataFileWriter(request.format().cliValue())
                .write(codeLists, request.outputPath());
    }

    public void writeDisabledRules(Set<String> disabledRules, WriterRequest request) {
        if (request.dataType() != DataType.DISABLED_RULES) {
            throw new IllegalArgumentException("Expected disabled-rules request");
        }

        factory.disabledRulesFileWriter(request.format().cliValue())
                .write(disabledRules, request.outputPath());
    }
}