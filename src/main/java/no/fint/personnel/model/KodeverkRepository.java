package no.fint.personnel.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.FintMainObject;
import no.fint.model.felles.basisklasser.Begrep;
import no.fint.model.resource.FintLinks;
import no.fint.personnel.service.ValidationService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

public abstract class KodeverkRepository<T extends Begrep & FintLinks & FintMainObject> extends FileRepository<T> {
    private final ObjectMapper objectMapper;
    private final Class<T> type;

    protected KodeverkRepository(Path location, ObjectMapper objectMapper, ValidationService validationService, Class<T> type) throws IOException {
        super(location, objectMapper.readerFor(type), objectMapper.writerFor(type), validationService);
        this.objectMapper = objectMapper;
        this.type = type;
    }

    public void store(String orgId, Collection<?> items) {
        super.store(orgId, items.stream()
                .map(it -> objectMapper.convertValue(it, type))
                .map(it -> new Identifiable<>(it, Begrep::getSystemId)));
    }

    public Stream<T> load(String orgId) {
        return super.load(orgId).map(type::cast);
    }

    @Override
    public boolean remove(String orgId, Collection<?> items) {
        return super.remove(orgId, items.stream()
                .map(it -> objectMapper.convertValue(it, type))
                .map(it -> new Identifiable<>(it, Begrep::getSystemId)));
    }

}
