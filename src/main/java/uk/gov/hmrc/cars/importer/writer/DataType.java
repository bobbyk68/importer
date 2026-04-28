package uk.gov.hmrc.cars.importer.writer;

public enum DataType {

    REFERENCE_DATA("reference-data"),
    DISABLED_RULES("disabled-rules");

    private final String cliValue;

    DataType(String cliValue) {
        this.cliValue = cliValue;
    }

    public static DataType from(String value) {
        for (DataType type : values()) {
            if (type.cliValue.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown data type: " + value);
    }
}