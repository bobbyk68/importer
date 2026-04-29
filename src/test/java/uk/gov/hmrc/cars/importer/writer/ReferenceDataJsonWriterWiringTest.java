package uk.gov.hmrc.cars.importer.writer;

import org.junit.jupiter.api.Test;
import uk.gov.hmrc.cars.importer.ReferenceDataWriterConfiguration;
import uk.gov.hmrc.cars.importer.model.CodeLists;

@SpringBootTest(
        classes = ReferenceDataWriterConfiguration.class,
        properties = {
                "writer.reference-data.json-enabled=true"
        }
)
class ReferenceDataJsonWriterWiringTest {

    @Autowired
    private FileDataWriter<CodeLists> writer;

    @Test
    void shouldWireJsonWriterWhenJsonEnabled() {
        assertThat(writer)
                .isInstanceOf(ReferenceDataJsonWriter.class);
    }
}