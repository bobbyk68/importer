package uk.gov.hmrc.cars.importer;

import java.util.HashMap;
import java.util.Map;

public class WriterRegistry {

    private final Map<String, Object> writers = new HashMap<>();

    public void register(String key, Object writer) {
        writers.put(key, writer);
    }

    public Object get(String key) {
        Object writer = writers.get(key);

        if (writer == null) {
            throw new IllegalArgumentException("Unknown writer: " + key);
        }

        return writer;
    }
}