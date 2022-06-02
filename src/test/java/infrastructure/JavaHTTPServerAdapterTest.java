package infrastructure;

import domain.DeleteServerRequest;
import domain.ServerID;
import domain.VolumeID;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ports.driving.DeleteServerUseCase;

import java.util.List;
import java.util.UUID;

import static domain.DeleteServerRequest.DeleteAttachedVolumesOption.KeepAttachedVolumes;
import static domain.DeleteServerResponse.CannotDeleteNonExistingServer;
import static domain.DeleteServerResponse.DeletionRequestAccepted;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JavaHTTPServerAdapterTest {

    private JavaHTTPServerAdapter httpApplication;
    private DeleteServerUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = mock(DeleteServerUseCase.class);
        httpApplication = new JavaHTTPServerAdapter(useCase);
    }

    @AfterEach
    void stop() {
        httpApplication.stop();
    }

    @Test
    void returns404WhenServerCannotBeFound() {
        ServerID serverId = new ServerID(UUID.randomUUID());
        when(useCase.deleteServer(any())).thenReturn(new CannotDeleteNonExistingServer(serverId));

        RestAssured
                .given()
                .when()
                .delete("/server/" + serverId.id().toString())
                .then()
                .statusCode(is(equalTo(404)));
    }

    @Test
    void passesServerIDFromURLToUseCase() {
        ServerID serverId = new ServerID(UUID.randomUUID());
        when(useCase.deleteServer(any())).thenReturn(new CannotDeleteNonExistingServer(serverId));

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
                .thenReturn(new CannotDeleteNonExistingServer(serverId));

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
                .thenReturn(new DeletionRequestAccepted(List.of(serverId, volume1Id, volume2Id)));

        RestAssured
                .given()
                .when()
                .delete("/server/" + serverId.id().toString())
                .then()
                .statusCode(is(equalTo(200)))
                .body("affectedResources", is(equalTo(List.of(
                        serverId.id().toString(),
                        volume1Id.id().toString(),
                        volume2Id.id().toString()
                ))));
    }

}
