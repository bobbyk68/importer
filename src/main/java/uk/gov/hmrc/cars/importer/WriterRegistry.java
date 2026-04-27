package uk.gov.hmrc.cars.importer;

import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.writer.DataWriter;
import uk.gov.hmrc.cars.importer.writer.FileDataWriter;
import uk.gov.hmrc.cars.importer.writer.ReferenceDataDbWriter;
import uk.gov.hmrc.cars.importer.writer.ReferenceDataYamlWriter;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class WriterRegistry<T> {

    private final Map<String, DataWriter<T>> dataWriters = new LinkedHashMap<>();
    private final Map<String, FileDataWriter<T>> fileWriters = new LinkedHashMap<>();

    public void registerDataWriter(String key, DataWriter<T> writer) {
        dataWriters.put(key, writer);
    }

    public void registerFileWriter(String key, FileDataWriter<T> writer) {
        fileWriters.put(key, writer);
    }

    public DataWriter<T> getDataWriter(String key) {
        DataWriter<T> writer = dataWriters.get(key);

        if (writer == null) {
            throw new IllegalArgumentException("Unknown data writer: " + key);
        }

        return writer;
    }

    public FileDataWriter<T> getFileWriter(String key) {
        FileDataWriter<T> writer = fileWriters.get(key);

        if (writer == null) {
            throw new IllegalArgumentException("Unknown file writer: " + key);
        }

        return writer;
    }
}

WriterRegistry<CodeLists> referenceDataRegistry = new WriterRegistry<>();

referenceDataRegistry.registerFileWriter(
        "yaml",
                new ReferenceDataYamlWriter(yamlMapper, mapper)
);

        referenceDataRegistry.registerFileWriter(
        "json",
                new ReferenceDataJsonWriter(jsonMapper, mapper)
);

        referenceDataRegistry.registerDataWriter(
        "db",
                new ReferenceDataDbWriter(repository)
);


String writerType = args[0];

if ("db".equals(writerType)) {
        referenceDataRegistry
        .getDataWriter(writerType)
            .write(codeLists);
} else {
Path outputPath = Path.of(args[1]);

    referenceDataRegistry
            .getFileWriter(writerType)
            .write(codeLists, outputPath);
}

WriterRegistry<Set<String>> disabledRulesRegistry = new WriterRegistry<>();

disabledRulesRegistry.registerFileWriter(
        "yaml",
                new DisabledRulesYamlWriter(yamlMapper)
);