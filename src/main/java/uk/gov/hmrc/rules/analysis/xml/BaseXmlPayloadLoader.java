package uk.gov.hmrc.rules.analysis.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BaseXmlPayloadLoader {

    public String load(Path basePayloadPath) {
        try {
            return Files.readString(basePayloadPath);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to read base XML payload: " + basePayloadPath,
                    exception
            );
        }
    }
}