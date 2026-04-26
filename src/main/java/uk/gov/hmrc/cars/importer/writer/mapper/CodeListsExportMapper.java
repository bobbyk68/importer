package uk.gov.hmrc.cars.importer.writer.mapper;

import uk.gov.hmrc.cars.importer.model.CodeList;
import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.model.Value;
import uk.gov.hmrc.cars.importer.writer.model.CodeListExport;
import uk.gov.hmrc.cars.importer.writer.model.CodeListsExport;
import uk.gov.hmrc.cars.importer.writer.model.ValueExport;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CodeListsExportMapper {

    public CodeListsExport toExport(CodeLists source) {
        Map<String, CodeListExport> result = new LinkedHashMap<>();

        for (Map.Entry<String, CodeList> entry : source.getCodeLists().entrySet()) {
            result.put(entry.getKey(), toExport(entry.getValue()));
        }

        return new CodeListsExport(result);
    }

    private CodeListExport toExport(CodeList source) {
        Map<String, List<ValueExport>> values = new LinkedHashMap<>();

        for (Map.Entry<String, List<Value>> entry : source.getValues().entrySet()) {
            values.put(
                    entry.getKey(),
                    entry.getValue()
                            .stream()
                            .map(this::toExport)
                            .toList()
            );
        }

        return new CodeListExport(values);
    }

    private ValueExport toExport(Value source) {
        return new ValueExport(
                source.getStartDate(),
                source.getEndDate(),
                source.getDescription()
        );
    }
}
