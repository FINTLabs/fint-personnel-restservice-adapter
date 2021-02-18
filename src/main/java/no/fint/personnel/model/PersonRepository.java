package no.fint.personnel.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.felles.PersonResource;
import no.fint.personnel.Properties;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

@Repository
public class PersonRepository extends FileRepository<PersonResource> {
    final ObjectMapper objectMapper;
    public PersonRepository(Properties properties, ObjectMapper objectMapper) throws IOException {
        super(properties.getRepository().resolve("person"), objectMapper.readerFor(PersonResource.class), objectMapper.writerFor(PersonResource.class));
        this.objectMapper = objectMapper;
    }

    public void store(Collection<?> items) {
        super.store(items.stream()
                .map(it -> objectMapper.convertValue(it, PersonResource.class))
                .map(it -> new Identifiable<>(it, PersonResource::getFodselsnummer)));
    }

    public Stream<PersonResource> load() {
        return super.load().map(PersonResource.class::cast);
    }
}
