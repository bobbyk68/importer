package uk.gov.hmrc.cars.importer.writer.model;

import java.time.LocalDateTime;

public record ValueExport(
        LocalDateTime startDate,
        LocalDateTime endDate,
        String description
) {
}
