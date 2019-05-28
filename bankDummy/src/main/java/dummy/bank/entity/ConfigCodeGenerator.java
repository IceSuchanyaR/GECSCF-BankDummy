package dummy.bank.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_config_code_generator")
public class ConfigCodeGenerator extends BaseBankEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "generator_id", nullable = false)
	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	private Long generatorID;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "generator_type", nullable = false, length = 50)
	private String generatorType;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "pattern_code", nullable = false, length = 20)
	private String patternCode;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "pattern", nullable = false, length = 20)
	private String pattern;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "next_running", nullable = false)
	private int nextRunning;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "running_step", nullable = false)
	private int runningStep;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "running_digit", nullable = false)
	private int runningDigit;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "last_generate_code", nullable = false, length = 100)
	private String lastGenerateCode;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "status", nullable = false, length = 2)
	private String status;

	public Long getGeneratorID() {
		return generatorID;
	}

	public void setGeneratorID(Long generatorID) {
		this.generatorID = generatorID;
	}

	public String getGeneratorType() {
		return generatorType;
	}

	public void setGeneratorType(String generatorType) {
		this.generatorType = generatorType;
	}

	public String getPatternCode() {
		return patternCode;
	}

	public void setPatternCode(String patternCode) {
		this.patternCode = patternCode;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int getNextRunning() {
		return nextRunning;
	}

	public void setNextRunning(int nextRunning) {
		this.nextRunning = nextRunning;
	}

	public int getRunningStep() {
		return runningStep;
	}

	public void setRunningStep(int runningStep) {
		this.runningStep = runningStep;
	}

	public int getRunningDigit() {
		return runningDigit;
	}

	public void setRunningDigit(int runningDigit) {
		this.runningDigit = runningDigit;
	}

	public String getLastGenerateCode() {
		return lastGenerateCode;
	}

	public void setLastGenerateCode(String lastGenerateCode) {
		this.lastGenerateCode = lastGenerateCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
