package uk.gov.hmrc.cars.importer.writer;

import org.junit.jupiter.api.Test;
import uk.gov.hmrc.cars.importer.ReferenceDataWriterConfiguration;
import uk.gov.hmrc.cars.importer.model.CodeLists;

@SpringBootTest(
        classes = ReferenceDataWriterConfiguration.class
)
class ReferenceDataYamlWriterWiringTest {

    @Autowired
    private FileDataWriter<CodeLists> writer;

    @Test
    void shouldWireYamlWriterByDefault() {
        assertThat(writer)
                .isInstanceOf(ReferenceDataYamlWriter.class);
    }
}