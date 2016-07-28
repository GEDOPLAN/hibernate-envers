package de.gedoplan.demo.envers.system;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Producer der den Benutzer ermittelt, hier nur ein Dummy-Beispiel.
 *
 * @author Dominik Mathmann
 */
@ApplicationScoped
public class UserProducer {

    @Produces
    @User
    public String getUserName() {
        // Username ermitteln
        return "Dummy-User";
    }
}
