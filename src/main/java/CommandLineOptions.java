import java.nio.file.Files;
import java.nio.file.Path;

public class CommandLineOptions {

    private final Path dslrDirectory;

    private CommandLineOptions(Path dslrDirectory) {
        this.dslrDirectory = dslrDirectory;
    }

    public static CommandLineOptions from(String[] args) {
        if (args.length != 2 || !"--dslr-dir".equals(args[0])) {
            throw new IllegalArgumentException("""
                    Usage:
                      java -jar rule-collision-analyser-1.0.0.jar --dslr-dir <path-to-dslr-folder>
                    """);
        }

        Path dslrDirectory = Path.of(args[1]).toAbsolutePath().normalize();

        if (!Files.exists(dslrDirectory)) {
            throw new IllegalArgumentException("DSLR directory does not exist: " + dslrDirectory);
        }

        if (!Files.isDirectory(dslrDirectory)) {
            throw new IllegalArgumentException("Path is not a directory: " + dslrDirectory);
        }

        return new CommandLineOptions(dslrDirectory);
    }

    public Path dslrDirectory() {
        return dslrDirectory;
    }
}