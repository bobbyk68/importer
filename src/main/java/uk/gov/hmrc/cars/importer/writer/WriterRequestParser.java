package uk.gov.hmrc.cars.importer.writer;

import java.nio.file.Path;

public final class WriterRequestParser {

    private WriterRequestParser() {
    }

    public static WriterRequest parse(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException(
                    "Usage: <data-type> <format> [output-path]"
            );
        }

        DataType dataType = DataType.from(args[0]);
        WriterFormat format = WriterFormat.from(args[1]);

        Path outputPath = format == WriterFormat.DB
                ? null
                : Path.of(args[2]);

        return new WriterRequest(dataType, format, outputPath);
    }
}