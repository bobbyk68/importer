package uk.gov.hmrc.cars.importer.writer;

public interface DataWriter<T> {
    void write(T data);
}