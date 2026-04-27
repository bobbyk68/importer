package uk.gov.hmrc.cars.importer;

import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.writer.FileDataWriter;

import java.nio.file.Path;
import java.util.function.Supplier;

public class ReferenceDataYamlCommand implements WriterCommand {

    private final FileDataWriter<CodeLists> writer;
    private final Supplier<CodeLists> supplier;

    public ReferenceDataYamlCommand(
            FileDataWriter<CodeLists> writer,
            Supplier<CodeLists> supplier
    ) {
        this.writer = writer;
        this.supplier = supplier;
    }

    @Override
    public void execute(String[] args) {
        Path path = Path.of(args[1]);
        writer.write(supplier.get(), path);
    }
}