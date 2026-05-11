package uk.gov.hmrc.rules.analysis.xml;

import java.util.Map;
import java.util.Optional;

public class XmlFieldMappingRegistry {

    private final Map<String, String> mappings = Map.of(
            "declaration.typeCode", "declaration/type",
            "procedureCategory", "declaration/procedureCategory",
            "requestedProcedure.code", "declaration/requestedProcedure",
            "previousDocument.typeCode", "declaration/consignmentShipment/previousDocuments/type",
            "additionalDocument.typeCode", "declaration/additionalDocuments/type",
            "additionalInformation.code", "declaration/additionalInformation/code",
            "additionalInformation.text", "declaration/additionalInformation/text"
    );

    public Optional<String> findXmlPath(String conditionPath) {
        return Optional.ofNullable(mappings.get(conditionPath));
    }
}