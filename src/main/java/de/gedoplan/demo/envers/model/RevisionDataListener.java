package de.gedoplan.demo.envers.model;

import de.gedoplan.demo.envers.system.User;
import java.util.Date;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import org.hibernate.envers.RevisionListener;

/**
 * Listener um die zusätzliche Revision-Daten zu füllen.
 *
 * @author Dominik Mathmann
 */
public class RevisionDataListener implements RevisionListener {

    @Override
    public void newRevision(Object o) {
        RevisionData revData = (RevisionData) o;
        revData.setChangeDate(new Date());
        revData.setUsername(getUsername());
    }

    /**
     * Kein gültiges CDI-Injektionsziel, aus diesem Grund der "Umweg" über den
     * Beanmanager.
     *
     * @return aktueller Benutzer
     */
    private String getUsername() {
        Instance<String> select = CDI.current().select(String.class, new User.UserQualifier() {
        });
        return select.get();
    }

}
