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
src/main/java
└── uk/gov/hmrc/cars/importer
    ├── model
    │   ├── CodeList.java
    │   ├── CodeLists.java
    │   └── Value.java
    └── writer
        ├── ReferenceDataWriter.java
        ├── json
        │   └── JsonReferenceDataWriter.java
        ├── mapper
        │   └── CodeListsExportMapper.java
        ├── model
        │   ├── CodeListExport.java
        │   ├── CodeListsExport.java
        │   └── ValueExport.java
        ├── support
        │   └── ObjectMapperFactory.java
        └── yaml
            └── YamlReferenceDataWriter.java
```
