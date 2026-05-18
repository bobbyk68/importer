package uk.gov.hmrc.rules.analysis.xml;

public record XmlFragmentDefinition(
        String name,
        String fileName,
        String parentPath,
        String rootElementName
) {
}