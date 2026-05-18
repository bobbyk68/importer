package uk.gov.hmrc.rules.analysis.cli;

import java.nio.file.Files;
import java.nio.file.Path;

public class CommandLineOptions {

    private final Path dslrDirectory;
    private final Path bddDirectory;
    private final Path outputDirectory;

    private CommandLineOptions(
            Path dslrDirectory,
            Path bddDirectory,
            Path outputDirectory
    ) {
        this.dslrDirectory = dslrDirectory;
        this.bddDirectory = bddDirectory;
        this.outputDirectory = outputDirectory;
    }

    public static CommandLineOptions from(String[] args) {
        if (args.length != 6) {
            throw new IllegalArgumentException("""
                    Usage:
                      java -jar rule-collision-analyser-1.0.0.jar \\
                        --dslr-dir <path-to-dslr-folder> \\
                        --base-payload <path-to-base-xml> \\
                        --output-dir <path-to-output-folder>
                    """);
        }

        Path dslrDirectory = null;
        Path bddDirectory = null;
        Path outputDirectory = null;

        for (int index = 0; index < args.length; index += 2) {
            String optionName = args[index];
            String optionValue = args[index + 1];

            switch (optionName) {
                case "--dslr-dir" ->
                        dslrDirectory = Path.of(optionValue).toAbsolutePath().normalize();

                case "--bdd-dir" ->
                        bddDirectory = Path.of(optionValue).toAbsolutePath().normalize();

                case "--output-dir" ->
                        outputDirectory = Path.of(optionValue).toAbsolutePath().normalize();

                default ->
                        throw new IllegalArgumentException("Unknown option: " + optionName);
            }
        }

        validateDirectory("DSLR directory", dslrDirectory);
        validateFile("Base payload", bddDirectory);

        if (outputDirectory == null) {
            throw new IllegalArgumentException("Missing --output-dir");
        }

        return new CommandLineOptions(dslrDirectory, bddDirectory, outputDirectory);
    }

    public Path dslrDirectory() {
        return dslrDirectory;
    }

    public Path bddDirectory() {
        return bddDirectory;
    }

    public Path outputDirectory() {
        return outputDirectory;
    }

    private static void validateDirectory(String label, Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Missing " + label);
        }

        if (!Files.exists(path)) {
            throw new IllegalArgumentException(label + " does not exist: " + path);
        }

        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException(label + " is not a directory: " + path);
        }
    }

    private static void validateFile(String label, Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Missing " + label);
        }

        if (!Files.exists(path)) {
            throw new IllegalArgumentException(label + " does not exist: " + path);
        }

        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException(label + " is not a file: " + path);
        }
    }
}