package domain;

public record DeleteServerRequest(ServerID serverID, DeleteAttachedVolumesOption deleteAttachedVolumes) {
    public enum DeleteAttachedVolumesOption {
        DeleteAttachedVolumes, KeepAttachedVolumes;
    }
}