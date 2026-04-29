package uk.gov.hmrc.cars.importer;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "writer.reference-data")
public class ReferenceDataWriterProperties {

    private boolean jsonEnabled = false;
    private Path outputPath = Path.of("target/reference-data.yml");

    public boolean isJsonEnabled() {
        return jsonEnabled;
    }

    public void setJsonEnabled(boolean jsonEnabled) {
        this.jsonEnabled = jsonEnabled;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(Path outputPath) {
        this.outputPath = outputPath;
    }
}