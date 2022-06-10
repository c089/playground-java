package infrastructure.http.jdk;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import domain.DeleteServerRequest;
import domain.DeleteServerResponse;
import domain.ServerID;
import infrastructure.http.DeleteServerHTTPRequestParseResult;
import infrastructure.http.JSONFormatter;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import ports.driving.UseCase;

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
import static infrastructure.http.DeleteServerHTTPRequestParseResult.InvalidRequest;
import static infrastructure.http.DeleteServerHTTPRequestParseResult.ValidRequest;

public class JavaHTTPServerAdapter {

    private final HttpServer server;
    private final UseCase useCase;
    private final JSONFormatter jsonFormatter = new JSONFormatter();

    public JavaHTTPServerAdapter(UseCase useCase) {
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
        switch (understandRequest(exchange)) {
            case ValidRequest r -> sendResponse(exchange, useCase.deleteServer(r.request()));
            case InvalidRequest invalidRequest -> sendBadRequestResponse(exchange, invalidRequest);
        }

    }

    private void sendResponse(HttpExchange exchange, DeleteServerResponse deleteServerResponse) throws IOException {
        switch (deleteServerResponse) {
            case DeletionRequestAccepted r -> {
                exchange.getResponseHeaders().put("Content-Type", List.of("application/json"));
                exchange.sendResponseHeaders(200, 0);
                exchange.getResponseBody().write(jsonFormatter.formatAsJSON(r).getBytes());
            }
            case CannotDeleteNonExistingServer r -> {
                exchange.getResponseHeaders().put("Content-Type", List.of("application/json"));
                exchange.sendResponseHeaders(404, 0);
                exchange.getResponseBody().write(jsonFormatter.formatAsJSON(r).getBytes());
            }
        }
    }

    private void sendBadRequestResponse(HttpExchange exchange, InvalidRequest r) throws IOException {
        exchange.getResponseHeaders().put("Content-Type", List.of("application/json"));
        exchange.sendResponseHeaders(400, 0);
        exchange.getResponseBody().write(jsonFormatter.formatAsJSON(r).getBytes());
    }

    private DeleteServerHTTPRequestParseResult understandRequest(HttpExchange exchange) {
        final String[] split = exchange.getRequestURI().getPath().split("/server/");
        final ServerID serverID = new ServerID(UUID.fromString(split[1]));
        final DeleteAttachedVolumesOption deleteAttachedVolumes = keepOrDeleteAttachedVolumes(exchange);
        final List<NameValuePair> queryParams = new URIBuilder(exchange.getRequestURI()).getQueryParams();
        return queryParams.stream()
                .filter(x -> !x.getName().equals("deleteVolumes")).findFirst().<DeleteServerHTTPRequestParseResult>map(x ->
                        new InvalidRequest("Found unexpected query parameter: \"%s\".".formatted(x.getName())))
                .orElseGet(() ->
                        new ValidRequest(new DeleteServerRequest(serverID, deleteAttachedVolumes)));
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
