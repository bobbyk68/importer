package uk.gov.hmrc.cars.importer;

import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.writer.DataWriter;

import java.util.function.Supplier;

public class ReferenceDataDbCommand implements WriterCommand {

    private final DataWriter<CodeLists> writer;
    private final Supplier<CodeLists> supplier;

    public ReferenceDataDbCommand(
            DataWriter<CodeLists> writer,
            Supplier<CodeLists> supplier
    ) {
        this.writer = writer;
        this.supplier = supplier;
    }

    @Override
    public void execute(String[] args) {
        writer.write(supplier.get());
    }
}