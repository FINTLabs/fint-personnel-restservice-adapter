package no.fint.personnel.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.personnel.Properties;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

@Repository
public class PersonalressursRepository extends FileRepository<PersonalressursResource> {
    final ObjectMapper objectMapper;

    public PersonalressursRepository(Properties properties, ObjectMapper objectMapper) throws IOException {
        super(properties.getRepository().resolve("personalressurs"), objectMapper.readerFor(PersonalressursResource.class), objectMapper.writerFor(PersonalressursResource.class));
        this.objectMapper = objectMapper;
    }

    public void store(Collection<?> items) {
        super.store(items.stream()
                .map(it -> objectMapper.convertValue(it, PersonalressursResource.class))
                .map(it -> new Identifiable<>(it, PersonalressursResource::getSystemId, PersonalressursResource::getAnsattnummer, PersonalressursResource::getBrukernavn)));
    }

    public Stream<PersonalressursResource> load() {
        return super.load().map(PersonalressursResource.class::cast);
    }

    @Override
    public void remove(Collection<?> items) {
        super.remove(items.stream()
                .map(it -> objectMapper.convertValue(it, PersonalressursResource.class))
                .map(it -> new Identifiable<>(it, PersonalressursResource::getSystemId, PersonalressursResource::getAnsattnummer, PersonalressursResource::getBrukernavn)));
    }
}
