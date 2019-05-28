package gec.scf.dummy.security.model;

import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "tbl_role", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})})
public class Role implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @JsonView({UserEvent.View.DetailForCommonUser.class})
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @JsonView({UserEvent.View.DetailForAllUser.class, UserEvent.View.DetailForCommonUser.class})
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_roles_privileges", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    @OrderBy
    @JsonView({UserEvent.View.DetailForAllUser.class, UserEvent.View.DetailForCommonUser.class})
    private Set<Privilege> privileges = new HashSet<>();

    public Role() {
    }

    public Role(Long id, String name, Set<Privilege> privileges) {
        this.id = id;
        this.name = name;
        this.privileges = privileges;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Role other = (Role) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}