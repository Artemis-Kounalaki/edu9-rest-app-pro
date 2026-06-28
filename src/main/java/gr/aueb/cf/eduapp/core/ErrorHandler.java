package gr.aueb.cf.eduapp.core;

import gr.aueb.cf.eduapp.core.exception.*;
import gr.aueb.cf.eduapp.dto.ErrorResponseDTO;
import gr.aueb.cf.eduapp.dto.ValidationErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleValidationException(ValidationException e){
        log.warn("Validation failed. Message={}", e.getMessage());

        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        for(FieldError fieldError :  bindingResult.getFieldErrors()){
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return  new ResponseEntity<>(new ValidationErrorResponseDTO(e.getCode(), e.getMessage(), errors),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException e){
        log.warn("Entity not found. Message ={}" , e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)  //404 - not found
                .body(new ErrorResponseDTO(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(EntityInvalidArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidArgumentException(EntityInvalidArgumentException e){
        log.warn("Entity not found. Message ={}" , e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) //400 - Bad Request
                .body(new ErrorResponseDTO(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityAlreadyExistException(EntityAlreadyExistsException e){
        log.warn("Entity not found. Message ={}" , e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)  //409-CONFLICT
                .body(new ErrorResponseDTO(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponseDTO> handleFileUploadException(FileUploadException e){
        log.warn("File upload failed. Message ={}" , e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)  //500- Internal Server Error
                .body(new ErrorResponseDTO(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleDatabaseException(DataAccessException e){
        log.warn("Database error. Message ={}" , e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)  //500- Internal Server Error
                .body(new ErrorResponseDTO("Database_Error", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception e){
        log.warn("Unexpected error. Message ={}" , e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)  //500- Internal Server Error
                .body(new ErrorResponseDTO("INTERNAL_SERVER_ERROR", "Unexpected error occured."));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(AuthenticationException e,
                                                                          HttpServletRequest request) {
        log.warn("Failed login for IP={}", request.getRemoteAddr());

        String errorCode = switch (e) {
            case BadCredentialsException ex -> "INVALID_CREDENTIALS";
            case DisabledException ex -> "ACCOUNT_DISABLED";
            case LockedException ex -> "ACCOUNT_LOCKED";
            case AccountExpiredException ex -> "ACCOUNT_EXPIRED";
            case CredentialsExpiredException ex -> "CREDENTIALS_EXPIRED";
            default -> "AUTHENTICATION_ERROR";
        };

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)            // 401 Unauthorized
                .body(new ErrorResponseDTO(errorCode, e.getMessage()));
    }

    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException e){
        log.warn("Access denied. Message = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)  //500- Internal Server Error
                .body(new ErrorResponseDTO("ACCESS_DENIED", e.getMessage()));
    }

}
