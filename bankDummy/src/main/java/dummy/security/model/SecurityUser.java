package dummy.security.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class SecurityUser extends User {

	private static final long serialVersionUID = -3283600889558675406L;

	private final String displayName;

	private Long userId;

	private boolean forceChangePassword;

	private String organizeId;

	private String fundingId;

	public SecurityUser(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
			Long userId, String displayName, boolean forceChangePassword,
			String organizeId, String fundingId) {

		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		this.displayName = displayName;
		this.userId = userId;
		this.forceChangePassword = forceChangePassword;
		this.organizeId = organizeId;
		this.fundingId = fundingId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isForceChangePassword() {
		return forceChangePassword;
	}

	public void setForceChangePassword(boolean forceChangePassword) {
		this.forceChangePassword = forceChangePassword;
	}

	public String getOrganizeId() {
		return organizeId;
	}

	public void setOrganizeId(String organizeId) {
		this.organizeId = organizeId;
	}

	public String getFundingId() {
		return fundingId;
	}

	public void setFundingId(String fundingId) {
		this.fundingId = fundingId;
	}

}
