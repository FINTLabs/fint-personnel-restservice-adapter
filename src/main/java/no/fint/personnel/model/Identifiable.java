package no.fint.personnel.model;

import no.fint.model.FintMainObject;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import org.jooq.lambda.tuple.Tuple2;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

public class Identifiable<T extends FintMainObject> {
    private final T value;
    private final Function<T, Identifikator>[] identifiers;

    public Identifiable(T value, Function<T, Identifikator> ... identifiers) {
        this.value = value;
        this.identifiers = identifiers;
    }

    public Stream<Identifikator> identifiers() {
        return Arrays.stream(identifiers).map(f -> f.apply(value));
    }

    public T value() {
        return value;
    }

    public Tuple2<Stream<Identifikator>, T> tuple() {
        return new Tuple2<>(identifiers(), value());
    }
}
