package dummy.bank.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_config_application")
public class ConfigApplication extends BaseBankEntity{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "config_id", nullable = false)
	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	private Long configID;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "config_group", nullable = false, length = 50)
	private String configGroup;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "config_value", nullable = false, length = 100)
	private String configValue;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "config_display", nullable = false, length = 255)
	private String configDisplay;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "description", nullable = true, length = 255)
	private String description;

	public Long getConfigID() {
		return configID;
	}

	public void setConfigID(Long configID) {
		this.configID = configID;
	}

	public String getConfigGroup() {
		return configGroup;
	}

	public void setConfigGroup(String configGroup) {
		this.configGroup = configGroup;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getConfigDisplay() {
		return configDisplay;
	}

	public void setConfigDisplay(String configDisplay) {
		this.configDisplay = configDisplay;
	}

	public String isDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
