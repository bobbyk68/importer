package uk.gov.hmrc.rules.analysis;

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

public class RuleCollisionAnalyserApplication {

    public static void main(String[] args) {
        CommandLineOptions options = CommandLineOptions.from(args);

        DslrFolderScanner folderScanner = new DslrFolderScanner();
        DslrRuleExtractor ruleExtractor = new DslrRuleExtractor();
        FeatureRuleReferenceScanner featureScanner = new FeatureRuleReferenceScanner();
        RuleSetSummaryPrinter ruleSetSummaryPrinter = new RuleSetSummaryPrinter();
        RuleCoverageReportWriter coverageReportWriter = new RuleCoverageReportWriter();

        List<Path> dslrFiles = folderScanner.findDslrFiles(options.dslrDirectory());

        System.out.println("DSLR directory:");
        System.out.println("  " + options.dslrDirectory());
        System.out.println();

        System.out.println("BDD directory:");
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

        ruleSetSummaryPrinter.print(allRules);

        List<RuleCoverageResult> coverageResults = allRules.stream()
                .map(rule -> featureScanner.findCoverage(
                        rule,
                        options.bddDirectory()
                ))
                .toList();

        coverageReportWriter.write(
                options.outputDirectory(),
                coverageResults
        );
    }
}