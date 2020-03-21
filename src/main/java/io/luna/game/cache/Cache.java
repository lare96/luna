package io.luna.game.cache;

import com.google.common.collect.ImmutableList;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.FileChannel;

public final class Cache implements Closeable {

    private final ImmutableList<FileChannel> indexFiles;
    private final FileChannel dataFile;

    public Cache(ImmutableList<FileChannel> indexFiles, FileChannel dataFile) {
        this.indexFiles = indexFiles;
        this.dataFile = dataFile;
    }

    @Override
    public void close() throws IOException {
        dataFile.close();
        for(FileChannel nextChannel : indexFiles) {
            nextChannel.close();
        }
    }

    public ImmutableList<FileChannel> getIndexFiles() {
        return indexFiles;
    }

    public FileChannel getDataFile() {
        return dataFile;
    }

}
