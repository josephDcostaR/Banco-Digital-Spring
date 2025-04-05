package com.br.cbd.BancoDigitalJPA.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = MinAgeValidator.class )
public @interface MinAge {

    int value();

    String message() default "A idade deve ser no mínimo {value} anos";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
