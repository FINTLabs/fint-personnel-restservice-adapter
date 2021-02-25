package no.fint.personnel.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.personnel.Properties;
import no.fint.personnel.service.ValidationService;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

@Repository
public class PersonalressursRepository extends FileRepository<PersonalressursResource> {
    final ObjectMapper objectMapper;

    public PersonalressursRepository(Properties properties, ObjectMapper objectMapper, ValidationService validationService) throws IOException {
        super(properties.getRepository().resolve("personalressurs"), objectMapper.readerFor(PersonalressursResource.class), objectMapper.writerFor(PersonalressursResource.class), validationService);
        this.objectMapper = objectMapper;
    }

    public void store(String orgId, Collection<?> items) {
        super.store(orgId, items.stream()
                .map(it -> objectMapper.convertValue(it, PersonalressursResource.class))
                .map(it -> new Identifiable<>(it, PersonalressursResource::getSystemId, PersonalressursResource::getAnsattnummer, PersonalressursResource::getBrukernavn)));
    }

    public Stream<PersonalressursResource> load(String orgId) {
        return super.load(orgId).map(PersonalressursResource.class::cast);
    }

    @Override
    public boolean remove(String orgId, Collection<?> items) {
        return super.remove(orgId, items.stream()
                .map(it -> objectMapper.convertValue(it, PersonalressursResource.class))
                .map(it -> new Identifiable<>(it, PersonalressursResource::getSystemId, PersonalressursResource::getAnsattnummer, PersonalressursResource::getBrukernavn)));
    }
}
