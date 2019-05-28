package gec.scf.dummy.bank.kbankws.generator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gec.scf.dummy.bank.entity.ConfigCodeGenerator;

public interface ConfigCodeGeneratorRepository extends JpaRepository<ConfigCodeGenerator, String> {

	public ConfigCodeGenerator findConfigCodeGeneratorByGeneratorType(String generatorType);
}
