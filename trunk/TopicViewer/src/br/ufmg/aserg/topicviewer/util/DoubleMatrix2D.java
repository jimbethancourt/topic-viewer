package br.ufmg.aserg.topicviewer.util;

import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class DoubleMatrix2D implements Closeable {
    private static final int MAPPING_SIZE = 1 << 30;
    private final RandomAccessFile raf;
    private final int width;
    private final int height;
    private final List<MappedByteBuffer> mappings = new ArrayList<MappedByteBuffer>();

    public DoubleMatrix2D(String filename, int width, int height) throws IOException {
    	// colocar int no cabeçalho
        this.raf = new RandomAccessFile(filename, "rw");
        try {
            this.width = width;
            this.height = height;
            long size = 8L * width * height;
            for (long offset = 0; offset < size; offset += MAPPING_SIZE) {
                long size2 = Math.min(size - offset, MAPPING_SIZE);
                mappings.add(raf.getChannel().map(FileChannel.MapMode.READ_WRITE, offset, size2));
            }
        } catch (IOException e) {
            raf.close();
            throw e;
        }
    }

    protected long position(int x, int y) {
        return (long) y * width + x;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double get(int x, int y) {
        assert x >= 0 && x < width;
        assert y >= 0 && y < height;
        long p = position(x, y) * 8;
        int mapN = (int) (p / MAPPING_SIZE);
        int offN = (int) (p % MAPPING_SIZE);
        return mappings.get(mapN).getDouble(offN);
    }

    public void set(int x, int y, double d) {
        assert x >= 0 && x < width;
        assert y >= 0 && y < height;
        long p = position(x, y) * 8;
        int mapN = (int) (p / MAPPING_SIZE);
        int offN = (int) (p % MAPPING_SIZE);
        mappings.get(mapN).putDouble(offN, d);
    }

    public void close() throws IOException {
        for (MappedByteBuffer mapping : mappings)
            clean(mapping);
        raf.close();
    }

    private void clean(MappedByteBuffer mapping) {
        if (mapping == null) return;
        Cleaner cleaner = ((DirectBuffer) mapping).cleaner();
        if (cleaner != null) cleaner.clean();
    }
    
    public static void main(String[] args) throws IOException {
    	long start = System.nanoTime();
    	final long used0 = usedMemory();
    	DoubleMatrix2D matrix = new DoubleMatrix2D("ldm.test", 3000, 3000);
    	for (int i = 0; i < matrix.width(); i++)
    		matrix.set(i, i, i);
    	for (int i = 0; i < matrix.width(); i++)
            System.out.print(matrix.get(i, i) + " ");
    	long time = System.nanoTime() - start;
    	final long used = usedMemory() - used0;
    	if (used == 0)
    		System.err.println("You need to use -XX:-UsedTLAB to see small changes in memory usage.");
    	System.out.printf("Setting the diagonal took %,d ms, Heap used is %,d KB%n", time / 1000 / 1000, used / 1024);
    	matrix.close();
	}
    
    private static long usedMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}
