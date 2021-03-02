package no.fint.personnel.model.kodeverk;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.administrasjon.kodeverk.AktivitetResource;
import no.fint.personnel.Properties;
import no.fint.personnel.model.KodeverkRepository;
import no.fint.personnel.service.ValidationService;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class AktivitetRepository extends KodeverkRepository<AktivitetResource> {

    public AktivitetRepository(Properties properties, ObjectMapper objectMapper, ValidationService validationService) throws IOException {
        super(properties.getRepository().resolve("aktivitet"), objectMapper, validationService, AktivitetResource.class);
    }

}
