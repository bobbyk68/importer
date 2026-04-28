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

    private final Map<String, T> writers = new LinkedHashMap<>();

    public void register(String key, T writer) {
        writers.put(key, writer);
    }

    public T get(String key, String type) {
        T writer = writers.get(key);

        if (writer == null) {
            throw new IllegalArgumentException("Unknown " + type + ": " + key);
        }

        return writer;
    }

    public Set<String> supportedKeys() {
        return writers.keySet();
    }
}