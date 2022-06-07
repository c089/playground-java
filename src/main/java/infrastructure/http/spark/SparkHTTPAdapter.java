package infrastructure.http.spark;

import domain.DeleteServerRequest;
import domain.DeleteServerRequest.DeleteAttachedVolumesOption;
import domain.DeleteServerResponse;
import domain.ServerID;
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

    public SparkHTTPAdapter(DeleteServerUseCase useCase) {
        this.useCase = useCase;
    }

    public void start() {
        Spark.port(8080);
        Spark.delete("/server/:serverId", (req, res) -> {
            final var deleteServerRequest = understandDeleteServerRequest(req);
            final var deleteServerResponse = this.useCase.deleteServer(deleteServerRequest);
            return sendResponse(res, deleteServerResponse);
        });
    }

    private String sendResponse(Response httpResponse, DeleteServerResponse deleteServerResponse) {
        return switch (deleteServerResponse) {
            case DeletionRequestAccepted x -> {
                httpResponse.status(200);
                httpResponse.type("application/json");
                yield JSONFormatter.formatAsJSON(x);
            }
            case CannotDeleteNonExistingServer x -> {
                httpResponse.status(404);
                yield "";
            }
        };
    }

    private DeleteServerRequest understandDeleteServerRequest(Request req) {
        final ServerID serverId = new ServerID(UUID.fromString(req.params("serverId")));
        return new DeleteServerRequest(serverId, keepOrDeleteAttachedVolumes(req));
    }

    private DeleteAttachedVolumesOption keepOrDeleteAttachedVolumes(Request req) {
        return req.queryParamOrDefault("deleteVolumes", "false").equals("true") ?
                DeleteAttachedVolumes :
                KeepAttachedVolumes;
    }
}