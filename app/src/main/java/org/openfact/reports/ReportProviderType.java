package org.openfact.reports;

import javax.inject.Qualifier;
import java.lang.annotation.*;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Documented
public @interface ReportProviderType {

    ProviderType type();

    enum ProviderType {
        EXTENDING, FOLDER, JAR
    }

}