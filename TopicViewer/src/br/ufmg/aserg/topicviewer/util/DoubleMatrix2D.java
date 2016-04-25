package br.ufmg.aserg.topicviewer.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;
import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;

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
            long size = 8L*(rows*columns + 1);
            
            for (long offset = 0; offset < size; offset += MAPPING_SIZE) {
                long size2 = Math.min(size - offset, MAPPING_SIZE);
                mappings.add(raf.getChannel().map(FileChannel.MapMode.READ_WRITE, offset, size2));
            }
            
            setRows(rows);
            setColumns(columns);
            numInstances++;
        } catch (IOException e) {
            raf.close();
            throw e;
        }
    }
    
    public DoubleMatrix2D(String fileName) throws IOException {
    	this.raf = new RandomAccessFile(fileName, "rw");
        try {
        	this.rows = this.raf.readInt();
            this.columns = this.raf.readInt();
            this.raf.seek(0L);
            
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
    
    public DoubleMatrix2D(cern.colt.matrix.tdouble.DoubleMatrix2D matrix) throws IOException {
    	this(matrix.rows(), matrix.columns());
    	for (int i = 0; i < matrix.rows(); i++)
    		for (int j = 0; j < matrix.columns(); j++)
    			this.set(i, j, matrix.get(i, j));
    }
    
    private void setRows(int rows) {
    	setInt(0, rows);
    	this.rows = rows;
    }
    
    private void setColumns(int columns) {
    	setInt(4, columns);
    	this.columns = columns;
    }
    
    private void setInt(int pos, int value) {
    	int mapN = (int) (pos / MAPPING_SIZE);
        int offN = (int) (pos % MAPPING_SIZE);
        mappings.get(mapN).putInt(offN, value);
    }
    
    protected long position(int x, int y) {
        return (long) (x*this.columns) + y + 1;
    }
    
    public int rows() {
    	return this.rows;
    }
    
    public int columns() {
    	return this.columns;
    }
    
    public double get(int x, int y) {
        assert x >= 0 && x < this.rows;
        assert y >= 0 && y < this.columns;
        long p = position(x, y) * 8;
        int mapN = (int) (p / MAPPING_SIZE);
        int offN = (int) (p % MAPPING_SIZE);
        return mappings.get(mapN).getDouble(offN);
    }
    
    public void set(int x, int y, double d) {
        assert x >= 0 && x < this.rows;
        assert y >= 0 && y < this.columns;
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
    
	public static cern.colt.matrix.tdouble.DoubleMatrix2D copy(DoubleMatrix2D lsiTransform) {
		cern.colt.matrix.tdouble.DoubleMatrix2D matrix = new DenseDoubleMatrix2D(lsiTransform.rows(), lsiTransform.columns());
		for (int i = 0; i < lsiTransform.rows(); i++)
    		for (int j = 0; j < lsiTransform.columns(); j++) 
    			matrix.set(i, j, lsiTransform.get(i, j));
		return matrix;
	}
    
    public void save(String fileName) throws IOException {
    	this.close();
    	
    	InputStream in = new FileInputStream(this.fileName);
    	OutputStream out = new FileOutputStream(fileName);
    	byte[] buf = new byte[1024];
    	int len;
    	while ((len = in.read(buf)) > 0)
    	   out.write(buf, 0, len);
    	in.close();
    	out.close();

    	new File(this.fileName).delete();
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
    	
    	DoubleMatrix2D matrix = new DoubleMatrix2D(2, 20);
    	for (int i = 0; i < matrix.rows(); i++)
    		for (int j = 0; j < matrix.columns(); j++)
    			matrix.set(i, j, i+j);
    	
    	String fileName = "C:\\Users\\admin\\test.matrix";
    	matrix.save(fileName);
    	
    	matrix = new DoubleMatrix2D(fileName);
    	for (int i = 0; i < matrix.rows(); i++)
    		for (int j = 0; j < matrix.columns(); j++)
    			if (matrix.get(i, j) != i+j)
    				System.err.println("t� errado, amigo");
    	
    	DoubleMatrix1D column = matrix.viewColumn(0);
    	for (int j = 0; j < column.size(); j++)
			if (column.get(j) != j)
				System.err.println("t� errada a coluna, amigo");
    	
    	DoubleMatrix1D row = matrix.viewRow(0);
    	for (int j = 0; j < row.size(); j++)
			if (row.get(j) != j)
				System.err.println("t� errada a linha, amigo");
    	
	}
}