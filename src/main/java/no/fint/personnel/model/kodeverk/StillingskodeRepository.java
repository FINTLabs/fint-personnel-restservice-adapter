package no.fint.personnel.model.kodeverk;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import no.fint.personnel.Properties;
import no.fint.personnel.model.KodeverkRepository;
import no.fint.personnel.service.ValidationService;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class StillingskodeRepository extends KodeverkRepository<StillingskodeResource> {

    public StillingskodeRepository(Properties properties, ObjectMapper objectMapper, ValidationService validationService) throws IOException {
        super(properties.getRepository().resolve("stillingskode"), objectMapper, validationService, StillingskodeResource.class);
    }

}
