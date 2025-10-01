package br.edu.iff.ccc.webappigormonteiro.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public Object handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            ProblemDetail problem = createProblem(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage(), request);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
        }
        ModelAndView mv = new ModelAndView("error/404");
        mv.addObject("mensagem", ex.getMessage());
        return mv;
    }

    @ExceptionHandler(BusinessException.class)
    public Object handleBusiness(BusinessException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            ProblemDetail problem = createProblem(HttpStatus.BAD_REQUEST, "Regra de negócio violada", ex.getMessage(), request);
            return ResponseEntity.badRequest().body(problem);
        }
        ModelAndView mv = new ModelAndView("error/error");
        mv.addObject("mensagem", ex.getMessage());
        return mv;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        if (isApiRequest(request)) {
            ProblemDetail problem = createProblem(HttpStatus.BAD_REQUEST, "Dados inválidos", message, request);
            problem.setProperty("errors", ex.getBindingResult().getFieldErrors().stream()
                    .map(error -> {
                        return error.getField() + ": " + error.getDefaultMessage();
                    })
                    .toList());
            return ResponseEntity.badRequest().body(problem);
        }
        ModelAndView mv = new ModelAndView("error/error");
        mv.addObject("mensagem", message);
        return mv;
    }

    @ExceptionHandler(Exception.class)
    public Object handleDefault(Exception ex, HttpServletRequest request) {
        log.error("Erro não tratado", ex);
        if (isApiRequest(request)) {
            ProblemDetail problem = createProblem(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado", ex.getMessage(), request);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }
        ModelAndView mv = new ModelAndView("error/error");
        mv.addObject("mensagem", ex.getMessage());
        mv.addObject("stacktrace", getStackTraceAsString(ex));
        return mv;
    }

    private boolean isApiRequest(HttpServletRequest request) {
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        if (accept == null) {
            return false;
        }
        return accept.contains(MediaType.APPLICATION_JSON_VALUE) || accept.contains(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
    }

    private String getStackTraceAsString(Exception ex) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append(element).append(System.lineSeparator());
        }
        return sb.toString();
    }

    private ProblemDetail createProblem(HttpStatus status, String title, String detail, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(status.is4xxClientError()
                ? URI.create("https://httpstatuses.io/" + status.value())
                : URI.create("about:blank"));
    problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("path", request.getRequestURI());
        return problem;
    }
}
