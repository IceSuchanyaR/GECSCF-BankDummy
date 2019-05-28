package gec.scf.dummy.common.domain;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ExtendedRepository<T, S extends Serializable>
		extends CrudRepository<T, S> {

	Page<T> findAll(Specification<T> specification, Pageable pageRequest);

}
