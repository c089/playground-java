package ports.driving;

import domain.DeleteServerRequest;
import domain.DeleteServerResponse;

public interface DeleteServerUseCase {
    DeleteServerResponse deleteServer(DeleteServerRequest deleteServerRequest);
}
