package uk.gov.hmrc.cars.importer.writer;

import uk.gov.hmrc.cars.importer.WriterRegistry;
import uk.gov.hmrc.cars.importer.model.CodeLists;

import java.util.Set;

public class WriterFactory {

    private final WriterRegistry<FileDataWriter<CodeLists>> referenceDataFileRegistry =
            new WriterRegistry<>();

    private final WriterRegistry<DataWriter<CodeLists>> referenceDataDataRegistry =
            new WriterRegistry<>();

    private final WriterRegistry<FileDataWriter<Set<String>>> disabledRulesFileRegistry =
            new WriterRegistry<>();

    // ===== Registration =====

    public void registerReferenceDataFileWriter(String key, FileDataWriter<CodeLists> writer) {
        referenceDataFileRegistry.register(key, writer);
    }

    public void registerReferenceDataDataWriter(String key, DataWriter<CodeLists> writer) {
        referenceDataDataRegistry.register(key, writer);
    }

    public void registerDisabledRulesFileWriter(String key, FileDataWriter<Set<String>> writer) {
        disabledRulesFileRegistry.register(key, writer);
    }

    // ===== Retrieval =====

    public FileDataWriter<CodeLists> referenceDataFileWriter(String format) {
        return referenceDataFileRegistry.get(format, "reference data file writer");
    }

    public DataWriter<CodeLists> referenceDataDataWriter(String format) {
        return referenceDataDataRegistry.get(format, "reference data data writer");
    }

    public FileDataWriter<Set<String>> disabledRulesFileWriter(String format) {
        return disabledRulesFileRegistry.get(format, "disabled rules file writer");
    }
}