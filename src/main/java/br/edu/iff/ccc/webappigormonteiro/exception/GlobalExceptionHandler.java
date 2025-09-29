package br.edu.iff.ccc.webappigormonteiro.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public Object handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        if (isJsonRequest(request)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorInfo(request.getRequestURI(), ex.getMessage(), "NOT_FOUND"));
        }
        ModelAndView mv = new ModelAndView("error/404");
        mv.addObject("mensagem", ex.getMessage());
        return mv;
    }

    @ExceptionHandler(BusinessException.class)
    public Object handleBusiness(BusinessException ex, HttpServletRequest request) {
        if (isJsonRequest(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorInfo(request.getRequestURI(), ex.getMessage(), "BUSINESS_ERROR"));
        }
        ModelAndView mv = new ModelAndView("error/error");
        mv.addObject("mensagem", ex.getMessage());
        return mv;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        if (isJsonRequest(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorInfo(request.getRequestURI(), ex.getMessage(), "ACCESS_DENIED"));
        }
        ModelAndView mv = new ModelAndView("error/403");
        mv.addObject("mensagem", ex.getMessage());
        return mv;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        if (isJsonRequest(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorInfo(request.getRequestURI(), message, "VALIDATION_ERROR"));
        }
        ModelAndView mv = new ModelAndView("error/error");
        mv.addObject("mensagem", message);
        return mv;
    }

    @ExceptionHandler(Exception.class)
    public Object handleDefault(Exception ex, HttpServletRequest request) {
        log.error("Erro não tratado", ex);
        if (isJsonRequest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorInfo(request.getRequestURI(), ex.getMessage(), "INTERNAL_ERROR"));
        }
        ModelAndView mv = new ModelAndView("error/error");
        mv.addObject("mensagem", ex.getMessage());
        mv.addObject("stacktrace", getStackTraceAsString(ex));
        return mv;
    }

    private boolean isJsonRequest(HttpServletRequest request) {
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        return accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE);
    }

    private String getStackTraceAsString(Exception ex) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append(element).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
