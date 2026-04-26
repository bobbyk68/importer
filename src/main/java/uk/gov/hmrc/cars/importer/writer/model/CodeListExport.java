package uk.gov.hmrc.cars.importer.writer.model;

import java.util.List;
import java.util.Map;

public record CodeListExport(
        Map<String, List<ValueExport>> values
) {
}
