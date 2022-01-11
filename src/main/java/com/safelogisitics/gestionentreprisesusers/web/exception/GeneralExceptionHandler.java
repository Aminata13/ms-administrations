package com.safelogisitics.gestionentreprisesusers.web.exception;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

  // 400

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final List<String> errors = new ArrayList<String>();
    for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getDefaultMessage());
    }
    for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getDefaultMessage());
    }
    final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors, ex.getLocalizedMessage());
    return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final List<String> errors = new ArrayList<String>();
    for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getDefaultMessage());
    }
    for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getDefaultMessage());
    }
    final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors, ex.getLocalizedMessage());
    return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final String error = "La valeur " + ex.getValue() + " pour " + ex.getPropertyName() + " doit être de type  " + ex.getRequiredType();

    final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error, ex.getLocalizedMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final String error = "La partie " + ex.getRequestPartName()+ " est manquante";
    final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error, ex.getLocalizedMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final String error = "Le paramètre " + ex.getParameterName()+ " est manquant";
    final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error, ex.getLocalizedMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  //

  @ExceptionHandler({ IllegalArgumentException.class })
  public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex, final WebRequest request) {
    logger.info(ex.getClass().getName());
    logger.error("error", ex);
    //
    final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), "Une erreur est survenue");
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final String error = ex.getName() + " doit être de type  " + ex.getRequiredType().getName();

    final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error, ex.getLocalizedMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler({ ConstraintViolationException.class })
  public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final List<String> errors = new ArrayList<String>();
    for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
    }

    final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors, ex.getLocalizedMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  // 404

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final String error = "Aucun gestionnaire trouvé pour " + ex.getHttpMethod() + " " + ex.getRequestURL();

    final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, error, ex.getLocalizedMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  // 405

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final StringBuilder builder = new StringBuilder();
    builder.append("La méthode " + ex.getMethod() + " n'est pas prise en charge pour cette requête. Les méthodes prises en charge sont ");
    ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

    final ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, builder.toString(), ex.getLocalizedMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  // 415

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final StringBuilder builder = new StringBuilder();
    builder.append(ex.getContentType());
    builder.append(" type de fichier n'est pas pris en charge. Les types de fichiers pris en charge sont ");
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

    final ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex.getLocalizedMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<Object> handleMaxSizeException(MaxUploadSizeExceededException ex, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final String error = "La taille du fichier ne doit pas dépasser 200MB";

    final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST , error, ex.getLocalizedMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handleMaxSizeException(AccessDeniedException ex, final WebRequest request) {
    logger.info(ex.getClass().getName());
    logger.error("error", ex);
    //
    final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, "Vous n'avez pas les droits pour accéder à l'api "+ request.getContextPath(), ex.getMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  // 500

  @ExceptionHandler({ Exception.class })
  public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
    logger.info(ex.getClass().getName());
    logger.error("error", ex);
    //
    final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur est survenue, merci de réessayer plus tard", ex.getLocalizedMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }
}