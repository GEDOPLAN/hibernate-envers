//
package de.gedoplan.demo.envers.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

/**
 * Entität um zusätliche Informationen zu jeder Revision zu speichern. Neben den
 * Standard-Informationen fügen wir hier z.B. den Benutzernamen und ein
 * Änderungsdatum ein.
 *
 * Das eigentliche Setzen dieser Informationen erfolgt im Referenzierten
 * Listener.
 *
 * @author Dominik Mathmann
 */
@Entity
@RevisionEntity(RevisionDataListener.class)
public class RevisionData extends DefaultRevisionEntity {

    @Temporal(TemporalType.TIMESTAMP)
    private Date changeDate;

    private String username;

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
