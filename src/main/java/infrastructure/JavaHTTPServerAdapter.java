package infrastructure;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import domain.DeleteServerRequest;
import domain.DeleteServerResponse;
import domain.ServerID;
import ports.driving.DeleteServerUseCase;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

class JavaHTTPServerAdapter {

    private final HttpServer server;
    private final DeleteServerUseCase useCase;

    public JavaHTTPServerAdapter(DeleteServerUseCase useCase) {
        this.useCase = useCase;
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.start();
        server.createContext("/server/", this::handleRequest);
    }

    private void handleRequest(HttpExchange exchange) {
        try {
            final DeleteServerRequest deleteServerRequest = parseRequest(exchange);
            final DeleteServerResponse deleteServerResponse = useCase.deleteServer(deleteServerRequest);

            switch (deleteServerResponse) {
                case DeleteServerResponse.DeletionRequestAccepted r -> {
                    exchange.getResponseHeaders().put("Content-Type", List.of("application/json"));
                    exchange.sendResponseHeaders(200, 0);
                    exchange.getResponseBody().write(formatAsJSON(r).getBytes());
                }
                case DeleteServerResponse.CannotDeleteNonExistingServer x -> {
                    exchange.sendResponseHeaders(404, 0);
                }
            }

            exchange.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private DeleteServerRequest parseRequest(HttpExchange exchange) {
        final String[] split = exchange.getRequestURI().getPath().split("/server/");
        final ServerID serverID = new ServerID(UUID.fromString(split[1]));
        final DeleteServerRequest.DeleteAttachedVolumesOption deleteAttachedVolumes =
                Optional.ofNullable(exchange.getRequestURI().getQuery())
                        .filter(x -> x.equals("deleteVolumes=true"))
                        .map(x -> DeleteServerRequest.DeleteAttachedVolumesOption.DeleteAttachedVolumes)
                        .orElse(DeleteServerRequest.DeleteAttachedVolumesOption.KeepAttachedVolumes);
        return new DeleteServerRequest(serverID, deleteAttachedVolumes);
    }

    private String formatAsJSON(DeleteServerResponse.DeletionRequestAccepted r) {
        final String ids = r.resourcesToDelete()
                .stream()
                .map(r_ -> "\"" + r_.id().toString() + "\"")
                .collect(Collectors.joining(",", "[", "]"));
        return """
                {"affectedResources": %s}
                """.formatted(ids);
    }

    void stop() {
        this.server.stop(0);
    }
}
