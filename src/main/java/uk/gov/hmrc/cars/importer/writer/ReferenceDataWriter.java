package uk.gov.hmrc.cars.importer.writer;

import uk.gov.hmrc.cars.importer.model.CodeLists;

public interface ReferenceDataWriter {

    void write(CodeLists codeLists);
}
