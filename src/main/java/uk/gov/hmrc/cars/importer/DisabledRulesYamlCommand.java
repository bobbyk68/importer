package uk.gov.hmrc.cars.importer;

import uk.gov.hmrc.cars.importer.writer.FileDataWriter;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Supplier;

public class DisabledRulesYamlCommand implements WriterCommand {

    private final FileDataWriter<Set<String>> writer;
    private final Supplier<Set<String>> disabledRulesSupplier;

    public DisabledRulesYamlCommand(
            FileDataWriter<Set<String>> writer,
            Supplier<Set<String>> disabledRulesSupplier
    ) {
        this.writer = writer;
        this.disabledRulesSupplier = disabledRulesSupplier;
    }

    @Override
    public void execute(String[] args) {
        Path outputPath = Path.of(args[1]);

        writer.write(disabledRulesSupplier.get(), outputPath);
    }
}