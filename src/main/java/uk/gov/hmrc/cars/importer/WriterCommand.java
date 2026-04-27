package uk.gov.hmrc.cars.importer;

public interface WriterCommand {

    void execute(String[] args);
}