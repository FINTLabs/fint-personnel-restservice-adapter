package no.fint.personnel.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.personnel.Properties;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

@Repository
public class OrganisasjonselementRepository extends FileRepository<OrganisasjonselementResource> {
    final ObjectMapper objectMapper;

    public OrganisasjonselementRepository(Properties properties, ObjectMapper objectMapper) throws IOException {
        super(properties.getRepository().resolve("organisasjonselement"), objectMapper.readerFor(OrganisasjonselementResource.class), objectMapper.writerFor(OrganisasjonselementResource.class));
        this.objectMapper = objectMapper;
    }

    public void store(String orgId, Collection<?> items) {
        super.store(orgId, items.stream()
                .map(it -> objectMapper.convertValue(it, OrganisasjonselementResource.class))
                .map(it -> new Identifiable<>(it, OrganisasjonselementResource::getOrganisasjonsId, OrganisasjonselementResource::getOrganisasjonsKode, OrganisasjonselementResource::getOrganisasjonsnummer)));
    }

    public Stream<OrganisasjonselementResource> load(String orgId) {
        return super.load(orgId).map(OrganisasjonselementResource.class::cast);
    }

    @Override
    public boolean remove(String orgId, Collection<?> items) {
        return super.remove(orgId, items.stream()
                .map(it -> objectMapper.convertValue(it, OrganisasjonselementResource.class))
                .map(it -> new Identifiable<>(it, OrganisasjonselementResource::getOrganisasjonsId, OrganisasjonselementResource::getOrganisasjonsKode, OrganisasjonselementResource::getOrganisasjonsnummer)));
    }
}
