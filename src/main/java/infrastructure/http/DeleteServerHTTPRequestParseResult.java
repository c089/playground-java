package infrastructure.http;

import domain.DeleteServerRequest;

sealed public interface DeleteServerHTTPRequestParseResult {
    record ValidRequest(DeleteServerRequest request) implements DeleteServerHTTPRequestParseResult {
    }

    record InvalidRequest(String reason) implements DeleteServerHTTPRequestParseResult {
    }
}
