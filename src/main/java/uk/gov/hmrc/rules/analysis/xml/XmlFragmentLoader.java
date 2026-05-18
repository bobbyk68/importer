package uk.gov.hmrc.rules.analysis.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class XmlFragmentLoader {

    private final Path fragmentsDirectory;

    public XmlFragmentLoader(Path fragmentsDirectory) {
        this.fragmentsDirectory = fragmentsDirectory;
    }

    public String load(XmlFragmentDefinition fragmentDefinition) {
        Path fragmentFile = fragmentsDirectory.resolve(fragmentDefinition.fileName());

        try {
            return Files.readString(fragmentFile);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to load XML fragment: " + fragmentFile,
                    exception
            );
        }
    }
}