package infrastructure.http;

import com.google.gson.Gson;
import domain.DeleteServerResponse;
import domain.ResourceID;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JSONFormatter {
    public static String formatAsJSON(DeleteServerResponse.DeletionRequestAccepted r) {
        final List<UUID> uuids = r.resourcesToDelete().stream().map(ResourceID::id).toList();
        return new Gson().toJson(Map.of("affectedResources", uuids));
    }
}
