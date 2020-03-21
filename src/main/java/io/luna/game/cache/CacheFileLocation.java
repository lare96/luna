package io.luna.game.cache;

public class CacheFileLocation {
    private final int typeId;
    private final int fileId;

    public CacheFileLocation(int typeId, int fileId) {
        this.typeId = typeId;
        this.fileId = fileId;
    }

    public int getTypeId() {
        return typeId;
    }

}
