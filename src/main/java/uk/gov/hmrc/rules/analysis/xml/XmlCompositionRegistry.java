package uk.gov.hmrc.rules.analysis.xml;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class XmlCompositionRegistry {

    private final Map<String, XmlFragmentDefinition> fragments = Map.of(
            "goodsItem", new XmlFragmentDefinition(
                    "goodsItem",
                    "goods-item-core.xml",
                    "declaration",
                    "goodsItem"
            ),

            "previousDocument", new XmlFragmentDefinition(
                    "previousDocument",
                    "previous-document-core.xml",
                    "declaration/goodsItem",
                    "previousDocument"
            ),

            "additionalDocument", new XmlFragmentDefinition(
                    "additionalDocument",
                    "additional-document-core.xml",
                    "declaration",
                    "additionalDocument"
            ),

            "additionalInformation", new XmlFragmentDefinition(
                    "additionalInformation",
                    "additional-information-core.xml",
                    "declaration",
                    "additionalInformation"
            )
    );

    private final Map<String, RuleXmlMapping> mappings = Map.of(
            "declaration.typeCode", new RuleXmlMapping(
                    "declaration.typeCode",
                    List.of(),
                    "declaration/type"
            ),

            "procedureCategory", new RuleXmlMapping(
                    "procedureCategory",
                    List.of(),
                    "declaration/procedureCategory"
            ),

            "requestedProcedure.code", new RuleXmlMapping(
                    "requestedProcedure.code",
                    List.of(),
                    "declaration/requestedProcedure"
            ),

            "previousDocument.typeCode", new RuleXmlMapping(
                    "previousDocument.typeCode",
                    List.of("goodsItem", "previousDocument"),
                    "declaration/goodsItem/previousDocument/type"
            ),

            "additionalDocument.typeCode", new RuleXmlMapping(
                    "additionalDocument.typeCode",
                    List.of("additionalDocument"),
                    "declaration/additionalDocument/type"
            ),

            "additionalInformation.code", new RuleXmlMapping(
                    "additionalInformation.code",
                    List.of("additionalInformation"),
                    "declaration/additionalInformation/code"
            )
    );

    public Optional<RuleXmlMapping> findMapping(String conditionPath) {
        return Optional.ofNullable(mappings.get(conditionPath));
    }

    public XmlFragmentDefinition fragment(String name) {
        XmlFragmentDefinition fragment = fragments.get(name);

        if (fragment == null) {
            throw new IllegalArgumentException("Unknown XML fragment: " + name);
        }

        return fragment;
    }
}