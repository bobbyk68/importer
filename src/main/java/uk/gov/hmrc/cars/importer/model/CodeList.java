package uk.gov.hmrc.cars.importer.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CodeList {

    private final String name;
    private final String sourceSystem;
    private final String internalOwner;
    private final Map<String, List<Value>> values = new LinkedHashMap<>();

    public CodeList(String name, String sourceSystem, String internalOwner) {
        this.name = name;
        this.sourceSystem = sourceSystem;
        this.internalOwner = internalOwner;
    }

    public String getName() {
        return name;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public String getInternalOwner() {
        return internalOwner;
    }

    public Map<String, List<Value>> getValues() {
        return values;
    }

    public void addValues(String code, List<Value> values) {
        this.values.put(code, values);
    }
}
