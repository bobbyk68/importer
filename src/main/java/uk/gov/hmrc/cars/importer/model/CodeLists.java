package uk.gov.hmrc.cars.importer.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class CodeLists {

    private final Map<String, CodeList> codeLists = new LinkedHashMap<>();

    public Map<String, CodeList> getCodeLists() {
        return codeLists;
    }

    public void addCodeList(String name, CodeList codeList) {
        this.codeLists.put(name, codeList);
    }
}
