package uk.gov.hmrc.cars.importer.writer;

public enum WriterFormat {

    YAML("yaml"),
    JSON("json"),
    DB("db");

    private final String cliValue;

    WriterFormat(String cliValue) {
        this.cliValue = cliValue;
    }

    public String cliValue() {
        return cliValue;
    }

    public static WriterFormat from(String value) {
        for (WriterFormat format : values()) {
            if (format.cliValue.equalsIgnoreCase(value)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Unknown format: " + value);
    }
}