package gec.scf.dummy.sponsor.ptt.document.status.service;

import gec.scf.dummy.sponsor.ptt.document.status.domain.UpdateDocumentStatus;
import gec.scf.dummy.sponsor.ptt.document.status.domain.UpdateDocumentStatusPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gec.scf.dummy.common.domain.ExtendedRepository;
import gec.scf.dummy.common.exception.DataIsInUseException;
import gec.scf.dummy.common.exception.DataNotFoundException;
import gec.scf.dummy.common.exception.DuplicationException;
import gec.scf.dummy.common.exception.InvalidDataException;
import gec.scf.dummy.common.exception.VersionMismatchException;
import gec.scf.dummy.sponsor.ptt.document.status.domain.UpdateDocumentStatusRepository;

@Service 
public class UpdateDocumentStatusServiceImp implements UpdateDocumentStatusService {

   @Autowired
   private UpdateDocumentStatusRepository updateDocumentStatusRepository;

   @Override
   public UpdateDocumentStatus create(UpdateDocumentStatus model) throws DuplicationException, InvalidDataException {
      return updateDocumentStatusRepository.save(model);
   }

   @Override
   public UpdateDocumentStatus update(UpdateDocumentStatus model)
         throws DuplicationException, VersionMismatchException, DataNotFoundException, InvalidDataException {
      return null;
   }

   @Override
   public void delete(UpdateDocumentStatus model)
         throws DataIsInUseException, VersionMismatchException, DataNotFoundException {
   }

   @Override
   public ExtendedRepository<UpdateDocumentStatus, UpdateDocumentStatusPK> getRepository() {
      return updateDocumentStatusRepository;
   }

}