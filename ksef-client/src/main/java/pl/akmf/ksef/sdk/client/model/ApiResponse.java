package pl.akmf.ksef.sdk.client.model;

import pl.akmf.ksef.sdk.api.http.HttpHeadersWrapper;

public class ApiResponse<T> {
    private final int statusCode;
    private final HttpHeadersWrapper headers;
    private final T data;

    /**
     * @param statusCode The status code of HTTP response
     * @param headers    The headers of HTTP response
     */
    public ApiResponse(int statusCode, HttpHeadersWrapper headers) {
        this(statusCode, headers, null);
    }

    /**
     * @param statusCode The status code of HTTP response
     * @param headers    The headers of HTTP response
     * @param data       The object deserialized from response bod
     */
    public ApiResponse(int statusCode, HttpHeadersWrapper headers, T data) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public HttpHeadersWrapper getHeaders() {
        return headers;
    }

    public T getData() {
        return data;
    }
}
