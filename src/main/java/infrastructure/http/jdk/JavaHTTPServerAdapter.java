package infrastructure.http.jdk;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import domain.DeleteServerRequest;
import domain.DeleteServerResponse;
import domain.ServerID;
import infrastructure.http.JSONFormatter;
import ports.driving.DeleteServerUseCase;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static domain.DeleteServerRequest.DeleteAttachedVolumesOption;
import static domain.DeleteServerRequest.DeleteAttachedVolumesOption.DeleteAttachedVolumes;
import static domain.DeleteServerRequest.DeleteAttachedVolumesOption.KeepAttachedVolumes;
import static domain.DeleteServerResponse.CannotDeleteNonExistingServer;
import static domain.DeleteServerResponse.DeletionRequestAccepted;

public class JavaHTTPServerAdapter {

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
            handleDeleteServerRequest(exchange);
            exchange.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteServerRequest(HttpExchange exchange) throws IOException {
        final var request = understandRequest(exchange);
        final var response = useCase.deleteServer(request);
        sendResponse(exchange, response);
    }

    private void sendResponse(HttpExchange exchange, DeleteServerResponse deleteServerResponse) throws IOException {
        switch (deleteServerResponse) {
            case DeletionRequestAccepted r -> {
                exchange.getResponseHeaders().put("Content-Type", List.of("application/json"));
                exchange.sendResponseHeaders(200, 0);
                exchange.getResponseBody().write(JSONFormatter.formatAsJSON(r).getBytes());
            }
            case CannotDeleteNonExistingServer ignored -> exchange.sendResponseHeaders(404, 0);
        }
    }

    private DeleteServerRequest understandRequest(HttpExchange exchange) {
        final String[] split = exchange.getRequestURI().getPath().split("/server/");
        final ServerID serverID = new ServerID(UUID.fromString(split[1]));
        final DeleteAttachedVolumesOption deleteAttachedVolumes = keepOrDeleteAttachedVolumes(exchange);
        return new DeleteServerRequest(serverID, deleteAttachedVolumes);
    }

    private DeleteAttachedVolumesOption keepOrDeleteAttachedVolumes(HttpExchange exchange) {
        return Optional.ofNullable(exchange.getRequestURI().getQuery())
                .filter(x -> x.equals("deleteVolumes=true"))
                .map(x -> DeleteAttachedVolumes)
                .orElse(KeepAttachedVolumes);
    }

    void stop() {
        this.server.stop(0);
    }
}
