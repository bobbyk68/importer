package uk.gov.hmrc.rules.analysis;

import uk.gov.hmrc.rules.analysis.bdd.BddRuleReference;
import uk.gov.hmrc.rules.analysis.bdd.FeatureRuleReferenceScanner;
import uk.gov.hmrc.rules.analysis.cli.CommandLineOptions;
import uk.gov.hmrc.rules.analysis.dslr.DslrFolderScanner;
import uk.gov.hmrc.rules.analysis.dslr.DslrRuleExtractor;
import uk.gov.hmrc.rules.analysis.dslr.ParsedDslrRule;
import uk.gov.hmrc.rules.analysis.report.RuleCoverageReportWriter;
import uk.gov.hmrc.rules.analysis.report.RuleCoverageResult;
import uk.gov.hmrc.rules.analysis.report.RuleSetSummaryPrinter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RuleCollisionAnalyserApplication {

    public static void main(String[] args) {
        CommandLineOptions options = CommandLineOptions.from(args);

        DslrFolderScanner folderScanner = new DslrFolderScanner();
        DslrRuleExtractor ruleExtractor = new DslrRuleExtractor();

        List<Path> dslrFiles = folderScanner.findDslrFiles(options.dslrDirectory());

        System.out.println("DSLR directory:");
        System.out.println("  " + options.dslrDirectory());
        System.out.println();

        System.out.println("Base XML payload:");
        System.out.println("  " + options.bddDirectory());
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

        RuleSetSummaryPrinter ruleSetSummaryPrinter =
                new RuleSetSummaryPrinter();

        ruleSetSummaryPrinter.print(allRules);

        FeatureRuleReferenceScanner featureScanner =
                new FeatureRuleReferenceScanner();

        Set<BddRuleReference> bddReferences =
                featureScanner.scan(options.bddDirectory());

        Map<String, List<String>> referencesByRuleName =
                bddReferences.stream()
                        .collect(Collectors.groupingBy(
                                BddRuleReference::ruleName,
                                Collectors.mapping(
                                        reference -> reference.sourceFile().toString(),
                                        Collectors.toList()
                                )
                        ));

        List<RuleCoverageResult> coverageResults =
                allRules.stream()
                        .map(rule -> {
                            List<String> references =
                                    referencesByRuleName.getOrDefault(
                                            rule.ruleName(),
                                            List.of()
                                    );

                            return new RuleCoverageResult(
                                    rule.ruleName(),
                                    rule.sourceFile(),
                                    !references.isEmpty(),
                                    references
                            );
                        })
                        .toList();

        new RuleCoverageReportWriter().write(
                options.outputDirectory(),
                coverageResults
        );

    }
}