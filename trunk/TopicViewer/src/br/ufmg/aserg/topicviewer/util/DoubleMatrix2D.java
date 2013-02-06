package br.ufmg.aserg.topicviewer.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;

public class DoubleMatrix2D implements Closeable {
	
    private static final int MAPPING_SIZE = 1 << 30;
    private final RandomAccessFile raf;
    private final List<MappedByteBuffer> mappings = new ArrayList<MappedByteBuffer>();

    private static int numInstances = 0;
    
    private int rows;
    private int columns;
    private String fileName;
    
    public DoubleMatrix2D(int rows, int columns) throws IOException {
    	this.fileName = File.createTempFile("temporarymatrix-" + numInstances, null).getAbsolutePath();
    	this.raf = new RandomAccessFile(this.fileName, "rw");
        try {
            setRows(rows); setColumns(columns);
            long size = 8L*(rows*columns + 1);
            
            for (long offset = 0; offset < size; offset += MAPPING_SIZE) {
                long size2 = Math.min(size - offset, MAPPING_SIZE);
                mappings.add(raf.getChannel().map(FileChannel.MapMode.READ_WRITE, offset, size2));
            }
            
            numInstances++;
        } catch (IOException e) {
            raf.close();
            throw e;
        }
    }
    
    public DoubleMatrix2D(String fileName) throws IOException {
    	this.raf = new RandomAccessFile(fileName, "rw");
        try {
            this.rows = this.getInt(0);
            this.columns = this.getInt(4);
            long size = 8L*(rows*columns + 1);
            for (long offset = 0; offset < size; offset += MAPPING_SIZE) {
                long size2 = Math.min(size - offset, MAPPING_SIZE);
                mappings.add(raf.getChannel().map(FileChannel.MapMode.READ_WRITE, offset, size2));
            }
        } catch (IOException e) {
            raf.close();
            throw e;
        }
    }
    
    private void setRows(int rows) {
    	setInt(0, rows);
    	this.rows = rows;
    }
    
    private void setColumns(int columns) {
    	setInt(4, columns);
    	this.columns = columns;
    }
    
    private int getInt(int pos) {
    	int mapN = (int) (pos / MAPPING_SIZE);
    	int offN = (int) (pos % MAPPING_SIZE);
    	return mappings.get(mapN).getInt(offN);
    }
    
    private void setInt(int pos, int value) {
    	int mapN = (int) (pos / MAPPING_SIZE);
        int offN = (int) (pos % MAPPING_SIZE);
        mappings.get(mapN).putDouble(offN, value);
    }
    
    protected long position(int x, int y) {
        return (long) y * this.columns + x + 1;
    }
    
    public int rows() {
    	return this.rows;
    }
    
    public int columns() {
    	return this.columns;
    }
    
    public double get(int x, int y) {
        assert x >= 0 && x < this.columns;
        assert y >= 0 && y < this.rows;
        long p = position(x, y) * 8;
        int mapN = (int) (p / MAPPING_SIZE);
        int offN = (int) (p % MAPPING_SIZE);
        return mappings.get(mapN).getDouble(offN);
    }
    
    public void set(int x, int y, double d) {
        assert x >= 0 && x < this.columns;
        assert y >= 0 && y < this.rows;
        long p = position(x, y) * 8;
        int mapN = (int) (p / MAPPING_SIZE);
        int offN = (int) (p % MAPPING_SIZE);
        mappings.get(mapN).putDouble(offN, d);
    }
    
    public DoubleMatrix1D viewRow(int index) {
    	DoubleMatrix1D row = new DenseDoubleMatrix1D(this.columns);
    	for (int i = 0; i < this.columns; i++)
    		row.set(i, this.get(index, i));
    	return row;
    }
    
    public DoubleMatrix1D viewColumn(int index) {
    	DoubleMatrix1D column = new DenseDoubleMatrix1D(this.rows);
    	for (int i = 0; i < this.rows; i++) 
    		column.set(i, this.get(i, index));
    	return column;
    }
    
    public DoubleMatrix2D copy() throws IOException {
    	DoubleMatrix2D copy = new DoubleMatrix2D(this.rows, this.columns);
    	for (int i = 0; i < this.rows; i++)
    		for (int j = 0; j < this.columns; j++)
    			copy.set(i, j, this.get(i, j));
    	return copy;
    }
    
    public void save(String fileName) throws IOException {
    	FileUtilities.copyFile(this.fileName, fileName);
    }
    
    @Override
    protected void finalize() throws Throwable {
    	super.finalize();
    	this.close();
    	
    	if (this.fileName.contains("temporarymatrix-"))
    		new File(this.fileName).delete();
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
//    	long start = System.nanoTime();
//    	final long used0 = usedMemory();
//    	DoubleMatrix2D matrix = new DoubleMatrix2D("ldm.test", 3000, 3000);
//    	for (int i = 0; i < matrix.width(); i++)
//    		matrix.set(i, i, i);
//    	for (int i = 0; i < matrix.width(); i++)
//            System.out.print(matrix.get(i, i) + " ");
//    	System.out.println(matrix.get(0, 2));
//    	long time = System.nanoTime() - start;
//    	final long used = usedMemory() - used0;
//    	if (used == 0)
//    		System.err.println("You need to use -XX:-UsedTLAB to see small changes in memory usage.");
//    	System.out.printf("Setting the diagonal took %,d ms, Heap used is %,d KB%n", time / 1000 / 1000, used / 1024);
//    	matrix.close();
	}
    
//    private static long usedMemory() {
//        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//    }
}