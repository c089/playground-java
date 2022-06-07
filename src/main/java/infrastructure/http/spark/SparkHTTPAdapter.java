package infrastructure.http.spark;

import domain.DeleteServerRequest;
import domain.DeleteServerRequest.DeleteAttachedVolumesOption;
import domain.DeleteServerResponse;
import domain.ServerID;
import infrastructure.http.DeleteServerHTTPRequestParseResult;
import infrastructure.http.JSONFormatter;
import ports.driving.DeleteServerUseCase;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.UUID;

import static domain.DeleteServerRequest.DeleteAttachedVolumesOption.DeleteAttachedVolumes;
import static domain.DeleteServerRequest.DeleteAttachedVolumesOption.KeepAttachedVolumes;
import static domain.DeleteServerResponse.CannotDeleteNonExistingServer;
import static domain.DeleteServerResponse.DeletionRequestAccepted;

public class SparkHTTPAdapter {
    private final DeleteServerUseCase useCase;
    private final JSONFormatter jsonFormatter = new JSONFormatter();

    public SparkHTTPAdapter(DeleteServerUseCase useCase) {
        this.useCase = useCase;
    }

    public void start() {
        Spark.port(8080);
        Spark.delete("/server/:serverId", (req, res) -> {
            final DeleteServerHTTPRequestParseResult httpRequest = parseRequest(req);
            return switch (httpRequest) {
                case DeleteServerHTTPRequestParseResult.InvalidRequest r -> sendBadRequest(res, r);
                case DeleteServerHTTPRequestParseResult.ValidRequest r ->
                        sendResponse(res, this.useCase.deleteServer(r.request()));
            };
        });
    }

    private String sendBadRequest(Response res, DeleteServerHTTPRequestParseResult.InvalidRequest r) {
        res.status(400);
        res.type("application/json");
        return jsonFormatter.formatAsJSON(r);
    }

    private DeleteServerHTTPRequestParseResult parseRequest(Request req) {
        return req.queryParams().stream()
                .filter(x -> !x.equals("deleteVolumes")).findFirst()
                .<DeleteServerHTTPRequestParseResult>map(reason -> new DeleteServerHTTPRequestParseResult.InvalidRequest("Found unexpected query parameter: \"%s\".".formatted(reason)))
                .orElseGet(() -> understandDeleteServerRequest(req));
    }

    private String sendResponse(Response httpResponse, DeleteServerResponse deleteServerResponse) {
        return switch (deleteServerResponse) {
            case DeletionRequestAccepted x -> {
                httpResponse.status(200);
                httpResponse.type("application/json");
                yield jsonFormatter.formatAsJSON(x);
            }
            case CannotDeleteNonExistingServer x -> {
                httpResponse.status(404);
                httpResponse.type("application/json");
                yield jsonFormatter.formatAsJSON(x);
            }
        };
    }

    private DeleteServerHTTPRequestParseResult understandDeleteServerRequest(Request req) {
        final ServerID serverId = new ServerID(UUID.fromString(req.params("serverId")));
        return new DeleteServerHTTPRequestParseResult.ValidRequest(new DeleteServerRequest(serverId, keepOrDeleteAttachedVolumes(req)));
    }

    private DeleteAttachedVolumesOption keepOrDeleteAttachedVolumes(Request req) {
        return req.queryParamOrDefault("deleteVolumes", "false").equals("true") ?
                DeleteAttachedVolumes :
                KeepAttachedVolumes;
    }
}