package uk.gov.hmrc.rules.analysis.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import uk.gov.hmrc.rules.analysis.ConditionOperator;
import uk.gov.hmrc.rules.analysis.RuleCondition;
import uk.gov.hmrc.rules.analysis.RuleDefinition;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class XmlPayloadUpdater {

    private final XmlFieldMappingRegistry mappingRegistry;

    public XmlPayloadUpdater(XmlFieldMappingRegistry mappingRegistry) {
        this.mappingRegistry = mappingRegistry;
    }

    public XmlUpdateResult updatePayload(
            String baseXml,
            RuleDefinition ruleDefinition
    ) {
        Document document = parseXml(baseXml);
        List<UnmappedCondition> unmappedConditions = new ArrayList<>();

        for (RuleCondition condition : ruleDefinition.conditions()) {
            if (condition.operator() != ConditionOperator.EQUALS) {
                continue;
            }

            String value = condition.values().stream()
                    .findFirst()
                    .orElse(null);

            if (value == null) {
                continue;
            }

            Optional<String> xmlPath = mappingRegistry.findXmlPath(condition.fieldPath());

            if (xmlPath.isEmpty()) {
                unmappedConditions.add(new UnmappedCondition(
                        ruleDefinition.ruleName(),
                        condition.fieldPath(),
                        condition.operator(),
                        value,
                        condition.originalLine()
                ));
                continue;
            }

            applyValue(document, xmlPath.get(), value);
        }

        return new XmlUpdateResult(
                ruleDefinition.ruleName(),
                toXml(document),
                unmappedConditions
        );
    }

    private Document parseXml(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);

            return factory.newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));

        } catch (Exception exception) {
            throw new IllegalStateException("Failed to parse base XML payload", exception);
        }
    }

    private void applyValue(
            Document document,
            String xmlPath,
            String value
    ) {
        String[] parts = xmlPath.split("/");

        Element current = document.getDocumentElement();

        for (String part : parts) {
            Element child = findDirectChild(current, part);

            if (child == null) {
                child = document.createElement(part);
                current.appendChild(child);
            }

            current = child;
        }

        current.setTextContent(value);
    }

    private Element findDirectChild(
            Element parent,
            String localName
    ) {
        NodeList children = parent.getChildNodes();

        for (int index = 0; index < children.getLength(); index++) {
            Node node = children.item(index);

            if (node instanceof Element element
                    && element.getTagName().equals(localName)) {
                return element;
            }
        }

        return null;
    }

    private String toXml(Document document) {
        try {
            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            StringWriter writer = new StringWriter();

            transformer.transform(
                    new DOMSource(document),
                    new StreamResult(writer)
            );

            return writer.toString();

        } catch (Exception exception) {
            throw new IllegalStateException("Failed to convert XML document to string", exception);
        }
    }
}