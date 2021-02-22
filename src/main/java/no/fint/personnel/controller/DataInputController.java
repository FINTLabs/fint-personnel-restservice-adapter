package no.fint.personnel.controller;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import no.fint.datainput.Input;
import no.fint.personnel.model.FileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("input/{datatype}")
@Slf4j
public class DataInputController {

    private final ImmutableMap<String, FileRepository> repositories;

    public DataInputController(Collection<FileRepository> repositories) {
        final ImmutableMap.Builder<String, FileRepository> b = ImmutableMap.builder();
        repositories.forEach(r -> b.put(r.getName(), r));
        this.repositories = b.build();
    }

    @PostMapping
    public ResponseEntity fullData(
            @PathVariable String datatype,
            @RequestBody Input input
    ) {
        log.info("Full update, {} {} {} {}, {} items", input.getOrgId(), datatype, input.getTimestamp(), input.getCorrId(), input.getData().size());
        final FileRepository repository = repositories.get(datatype);
        if (repository == null) {
            return ResponseEntity.notFound().build();
        }
        repository.clear(input.getOrgId());
        repository.store(input.getOrgId(), input.getData());
        return ResponseEntity.accepted().build();
    }

    @PatchMapping
    public ResponseEntity incrementalData(
            @PathVariable String datatype,
            @RequestBody Input input
    ) {
        log.info("Incremental update, {} {} {} {}, {} items", input.getOrgId(), datatype, input.getTimestamp(), input.getCorrId(), input.getData().size());
        final FileRepository repository = repositories.get(datatype);
        if (repository == null) {
            return ResponseEntity.notFound().build();
        }
        repository.store(input.getOrgId(), input.getData());
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping
    public ResponseEntity deleteData(
            @PathVariable String datatype,
            @RequestBody Input input
    ) {
        log.info("Deletion, {} {} {} {}, {} items", input.getOrgId(), datatype, input.getTimestamp(), input.getCorrId(), input.getData().size());
        final FileRepository repository = repositories.get(datatype);
        if (repository == null) {
            return ResponseEntity.notFound().build();
        }
        if (repository.remove(input.getOrgId(), input.getData())) {
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.unprocessableEntity().build();
    }

}
