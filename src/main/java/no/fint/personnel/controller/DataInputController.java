package no.fint.personnel.controller;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import no.fint.personnel.model.FileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("input")
@Slf4j
public class DataInputController {

    private final ImmutableMap<String, FileRepository> repositories;

    public DataInputController(Collection<FileRepository> repositories) {
        final ImmutableMap.Builder<String, FileRepository> b = ImmutableMap.builder();
        repositories.forEach(r -> b.put(r.getName(), r));
        this.repositories = b.build();
    }

    @PostMapping("full")
    public ResponseEntity postFullData(@RequestBody DataInput dataInput) {
        log.info("Full update, {} {} {} {}, {} items", dataInput.getOrgId(), dataInput.getDatatype(), dataInput.getTimestamp(), dataInput.getCorrId(), dataInput.getData().size());
        final FileRepository repository = repositories.get(dataInput.getDatatype());
        if (repository == null) {
            return ResponseEntity.notFound().build();
        }
        repository.store(dataInput.getData());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("incremental")
    public ResponseEntity postIncrementalData(@RequestBody DataInput dataInput) {
        log.info("Incremental update, {} {} {} {}, {} items", dataInput.getOrgId(), dataInput.getDatatype(), dataInput.getTimestamp(), dataInput.getCorrId(), dataInput.getData().size());
        final FileRepository repository = repositories.get(dataInput.getDatatype());
        if (repository == null) {
            return ResponseEntity.notFound().build();
        }
        repository.store(dataInput.getData());
        return ResponseEntity.accepted().build();
    }

}
