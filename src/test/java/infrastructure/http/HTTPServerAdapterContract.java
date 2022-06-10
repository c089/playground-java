package infrastructure.http;

import domain.DeleteServerRequest;
import domain.DeleteServerResponse;
import domain.ServerID;
import domain.VolumeID;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ports.driving.UseCase;

import java.util.Set;
import java.util.UUID;

import static domain.DeleteServerRequest.DeleteAttachedVolumesOption.KeepAttachedVolumes;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public abstract class HTTPServerAdapterContract {
    protected UseCase useCase;

    @Test
    void returns404WhenServerCannotBeFound() {
        ServerID serverId = new ServerID(UUID.randomUUID());
        when(useCase.deleteServer(any())).thenReturn(new DeleteServerResponse.CannotDeleteNonExistingServer(serverId));

        RestAssured
                .when()
                .delete("/server/" + serverId.id().toString())
                .then()
                .statusCode(is(equalTo(404)))
                .contentType(is(equalTo("application/json")))
                .body("error", is(equalTo("Server with id " + serverId.id() + " does not exist.")));
    }

    @Test
    void passesServerIDFromURLToUseCase() {
        ServerID serverId = new ServerID(UUID.randomUUID());
        when(useCase.deleteServer(any())).thenReturn(new DeleteServerResponse.CannotDeleteNonExistingServer(serverId));

        RestAssured
                .given()
                .when()
                .delete("/server/" + serverId.id().toString());

        verify(useCase).deleteServer(new DeleteServerRequest(serverId, KeepAttachedVolumes));
    }

    @Test
    void givenDeleteVolumesQueryParameterDeletesAllVolumes() {
        ServerID serverId = new ServerID(UUID.randomUUID());
        when(useCase.deleteServer(any()))
                .thenReturn(new DeleteServerResponse.CannotDeleteNonExistingServer(serverId));

        RestAssured
                .given()
                .when()
                .delete("/server/" + serverId.id().toString() + "?deleteVolumes=true");

        verify(useCase).deleteServer(new DeleteServerRequest(
                serverId,
                DeleteServerRequest.DeleteAttachedVolumesOption.DeleteAttachedVolumes
        ));
    }

    @Test
    void returns200AndListOfDeletedResourcesWhenServerIsScheduledForDeletion() {
        ServerID serverId = new ServerID(UUID.randomUUID());
        VolumeID volume1Id = new VolumeID(UUID.randomUUID());
        VolumeID volume2Id = new VolumeID(UUID.randomUUID());

        when(useCase.deleteServer(any()))
                .thenReturn(new DeleteServerResponse.DeletionRequestAccepted(Set.of(serverId, volume1Id, volume2Id)));

        RestAssured
                .given()
                .when()
                .delete("/server/" + serverId.id().toString())
                .then()
                .log().body()
                .statusCode(is(equalTo(200)))
                .contentType("application/json")
                .body("affectedResources", containsInAnyOrder(
                        serverId.id().toString(),
                        volume1Id.id().toString(),
                        volume2Id.id().toString()
                ));
    }

    @Test
    void givenUnknownQueryShouldRejectRequests() {
        ServerID serverId = new ServerID(UUID.randomUUID());

        RestAssured
                .when()
                .delete("/server/" + serverId.id() + "?nonsense=always")
                .then()
                .statusCode(is(equalTo(400)))
                .contentType("application/json")
                .body("error", is(equalTo("Found unexpected query parameter: \"nonsense\".")));
    }

    @BeforeEach
    void setUpUseCase() {
        useCase = mock(UseCase.class);
    }
}
