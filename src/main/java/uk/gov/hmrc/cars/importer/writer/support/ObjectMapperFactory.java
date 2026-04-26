package uk.gov.hmrc.cars.importer.writer.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class ObjectMapperFactory {

    private ObjectMapperFactory() {
    }

    public static ObjectMapper jsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        configureCommon(mapper);
        return mapper;
    }

    public static ObjectMapper yamlMapper() {
        YAMLFactory yamlFactory = YAMLFactory.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build();

        ObjectMapper mapper = new ObjectMapper(yamlFactory);
        configureCommon(mapper);
        return mapper;
    }

    private static void configureCommon(ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
    }
}
