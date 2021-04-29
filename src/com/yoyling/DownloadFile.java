package com.yoyling;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicLong;

class DownloadFile {
    private final RandomAccessFile file;
    private final FileChannel channel; // 线程安全类
    private AtomicLong wroteSize; // 已写入的长度

    DownloadFile(String fileName, long fileSize) throws IOException {
        this.wroteSize = new AtomicLong(0);
        this.file = new RandomAccessFile(fileName, "rw");
        file.setLength(fileSize);
        channel = file.getChannel();
    }

    /**
     * 写数据
     * @param offset 写偏移量
     * @param buffer 数据
     * @throws IOException 写数据出现异常
     */
    void write(long offset, ByteBuffer buffer, int threadID, long upperBound) throws IOException {
        buffer.flip();
        int length = buffer.limit();
        while (buffer.hasRemaining()) {
            channel.write(buffer, offset);
        }
        wroteSize.addAndGet(length);
    }

    /**
     * @return 已经下载的数据量，为了知道何时结束整个任务，以及统计信息
     */
    long getWroteSize() {
        return wroteSize.get();
    }

    void close() {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
