package no.fint.personnel.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.personnel.Properties;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

@Repository
public class ArbeidsforholdRepository extends FileRepository<ArbeidsforholdResource> {
    final ObjectMapper objectMapper;

    public ArbeidsforholdRepository(Properties properties, ObjectMapper objectMapper) throws IOException {
        super(properties.getRepository().resolve("arbeidsforhold"), objectMapper.readerFor(ArbeidsforholdResource.class), objectMapper.writerFor(ArbeidsforholdResource.class));
        this.objectMapper = objectMapper;
    }

    public void store(String orgId, Collection<?> items) {
        super.store(orgId, items.stream()
                .map(it -> objectMapper.convertValue(it, ArbeidsforholdResource.class))
                .map(it -> new Identifiable<>(it, ArbeidsforholdResource::getSystemId)));
    }

    public Stream<ArbeidsforholdResource> load(String orgId) {
        return super.load(orgId).map(ArbeidsforholdResource.class::cast);
    }

    @Override
    public boolean remove(String orgId, Collection<?> items) {
        return super.remove(orgId, items.stream()
                .map(it -> objectMapper.convertValue(it, ArbeidsforholdResource.class))
                .map(it -> new Identifiable<>(it, ArbeidsforholdResource::getSystemId)));
    }
}
