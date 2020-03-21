package io.luna.game.cache;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;

public final class CacheLoader {

    private final Path dir;
    private final ListeningExecutorService loader;
    private FileChannel[] indexFiles;
    private FileChannel dataFile;

    /**
     * A list of cache decoders.
     */
    private final List<CacheDecoder> decoders = new ArrayList<>();

    public CacheLoader(Path dir) {
        this.dir = Objects.requireNonNull(dir);
        loader = buildLoadingExecutor();
    }

    public void addDecoder(CacheDecoder decoder) {
        decoders.add(decoder);
    }

    public ListenableFuture<Duration> load() {
        return loader.submit(() -> {
            var timer = Stopwatch.createStarted();
            int indexSize = checkDir();
            try(Cache loadedCache = loadDir(indexSize)) {
                for (CacheDecoder nextDecoder : decoders) {
                    nextDecoder.decode(loadedCache);
                }
            }
            return timer.elapsed();
        });
    }

    private int checkDir() throws Exception {
        if (!Files.exists(dir)) {
            throw new FileNotFoundException("Specified cache location does not exist.");
        }
        int size = 0;
        for (int index = 0; index < indexFiles.length; index++) {
            Path indexFileDir = dir.resolve(CacheConstants.INDEX_FILE_NAME + index);
            if (!Files.exists(indexFileDir)) {
                break;
            } else {
                size++;
            }
        }
        if (size == 0) {
            throw new FileNotFoundException("No index files found.");
        }
        Path dataFileDir = dir.resolve(CacheConstants.DATA_FILE_NAME);
        if (!Files.exists(dataFileDir)) {
            throw new FileNotFoundException("No data file found.");
        }
        return size;
    }

    private Cache loadDir(int indexSize) throws IOException {
        var options = Set.of(StandardOpenOption.READ, StandardOpenOption.WRITE);
        dataFile = FileChannel.open(dir.resolve(CacheConstants.DATA_FILE_NAME), options);
        indexFiles = new FileChannel[indexSize];
        for (int index = 0; index < indexFiles.length; index++) {
            indexFiles[index] = FileChannel.open(dir.resolve(CacheConstants.INDEX_FILE_NAME + index), options);
        }
        return new Cache(ImmutableList.copyOf(indexFiles), dataFile);
    }

    private ListeningExecutorService buildLoadingExecutor() {
        var threadFactory = new ThreadFactoryBuilder().setNameFormat("CacheLoaderThread").build();
        var executorService = Executors.newSingleThreadExecutor(threadFactory);
        return MoreExecutors.listeningDecorator(executorService);
    }
}
