package uk.gov.hmrc.cars.importer.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.hmrc.cars.importer.ReferenceDataWriterProperties;
import uk.gov.hmrc.cars.importer.model.CodeLists;
import uk.gov.hmrc.cars.importer.writer.FileDataWriter;
import uk.gov.hmrc.cars.importer.writer.ReferenceDataYamlWriter;
import uk.gov.hmrc.cars.importer.writer.support.ObjectMapperFactory;

@Configuration
@EnableConfigurationProperties(ReferenceDataWriterProperties.class)
public class ReferenceDataWriterConfiguration {

    @Bean
    public ReferenceDataExportMapper referenceDataExportMapper() {
        return new ReferenceDataExportMapper();
    }

    @Bean
    public ObjectMapper yamlMapper() {
        return ObjectMapperFactory.yamlMapper();
    }

    @Bean
    public ObjectMapper jsonMapper() {
        return ObjectMapperFactory.jsonMapper();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "writer.reference-data",
            name = "json-enabled",
            havingValue = "true"
    )
    public FileDataWriter<CodeLists> referenceDataJsonWriter(
            ObjectMapper jsonMapper,
            ReferenceDataExportMapper mapper
    ) {
        return new ReferenceDataJsonWriter(jsonMapper, mapper);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "writer.reference-data",
            name = "json-enabled",
            havingValue = "false",
            matchIfMissing = true
    )
    public FileDataWriter<CodeLists> referenceDataYamlWriter(
            ObjectMapper yamlMapper,
            ReferenceDataExportMapper mapper
    ) {
        return new ReferenceDataYamlWriter(yamlMapper, mapper);
    }
}