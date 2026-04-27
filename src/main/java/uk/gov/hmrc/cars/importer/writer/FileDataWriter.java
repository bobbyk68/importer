package uk.gov.hmrc.cars.importer.writer;

import java.nio.file.Path;

public interface FileDataWriter<T> extends DataWriter<T> {

    void write(T data, Path path);

}