package uk.gov.hmrc.rules.analysis;

import uk.gov.hmrc.rules.analysis.xml.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RuleCollisionAnalyserApplication {

    public static void main(String[] args) {
        CommandLineOptions options = CommandLineOptions.from(args);

        DslrFolderScanner folderScanner = new DslrFolderScanner();
        DslrRuleExtractor ruleExtractor = new DslrRuleExtractor();
        RuleConditionExtractor conditionExtractor = new RuleConditionExtractor();

        BaseXmlPayloadLoader baseXmlPayloadLoader = new BaseXmlPayloadLoader();
        GeneratedPayloadWriter generatedPayloadWriter = new GeneratedPayloadWriter();

        List<Path> dslrFiles = folderScanner.findDslrFiles(options.dslrDirectory());

        System.out.println("DSLR directory:");
        System.out.println("  " + options.dslrDirectory());
        System.out.println();

        System.out.println("Base XML payload:");
        System.out.println("  " + options.basePayload());
        System.out.println();

        System.out.println("Output directory:");
        System.out.println("  " + options.outputDirectory());
        System.out.println();

        System.out.println("DSLR files found:");
        System.out.println("  " + dslrFiles.size());
        System.out.println();

        List<ParsedDslrRule> allRules = new ArrayList<>();

        for (Path dslrFile : dslrFiles) {
            List<ParsedDslrRule> rulesFromFile = ruleExtractor.extractRules(dslrFile);
            allRules.addAll(rulesFromFile);

            System.out.println(dslrFile.getFileName() + " -> " + rulesFromFile.size() + " rules");
        }

        System.out.println();
        System.out.println("Total rules extracted:");
        System.out.println("  " + allRules.size());
        System.out.println();

        List<RuleDefinition> ruleDefinitions = allRules.stream()
                .map(conditionExtractor::extract)
                .toList();

        String baseXml = baseXmlPayloadLoader.load(options.basePayload());

        XmlPayloadUpdater xmlPayloadUpdater =
                new XmlPayloadUpdater(new XmlFieldMappingRegistry());

        UnmappedConditionReportWriter unmappedConditionReportWriter =
                new UnmappedConditionReportWriter();

        List<UnmappedCondition> allUnmappedConditions = new ArrayList<>();

        for (RuleDefinition ruleDefinition : ruleDefinitions) {
            XmlUpdateResult updateResult =
                    xmlPayloadUpdater.updatePayload(baseXml, ruleDefinition);

            generatedPayloadWriter.write(
                    options.outputDirectory(),
                    updateResult.ruleName(),
                    updateResult.xml()
            );

            allUnmappedConditions.addAll(updateResult.unmappedConditions());
        }

        unmappedConditionReportWriter.write(
                options.outputDirectory(),
                allUnmappedConditions
        );
    }
}