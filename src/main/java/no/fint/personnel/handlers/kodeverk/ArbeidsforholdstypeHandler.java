package no.fint.personnel.handlers.kodeverk;

import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.model.administrasjon.kodeverk.KodeverkActions;
import no.fint.model.resource.FintLinks;
import no.fint.personnel.model.kodeverk.ArbeidsforholdstypeRepository;
import no.fint.personnel.service.Handler;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class ArbeidsforholdstypeHandler implements Handler {
    private final ArbeidsforholdstypeRepository repository;

    public ArbeidsforholdstypeHandler(ArbeidsforholdstypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void accept(Event<FintLinks> fintLinksEvent) {
        repository.load(fintLinksEvent.getOrgId()).forEach(fintLinksEvent::addData);
        fintLinksEvent.setResponseStatus(ResponseStatus.ACCEPTED);
    }

    @Override
    public Set<String> actions() {
        return Collections.singleton(KodeverkActions.GET_ALL_ARBEIDSFORHOLDSTYPE.name());
    }
}
