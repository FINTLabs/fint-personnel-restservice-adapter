package no.fint.personnel;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@ConfigurationProperties("fint.personnel")
@Component
@Data
public class Properties {
    private Path repository;
}
