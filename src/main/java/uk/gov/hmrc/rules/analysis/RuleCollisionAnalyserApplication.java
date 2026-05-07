package uk.gov.hmrc.rules.analysis;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RuleCollisionAnalyserApplication {

    public static void main(String[] args) {
        CommandLineOptions options = CommandLineOptions.from(args);

        DslrFolderScanner folderScanner = new DslrFolderScanner();
        DslrRuleExtractor ruleExtractor = new DslrRuleExtractor();
        RuleConditionExtractor conditionExtractor = new RuleConditionExtractor();

        List<Path> dslrFiles = folderScanner.findDslrFiles(options.dslrDirectory());

        System.out.println("DSLR directory:");
        System.out.println("  " + options.dslrDirectory());
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

        for (RuleDefinition definition : ruleDefinitions) {
            System.out.println("==================================================");
            System.out.println("RULE: " + definition.ruleName());
            System.out.println("FILE: " + definition.sourceFile());
            System.out.println();

            for (RuleCondition condition : definition.conditions()) {
                System.out.println("Original line:");
                System.out.println("  " + condition.originalLine());

                System.out.println("Parsed:");
                System.out.println("  fieldPath = " + condition.fieldPath());
                System.out.println("  operator  = " + condition.operator());
                System.out.println("  values    = " + condition.values());

                System.out.println();
            }
        }
    }
}