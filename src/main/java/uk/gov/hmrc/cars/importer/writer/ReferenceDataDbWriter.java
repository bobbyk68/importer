package uk.gov.hmrc.cars.importer.writer;

import uk.gov.hmrc.cars.importer.model.CodeLists;
public class ReferenceDataDbWriter implements DataWriter<CodeLists> {

    @Override
    public void write(CodeLists data) {
        repository.save(data);
    }
}