package com.castores.exceptions;

import static com.castores.util.Constantes.MENSAJE;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author Luis.Bonifaz
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler(CustomExceptions.ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(CustomExceptions.ResourceNotFoundException ex) {
        LOGGER.error("Resource Not Found: ", ex);
        Map<String, Object> body = new HashMap<>();
        body.put(MENSAJE, "No se encontraron resultados");
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomExceptions.ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(CustomExceptions.ForbiddenException ex) {
        LOGGER.error("Forbidden Access: ", ex);
        Map<String, Object> body = new HashMap<>();
        body.put(MENSAJE, "Acceso denegado");
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CustomExceptions.BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(CustomExceptions.BadRequestException ex) {
        LOGGER.error("Bad Request: ", ex);
        Map<String, Object> body = new HashMap<>();
        body.put(MENSAJE, "Solicitud incorrecta");
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        LOGGER.error("Validation Error: ", ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        LOGGER.error("An unexpected error occurred: ", ex);
        Map<String, Object> body = new HashMap<>();
        body.put(MENSAJE, "Ocurrió un error inesperado");
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
