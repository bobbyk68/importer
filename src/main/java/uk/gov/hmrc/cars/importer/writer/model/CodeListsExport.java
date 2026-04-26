package uk.gov.hmrc.cars.importer.writer.model;

import java.util.Map;

public record CodeListsExport(
        Map<String, CodeListExport> codeLists
) {
}
