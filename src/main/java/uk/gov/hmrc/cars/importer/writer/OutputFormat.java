package uk.gov.hmrc.cars.importer.writer;

public enum OutputFormat {

    YAML("yaml"),
    JSON("json"),
    DB("db");

    private final String cliValue;

    OutputFormat(String cliValue) {
        this.cliValue = cliValue;
    }

    public String cliValue() {
        return cliValue;
    }

    public static OutputFormat from(String value) {
        for (OutputFormat format : values()) {
            if (format.cliValue.equalsIgnoreCase(value)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Unknown format: " + value);
    }
}