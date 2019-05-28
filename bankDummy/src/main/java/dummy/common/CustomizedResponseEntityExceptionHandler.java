package gec.scf.dummy.common;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import gec.scf.dummy.common.domain.ErrorDetails;
import gec.scf.dummy.common.domain.ErrorItem;
import gec.scf.dummy.common.exception.DataIsInUseException;
import gec.scf.dummy.common.exception.DataNotFoundException;
import gec.scf.dummy.common.exception.DuplicationException;
import gec.scf.dummy.common.exception.VersionMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler
		extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {

		Collection<ErrorItem> errorsItems = ex.getBindingResult().getFieldErrors()
				.stream().map(ErrorItem::new).collect(Collectors.toList());

		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
				ex.getMessage(), "Argument not valid", errorsItems);
		return ResponseEntity.badRequest().body(errorDetails);
	}

	@ExceptionHandler({ VersionMismatchException.class })
	public ResponseEntity<Object> handleVersionMismatchException(
			VersionMismatchException ex, WebRequest request) {

		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
				ex.getEvent(), "Version not matched");
		return new ResponseEntity<>(errorDetails, new HttpHeaders(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler({ DuplicationException.class })
	public ResponseEntity<Object> handleDuplicationException(DuplicationException ex,
			WebRequest request) {

		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                ex.getEvent(), "Data is exist or duplication",
				ex.getErrorFields());
		return new ResponseEntity<>(errorDetails, new HttpHeaders(),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ DataNotFoundException.class })
	public ResponseEntity<Object> handleDuplicationException(DataNotFoundException ex,
			WebRequest request) {

		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                ex.getEvent(), "Data is not exist", ex.getErrors());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ DataIsInUseException.class })
	public ResponseEntity<Object> handleDataIsInUseException(DataIsInUseException ex,
			WebRequest request) {

		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                ex.getEvent(), "Data is in use");

		return new ResponseEntity<>(errorDetails, new HttpHeaders(), HttpStatus.CONFLICT);
	}

}
