package uk.gov.hmrc.cars.importer.writer;

import java.nio.file.Path;

public record WriterRequest(
        DataType dataType,
        WriterFormat format,
        Path outputPath
) {
}