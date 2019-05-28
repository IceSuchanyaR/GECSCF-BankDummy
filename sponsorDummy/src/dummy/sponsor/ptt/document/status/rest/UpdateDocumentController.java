package gec.scf.dummy.sponsor.ptt.document.status.rest;

import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import gec.scf.dummy.common.PaginationUtil;
import gec.scf.dummy.sponsor.ptt.document.status.domain.UpdateDocumentStatus;
import gec.scf.dummy.sponsor.ptt.document.status.domain.UpdateDocumentStatuses;
import gec.scf.dummy.sponsor.ptt.document.status.service.UpdateDocumentStatusService;

@RestController
@RequestMapping(path = "/api")
public class UpdateDocumentController {

	@Autowired
	private UpdateDocumentStatusService updateDocumentStatuService;

	@JsonView(UpdateDocumentStatuses.View.ListForAll.class)
	@GetMapping("/update-status-documents")
	public ResponseEntity<List<UpdateDocumentStatus>> getAll(Pageable pageable) throws URISyntaxException {

		Page<UpdateDocumentStatus> updateDocumentStatusPages = updateDocumentStatuService.getAll(null, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(updateDocumentStatusPages);
		return new ResponseEntity<>(updateDocumentStatusPages.getContent(), headers, HttpStatus.OK);
	}

}
