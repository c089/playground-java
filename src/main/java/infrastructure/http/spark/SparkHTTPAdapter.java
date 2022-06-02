package infrastructure.http.spark;

import domain.DeleteServerRequest;
import domain.DeleteServerResponse;
import domain.ServerID;
import infrastructure.http.JSONFormatter;
import ports.driving.DeleteServerUseCase;
import spark.Request;
import spark.Spark;

import java.util.UUID;

import static domain.DeleteServerRequest.DeleteAttachedVolumesOption.DeleteAttachedVolumes;
import static domain.DeleteServerRequest.DeleteAttachedVolumesOption.KeepAttachedVolumes;

public class SparkHTTPAdapter {
    private final DeleteServerUseCase useCase;

    public SparkHTTPAdapter(DeleteServerUseCase useCase) {
        this.useCase = useCase;
    }

    public void start() {
        Spark.port(8080);
        Spark.delete("/server/:serverId", (req, res) -> {
            final DeleteServerRequest deleteServerRequest = understandDeleteServerRequest(req);
            final DeleteServerResponse deleteServerResponse = this.useCase.deleteServer(deleteServerRequest);

            return switch (deleteServerResponse) {
                case DeleteServerResponse.DeletionRequestAccepted x -> {
                    res.status(200);
                    res.type("application/json");
                    yield JSONFormatter.formatAsJSON(x);
                }
                case DeleteServerResponse.CannotDeleteNonExistingServer x -> {
                    res.status(404);
                    yield "";
                }
            };
        });
    }

    private DeleteServerRequest understandDeleteServerRequest(Request req) {
        final ServerID serverId = new ServerID(UUID.fromString(req.params("serverId")));
        final DeleteServerRequest.DeleteAttachedVolumesOption keepAttachedVolumes =
                req.queryParamOrDefault("deleteVolumes", "false").equals("true") ?
                        DeleteAttachedVolumes :
                        KeepAttachedVolumes;
        return new DeleteServerRequest(serverId, keepAttachedVolumes);
    }
}