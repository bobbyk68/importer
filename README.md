# cars-importer-writer

Java 17 Maven example showing a clean writer design:

- `ReferenceDataWriter` interface
- `YamlReferenceDataWriter`
- `JsonReferenceDataWriter`
- `CodeListsExportMapper`
- export-only DTO records
- JUnit 5 tests for YAML and JSON output

## Run tests

```bash
mvn test
```

## Package structure

```text
rule-collision-analyser
├── pom.xml
└── src
    └── main
        └── java
            └── uk
                └── gov
                    └── hmrc
                        └── rules
                            └── analysis
                                ├── RuleCollisionAnalyserApplication.java
                                ├── cli
                                │   └── CommandLineOptions.java
                                └── dslr
                                    ├── DslrFolderScanner.java
                                    ├── DslrRuleExtractor.java
                                    └── ParsedDslrRule.java
```
java -jar rule-collision-analyser-1.0.0.jar \
--dslr-dir /path/to/your/dslr/folder

java -jar target/rule-collision-analyser-1.0.0.jar \
--dslr-dir src/main/resources/rules/br455

java -jar target/rule-collision-analyser-1.0.0.jar \
--dslr-dir src/main/resources/rules/br455