package uk.gov.hmrc.rules.analysis.report;

import uk.gov.hmrc.rules.analysis.dslr.ParsedDslrRule;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuleSetSummaryPrinter {

    public void print(List<ParsedDslrRule> allRules) {

        Map<String, Long> ruleCountByFile = allRules.stream()
                .collect(Collectors.groupingBy(
                        ParsedDslrRule::sourceFile,
                        Collectors.counting()
                ));

        System.out.println();
        System.out.println("==================================================");
        System.out.println("DSLR RULE SUMMARY");
        System.out.println("==================================================");
        System.out.println();

        ruleCountByFile.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    System.out.printf(
                            "%-35s -> %5d rules%n",
                            entry.getKey(),
                            entry.getValue()
                    );
                });

        System.out.println();
        System.out.println("--------------------------------------------------");

        System.out.printf(
                "%-35s -> %5d%n",
                "TOTAL RULES",
                allRules.size()
        );

        System.out.printf(
                "%-35s -> %5d%n",
                "TOTAL DSLR FILES",
                ruleCountByFile.size()
        );

        System.out.println("==================================================");
        System.out.println();
    }
}