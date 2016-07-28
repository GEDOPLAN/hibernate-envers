package de.gedoplan.demo.envers.system;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;

/**
 * User Qualifier für den Producer der den aktuellen Benutzer liefert.
 *
 * @author Dominik Mathmann
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
public @interface User {

    /**
     * AnnotationLiteral um den korrekten Typ über den Bean-Manager zu
     * ermitteln.
     */
    class UserQualifier extends AnnotationLiteral<User>
            implements User {
    }
}
