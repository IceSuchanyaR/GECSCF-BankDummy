package gec.scf.dummy.security.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_privilege", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "name" }) })
@Immutable
public class Privilege implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8384589511907040689L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView({UserEvent.View.DetailForCommonUser.class})
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAME")
	@JsonView({UserEvent.View.DetailForAllUser.class,UserEvent.View.DetailForCommonUser.class})
	private String name;

	@ManyToMany(mappedBy = "privileges")
	@JsonIgnore
	@OrderBy
	private Set<Role> roles;

	public Privilege() {
	}

	public Privilege(Long id, String name, Set<Role> roles) {
		this.id = id;
		this.name = name;
		this.roles = roles;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
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
		Privilege other = (Privilege) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

}