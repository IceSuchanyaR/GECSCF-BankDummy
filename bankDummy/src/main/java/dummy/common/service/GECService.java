package gec.scf.dummy.common.service;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import gec.scf.dummy.common.domain.ExtendedRepository;
import gec.scf.dummy.common.exception.DataIsInUseException;
import gec.scf.dummy.common.exception.DataNotFoundException;
import gec.scf.dummy.common.exception.DuplicationException;
import gec.scf.dummy.common.exception.InvalidDataException;
import gec.scf.dummy.common.exception.VersionMismatchException;

public interface GECService<T, S extends Serializable> {

	@Transactional(readOnly = true)
	default T getById(S id) throws DataNotFoundException {
		Optional<T> entity = getRepository().findById(id);
		if (!entity.isPresent())
			throw new DataNotFoundException();
		return entity.get();

	}

	@Transactional(readOnly = true)
	default Page<T> getAll(Specification<T> specification, Pageable pageRequest) {
		return getRepository().findAll(specification, pageRequest);
	}

	T create(T model) throws DuplicationException, InvalidDataException;

	T update(T model) throws DuplicationException, VersionMismatchException,
			DataNotFoundException, InvalidDataException;

	void delete(T model)
			throws DataIsInUseException, VersionMismatchException, DataNotFoundException;

	ExtendedRepository<T, S> getRepository();
}
