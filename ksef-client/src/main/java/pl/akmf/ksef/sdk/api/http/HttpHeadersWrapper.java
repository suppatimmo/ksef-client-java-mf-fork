package pl.akmf.ksef.sdk.api.http;

import java.util.*;

/**
 * Simple wrapper for HTTP headers to replace java.net.http.HttpHeaders for Java 8 compatibility
 */
public class HttpHeadersWrapper {
    private final Map<String, List<String>> headers;

    public HttpHeadersWrapper(Map<String, List<String>> headers) {
        this.headers = new HashMap<>(headers);
    }

    public Optional<String> firstValue(String name) {
        List<String> values = headers.get(name);
        if (values == null || values.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(values.get(0));
    }

    public List<String> allValues(String name) {
        List<String> values = headers.get(name);
        return values != null ? new ArrayList<>(values) : Collections.emptyList();
    }

    public Map<String, List<String>> map() {
        return new HashMap<>(headers);
    }
}
