package br.edu.iff.ccc.webappigormonteiro.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorInfo(String path, String message, String error) {
}
