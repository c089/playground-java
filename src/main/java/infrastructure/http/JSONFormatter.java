package infrastructure.http;

import domain.DeleteServerResponse;

import java.util.stream.Collectors;

public class JSONFormatter {
    public static String formatAsJSON(DeleteServerResponse.DeletionRequestAccepted r) {
        final String ids = r.resourcesToDelete()
                .stream()
                .map(r_ -> "\"" + r_.id().toString() + "\"")
                .collect(Collectors.joining(",", "[", "]"));
        return """
                {"affectedResources": %s}
                """.formatted(ids);
    }
}
