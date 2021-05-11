#!/bin/bash

rm -rf src/main/java/no/fint/personnel/{model,handlers}/kodeverk
mkdir src/main/java/no/fint/personnel/{model,handlers}/kodeverk

while read javatype; do
  cat >"src/main/java/no/fint/personnel/model/kodeverk/${javatype}Repository.java" <<EOF
package no.fint.personnel.model.kodeverk;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.administrasjon.kodeverk.${javatype}Resource;
import no.fint.personnel.Properties;
import no.fint.personnel.model.KodeverkRepository;
import no.fint.personnel.service.ValidationService;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class ${javatype}Repository extends KodeverkRepository<${javatype}Resource> {

    public ${javatype}Repository(Properties properties, ObjectMapper objectMapper, ValidationService validationService) throws IOException {
        super(properties.getRepository().resolve("${javatype,,}"), objectMapper, validationService, ${javatype}Resource.class);
    }

}
EOF

  cat >"src/main/java/no/fint/personnel/handlers/kodeverk/${javatype}Handler.java" <<EOF
package no.fint.personnel.handlers.kodeverk;

import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.model.administrasjon.kodeverk.KodeverkActions;
import no.fint.model.resource.FintLinks;
import no.fint.personnel.model.kodeverk.${javatype}Repository;
import no.fint.personnel.service.Handler;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class ${javatype}Handler implements Handler {
    private final ${javatype}Repository repository;

    public ${javatype}Handler(${javatype}Repository repository) {
        this.repository = repository;
    }

    @Override
    public void accept(Event<FintLinks> fintLinksEvent) {
        repository.load(fintLinksEvent.getOrgId()).forEach(fintLinksEvent::addData);
        fintLinksEvent.setResponseStatus(ResponseStatus.ACCEPTED);
    }

    @Override
    public Set<String> actions() {
        return Collections.singleton(KodeverkActions.GET_ALL_${javatype^^}.name());
    }
}
EOF

done <<EOF
Aktivitet
Anlegg
Ansvar
Arbeidsforholdstype
Art
Diverse
Fravarsgrunn
Fravarstype
Funksjon
Kontrakt
Lonnsart
Lopenummer
Objekt
Personalressurskategori
Prosjekt
Ramme
Stillingskode
Uketimetall
EOF
