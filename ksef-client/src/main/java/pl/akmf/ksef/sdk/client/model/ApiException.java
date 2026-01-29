package pl.akmf.ksef.sdk.client.model;

import pl.akmf.ksef.sdk.api.http.HttpHeadersWrapper;

public class ApiException extends Exception {
    private final int code;
    private final transient HttpHeadersWrapper responseHeaders;
    private final transient ExceptionResponse exceptionResponse;

    public ApiException(int code, String message) {
        super(message);
         this.code = code;
        this.responseHeaders = null;
        this.exceptionResponse = null;
    }

    public ApiException(Throwable throwable) {
        super(throwable);
        this.code = 0;
        this.responseHeaders = null;
        this.exceptionResponse = null;
    }

    public ApiException(String message) {
        super(message);
        this.code = 0;
        this.responseHeaders = null;
        this.exceptionResponse = null;
    }

    public ApiException(int code, String message, HttpHeadersWrapper responseHeaders, ExceptionResponse exceptionResponse) {
        super(message);
        this.code = code;
        this.exceptionResponse = exceptionResponse;
        this.responseHeaders = responseHeaders;
    }

    public int getCode() {
        return code;
    }

    public ExceptionResponse getExceptionResponse() {
        return exceptionResponse;
    }

    public HttpHeadersWrapper getResponseHeaders() {
        return responseHeaders;
    }
}
