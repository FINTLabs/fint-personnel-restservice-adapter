package no.fint.personnel.model;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.Streams;
import no.fint.model.FintMainObject;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.Unchecked;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class FileRepository<T extends FintMainObject> {
    private final Path location;
    private final ObjectReader reader;
    private final ObjectWriter writer;

    public String getName() {
        return location.getName(location.getNameCount() - 1).toString();
    }

    protected FileRepository(Path location, ObjectReader reader, ObjectWriter writer) throws IOException {
        if (!Files.isDirectory(location)) {
            this.location = Files.createDirectories(location);
        } else {
            this.location = location;
        }
        this.reader = reader;
        this.writer = writer;
    }

    protected Consumer<BufferedWriter> writerConsumer(T value) {
        return bufferedWriter -> {
            try {
                writer.writeValue(bufferedWriter, value);
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public void clear() {
        try {
            Files.find(location, 2, (p, a) -> a.isRegularFile())
                    .forEach(Unchecked.consumer(Files::delete));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void store(Collection<?> items);

    protected void store(Stream<Identifiable<T>> items) {
        items.forEach(it ->
                Streams.mapWithIndex(it.identifiers()
                                .filter(Objects::nonNull)
                                .map(Identifikator::getIdentifikatorverdi)
                                .filter(StringUtils::isNotBlank),
                        (id, index) -> location.resolve(index + "_" + id + ".json"))
                        .map(Unchecked.function(Files::newBufferedWriter))
                        .forEach(writerConsumer(it.value())));
    }

    protected Stream<T> load() {
        try {
            return Files.find(location, 2, (p, a) -> a.isRegularFile())
                    .map(Unchecked.function(Files::newBufferedReader))
                    .map(Unchecked.function(reader::readValue));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void remove(Collection<?> data);

    protected void remove(Stream<Identifiable<T>> items) {
        items.forEach(it ->
                Streams.mapWithIndex(it.identifiers()
                                .filter(Objects::nonNull)
                                .map(Identifikator::getIdentifikatorverdi)
                                .filter(StringUtils::isNotBlank),
                        (id, index) -> location.resolve(index + "_" + id + ".json"))
                        .map(Unchecked.function(Files::deleteIfExists)));
    }
}
