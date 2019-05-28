package gec.scf.dummy.bank.kbankws.generator.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import gec.scf.dummy.bank.entity.ConfigCodeGenerator;
import gec.scf.dummy.bank.kbankws.Constants;
import gec.scf.dummy.bank.kbankws.enums.ServiceType;
import gec.scf.dummy.bank.kbankws.generator.repository.ConfigCodeGeneratorRepository;
import gec.scf.dummy.bank.kbankws.generator.service.GeneratorCoreBankService;

@Service
@Profile(Constants.Profiles.PRODUCTION)
public class GeneratorCoreBankServiceImpl implements GeneratorCoreBankService {

	@Override
	public String generateBankTransactionNo(ServiceType serviceType) throws Exception {
		String code = "";
		try {
			if (serviceType == null) {
				return "";
			}

			ConfigCodeGenerator configCodeGenerator = configCodeGeneratorRepository
					.findConfigCodeGeneratorByGeneratorType(serviceType.getCode());
			if (configCodeGenerator == null) {
				return "";
			}
			code = processGenerateCode(configCodeGenerator);
		} catch (Exception e) {
		}
		return code;
	}

	private String processGenerateCode(ConfigCodeGenerator config) {
		String code = "";
		try {
			if ("IN".equals(config.getStatus())) {
				return "";
			}
			String pattern = config.getPattern();
			SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en"));
			String patternCode = config.getPatternCode();
			int runningDigit = config.getRunningDigit();
			int nextRunning = config.getNextRunning();
			int runningStep = config.getRunningStep();
			Date updateTime = config.getUpdateTime();

			// Check Today
			if (!isRunningToday(updateTime)) {
				nextRunning = 2;
				runningStep = 1;
			}

			int length = runningDigit - (Integer.valueOf(String.valueOf(nextRunning).length()));
			String digitGen = "";
			for (int i = 0; i < length; i++) {
				digitGen = digitGen + "0";
			}
			code = code + patternCode + format.format(new Date()) + digitGen + nextRunning;

			nextRunning++;
			runningStep++;

			config.setLastGenerateCode(code);
			config.setUpdateTime(new Date());
			config.setRunningStep(runningStep);
			config.setNextRunning(nextRunning);
			// Update ConfigCodeGenerator
			configCodeGeneratorRepository.save(config);
		} catch (Exception e) {
		}
		return code;
	}

	private boolean isRunningToday(Date updateTime) {
		boolean result = false;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", new Locale("en"));
			String updateTimeFormat = format.format(updateTime);
			String currentTimeFormat = format.format(new Date());
			if (currentTimeFormat.equals(updateTimeFormat)) {
				result = true;
			}
		} catch (Exception e) {
		}
		return result;
	}

	@Autowired
	private ConfigCodeGeneratorRepository configCodeGeneratorRepository;
}
