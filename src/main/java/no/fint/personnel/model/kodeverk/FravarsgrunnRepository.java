package no.fint.personnel.model.kodeverk;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.administrasjon.kodeverk.FravarsgrunnResource;
import no.fint.personnel.Properties;
import no.fint.personnel.model.KodeverkRepository;
import no.fint.personnel.service.ValidationService;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class FravarsgrunnRepository extends KodeverkRepository<FravarsgrunnResource> {

    public FravarsgrunnRepository(Properties properties, ObjectMapper objectMapper, ValidationService validationService) throws IOException {
        super(properties.getRepository().resolve("fravarsgrunn"), objectMapper, validationService, FravarsgrunnResource.class);
    }

}
