package infrastructure.http;

import com.google.gson.Gson;
import domain.DeleteServerResponse;
import domain.ResourceID;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JSONFormatter {

    private final Gson gson = new Gson();

    public String formatAsJSON(DeleteServerResponse.DeletionRequestAccepted r) {
        final List<UUID> uuids = r.resourcesToDelete().stream().map(ResourceID::id).toList();
        return gson.toJson(Map.of("affectedResources", uuids));
    }

    public String formatAsJSON(DeleteServerResponse.CannotDeleteNonExistingServer r) {
        return gson.toJson(Map.of("error", "Server with id %s does not exist.".formatted(r.uuid().id())));
    }

    public String formatAsJSON(DeleteServerHTTPRequestParseResult.InvalidRequest r) {
        return gson.toJson(Map.of("error", r.reason()));
    }
}
