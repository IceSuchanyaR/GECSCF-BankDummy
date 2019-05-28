package gec.scf.dummy.sponsor.ptt.document.status.rest;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gec.scf.dummy.common.PaginationUtil;
import gec.scf.dummy.common.exception.DuplicationException;
import gec.scf.dummy.common.exception.InvalidDataException;
import gec.scf.dummy.sponsor.ptt.document.status.domain.UpdateDocumentStatus;
import gec.scf.dummy.sponsor.ptt.document.status.domain.UpdateDocumentStatusLog;
import gec.scf.dummy.sponsor.ptt.document.status.domain.UpdateDocumentStatusLogRepository;
import gec.scf.dummy.sponsor.ptt.document.status.domain.UpdateDocumentStatuses;
import gec.scf.dummy.sponsor.ptt.document.status.service.UpdateDocumentStatusLogService;

@RestController
@RequestMapping(path = "/document/v1")
public class UpdateDocumentStatusController {

	@Autowired
	UpdateDocumentStatusLogService updateDocumentStatusLogService;

	@PostMapping("/update-status-documents")
	public ResponseEntity<UpdateDocumentStatusLog> insertAll(@RequestBody String requestMessage,
			HttpServletRequest request) throws DuplicationException, InvalidDataException, JsonProcessingException {

		Map<String, String> headers = getHeadersInfo(request);
		String headerJson = new ObjectMapper().writeValueAsString(headers);
		UpdateDocumentStatusLog log = new UpdateDocumentStatusLog();
		log.setRequestHeader(headerJson);
		log.setRequestMessage(requestMessage);
		log.setRequestTime(LocalDateTime.now());
		return new ResponseEntity<>(updateDocumentStatusLogService.create(log), HttpStatus.OK);
	}

	@GetMapping("/update-status-document-logs")
	public ResponseEntity<List<UpdateDocumentStatusLog>> getAll(Pageable pageable) throws URISyntaxException {

		Page<UpdateDocumentStatusLog> updateDocumentStatusPages = updateDocumentStatusLogService.getAll(null, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(updateDocumentStatusPages);
		return new ResponseEntity<>(updateDocumentStatusPages.getContent(), headers, HttpStatus.OK);
	}

	private Map<String, String> getHeadersInfo(HttpServletRequest request) {

		Map<String, String> map = new HashMap<String, String>();

		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}

		return map;
	}
}
