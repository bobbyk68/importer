import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RuleCollisionAnalyserApplication {

    public static void main(String[] args) {
        CommandLineOptions options = CommandLineOptions.from(args);

        DslrFolderScanner folderScanner = new DslrFolderScanner();
        DslrRuleExtractor ruleExtractor = new DslrRuleExtractor();

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

        for (ParsedDslrRule rule : allRules) {
            System.out.println("--------------------------------------------------");
            System.out.println("Rule: " + rule.ruleName());
            System.out.println("File: " + rule.sourceFile());
            System.out.println("When lines: " + rule.whenLines().size());
            System.out.println("Then lines: " + rule.thenLines().size());

            System.out.println();
            System.out.println("WHEN:");
            rule.whenLines().forEach(line -> System.out.println("  " + line));

            System.out.println();
            System.out.println("THEN:");
            rule.thenLines().forEach(line -> System.out.println("  " + line));
            System.out.println();
        }
    }
}