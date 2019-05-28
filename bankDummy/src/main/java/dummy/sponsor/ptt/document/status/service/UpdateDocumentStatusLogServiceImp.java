package gec.scf.dummy.sponsor.ptt.document.status.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gec.scf.dummy.common.domain.ExtendedRepository;
import gec.scf.dummy.common.exception.DataIsInUseException;
import gec.scf.dummy.common.exception.DataNotFoundException;
import gec.scf.dummy.common.exception.DuplicationException;
import gec.scf.dummy.common.exception.InvalidDataException;
import gec.scf.dummy.common.exception.VersionMismatchException;
import gec.scf.dummy.sponsor.ptt.document.status.domain.UpdateDocumentStatusLog;
import gec.scf.dummy.sponsor.ptt.document.status.domain.UpdateDocumentStatusLogRepository;

@Service
public class UpdateDocumentStatusLogServiceImp implements UpdateDocumentStatusLogService {

	@Autowired
	UpdateDocumentStatusLogRepository updateDocumentStatusLogRepository;

	@Override
	public UpdateDocumentStatusLog create(UpdateDocumentStatusLog entity)
			throws DuplicationException, InvalidDataException {
		return updateDocumentStatusLogRepository.save(entity);
	}

	@Override
	public UpdateDocumentStatusLog update(UpdateDocumentStatusLog model)
			throws DuplicationException, VersionMismatchException, DataNotFoundException, InvalidDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(UpdateDocumentStatusLog model)
			throws DataIsInUseException, VersionMismatchException, DataNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public ExtendedRepository<UpdateDocumentStatusLog, String> getRepository() {
		return updateDocumentStatusLogRepository;
	}

}