package no.fint.personnel.model;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import no.fint.model.FintMainObject;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.Unchecked;
import org.jooq.lambda.tuple.Tuple;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Stream;

public abstract class FileRepository<T extends FintMainObject> {
    private final Path location;
    private final ObjectReader reader;
    private final ObjectWriter writer;
    private final HashFunction hashFunction;

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
        hashFunction = Hashing.goodFastHash(32);
    }

    public void clear(String orgId) {
        try {
            Files.find(getRootDir(orgId), 2, (p, a) -> a.isRegularFile())
                    .forEach(Unchecked.consumer(Files::delete));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getRootDir(String orgId) {
        try {
            final Path rootDir = location.resolve(orgId);
            if (!Files.isDirectory(rootDir)) {
                return Files.createDirectories(rootDir);
            }
            return rootDir;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void store(String orgId, Collection<?> items);

    protected void store(String orgId, Stream<Identifiable<T>> items) {
        final Path path = getRootDir(orgId);
        items.map(Identifiable::tuple)
                .map(t -> t.map1(this::getFileName))
                .map(t -> t.map1(path::resolve))
                .peek(t -> System.out.println(t.v1))
                .map(t -> t.map1(Path::toFile))
                .forEach(Tuple.consumer(Unchecked.biConsumer(writer::writeValue)));
    }

    private String getFileName(Stream<Identifikator> identifiers) {
        return identifiers
                .filter(Objects::nonNull)
                .map(Identifikator::getIdentifikatorverdi)
                .filter(StringUtils::isNotBlank)
                .sorted()
                .collect(Collector.of(
                        hashFunction::newHasher,
                        (h, v) -> h.putUnencodedChars(v),
                        (h1, h2) -> {
                            throw new IllegalStateException();
                        },
                        h -> h.hash().toString() + ".json"));
    }

    protected Stream<T> load(String orgId) {
        try {
            return Files.find(getRootDir(orgId), 2, (p, a) -> a.isRegularFile())
                    .map(Unchecked.function(Files::newBufferedReader))
                    .map(Unchecked.function(reader::readValue));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract boolean remove(String orgId, Collection<?> data);

    protected boolean remove(String orgId, Stream<Identifiable<T>> items) {
        final Path path = getRootDir(orgId);
        return items.map(Identifiable::identifiers)
                .map(this::getFileName)
                .map(path::resolve)
                .peek(System.out::println)
                .allMatch(Unchecked.predicate(Files::deleteIfExists));
    }
}
