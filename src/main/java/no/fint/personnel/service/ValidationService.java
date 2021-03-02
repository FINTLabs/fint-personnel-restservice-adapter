package no.fint.personnel.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.function.Consumer;

@Service
@Slf4j
public class ValidationService {

    @Autowired
    private ValidatorFactory validatorFactory;

    /**
     * Set to `false` to make validations result in errors.
     */
    @Value("${fint.validation.ignore:true}")
    private boolean ignoreValidation;

    public <T> Consumer<T> validate() {
        final Validator validator = validatorFactory.getValidator();
        return item -> {
            final Set<ConstraintViolation<T>> violations = validator.validate(item);
            log.trace("Item {}, violations: {}", item, violations);
            if (ignoreValidation || violations == null || violations.isEmpty()) {
                return;
            }
            throw new ValidationException(String.format("Validation failures for object %s\n%s", item, violations));
        };
    }

}
