import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class DslrFolderScanner {

    public List<Path> findDslrFiles(Path dslrDirectory) {
        try (Stream<Path> paths = Files.walk(dslrDirectory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".dslr"))
                    .sorted()
                    .toList();
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to scan DSLR directory: " + dslrDirectory, exception);
        }
    }
}