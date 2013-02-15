package br.ufmg.aserg.topicviewer.util;

import java.io.IOException;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

public class DoubleMatrix2DTest {

	private static final double[][] TEST = {
			{ 0.006652,  0.062288,  0.056997,  0.002963,  0.001462,  0.001462,  0.001462,  0.156192,  0.005917,  0.01177 ,  0.000731,  0.002116,  0.018257,  0.000731,  0.004282,  0.022424,  0.008709,  0.015535,  0.005469,  0.019451,  0.014536,  0.016686,  0.006476,  0.000704},
			{-0.050817, -0.390273, -0.137669, -0.004709, -0.007937, -0.007937, -0.007937,  0.094398, -0.011059, -0.000914, -0.003969, -0.008186, -0.163168, -0.003969, -0.010395, -0.013445, -0.060997, -0.010042, -0.005956, -0.005815, -0.029643, -0.001956, -0.002136,  7.045956E-005},
			{ 0.026603,  0.19396,  -0.013088, -0.002941,  0.005071,  0.005071,  0.005071,  0.152611, -0.004629,  0.001402,  0.002536,  0.004315,  0.092723,  0.002536,  0.002522, -0.003591,  0.03188 , -0.016507, -0.00595 , -0.005112, -0.035607, -0.006289,  0.006994, -0.000364},
			{-0.102611, -0.155967, -0.150152, -0.004902, -0.017216, -0.017216, -0.017216, -0.180444, -0.015306, -0.011264, -0.008608, -0.016824, -0.335599, -0.008608, -0.010231, -0.027579, -0.123945,  0.005042, -0.004663, -0.010825, -0.011814, -0.005877,  0.009333, -1.981476E-005},
			{ 0.06016,   0.136563,  0.017989,  0.005599,  0.007083,  0.007083,  0.007083, -0.373879, -0.002924, -0.008916,  0.003542,  0.005846,  0.198934,  0.003542,  0.009908, -0.025061,  0.067265,  0.025339,  0.004441, -0.030236, -0.031105, -0.019295,  0.00414 , -0.000416},
			{-0.060612,  0.053503,  0.032354, -0.010405, -0.011535, -0.011535, -0.011535, -0.252515, -0.004089,  0.005042, -0.005767, -0.010207, -0.215193, -0.005767,  0.00522 , -0.001786, -0.078284, -0.005539, -0.014389,  0.021663,  0.092244,  0.020056,  0.0095  ,  0.000625},
			{ 0.040452, -0.079861,  0.022501,  0.001135,  0.004293,  0.004293,  0.004293, -0.163253, -0.004407,  0.015237,  0.002147,  0.003753,  0.128095,  0.002147, -0.008281, -0.025778,  0.043527,  0.011941, -0.003726, -0.018801,  0.093333,  0.020026,  0.010108,  0.002437}};
	
	public static void main(String[] args) throws IOException {
		DoubleMatrix2D coltMatrix = new DenseDoubleMatrix2D(TEST);
		
		int rows = coltMatrix.rows();
		int columns = coltMatrix.columns();
		
		br.ufmg.aserg.topicviewer.util.DoubleMatrix2D myMatrix = new br.ufmg.aserg.topicviewer.util.DoubleMatrix2D(coltMatrix);
		
		if (rows != myMatrix.rows()) System.err.println("different row number");
		if (columns != myMatrix.columns()) System.err.println("different column number");
		
		for (int i = 0; i < myMatrix.rows(); i++)
			for (int j = 0; j < myMatrix.columns(); j++)
				if (coltMatrix.get(i, j) != myMatrix.get(i, j))
					System.err.println("different values: " + coltMatrix.get(i, j) + " " + myMatrix.get(i, j));
		
		myMatrix.save("C:\\Users\\admin\\Downloads\\matrixTest.matrix");
		myMatrix = new br.ufmg.aserg.topicviewer.util.DoubleMatrix2D("C:\\Users\\admin\\Downloads\\matrixTest.matrix");
		
		if (rows != myMatrix.rows()) System.err.println("different row number from file");
		if (columns != myMatrix.columns()) System.err.println("different column number from file");
		
		for (int i = 0; i < myMatrix.rows(); i++)
			for (int j = 0; j < myMatrix.columns(); j++)
				if (coltMatrix.get(i, j) != myMatrix.get(i, j))
					System.err.println("different values from file: " + coltMatrix.get(i, j) + " " + myMatrix.get(i, j));
				else System.out.println("ok");
	}
}
