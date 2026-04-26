package uk.gov.hmrc.cars.importer.model;

import java.time.LocalDateTime;

public class Value {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String description;
    private final String internalId;
    private final String sourceSystem;

    public Value(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String description,
            String internalId,
            String sourceSystem
    ) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.internalId = internalId;
        this.sourceSystem = sourceSystem;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    public String getInternalId() {
        return internalId;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }
}
