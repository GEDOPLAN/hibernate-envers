package de.gedoplan.demo.envers.model;

import de.gedoplan.demo.envers.system.User;
import java.util.Date;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import org.hibernate.envers.RevisionListener;

/**
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

    private String getUsername() {
        Instance<String> select = CDI.current().select(String.class, new User.UserQualifier() {
        });
        return select.get();
    }

}
