package ports.driving;

import domain.*;

public interface UseCase {
    DeleteServerResponse deleteServer(DeleteServerRequest deleteServerRequest);

    void attachVolumeToServer(ServerID server, VolumeID volume);

    Server createServer();

    Volume createVolume();
}
