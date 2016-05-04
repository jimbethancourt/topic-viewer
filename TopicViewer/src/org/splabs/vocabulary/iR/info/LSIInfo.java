package org.splabs.vocabulary.iR.info;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra;
import cern.colt.matrix.tdouble.algo.decomposition.DenseDoubleSingularValueDecomposition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.splabs.vocabulary.iR.util.TermWeightTuple;

public class LSIInfo extends RetrievedInfo
{
  private DoubleMatrix2D lsiTermDocumentMatrix;
  protected DoubleMatrix2D lsiTransform;
  private int lowRank = 0;
  private boolean makeSparse = false;

  public LSIInfo(Map<String, List<TermWeightTuple>> tupleMap, Properties props)
  {
    super(tupleMap, props);

    validateProperties(props);
    generateLowRankApproximation();
  }

  public LSIInfo(String[] terms, String[] documents, DoubleMatrix2D termDocumentMatrix, Properties props) {
    super(new HashMap(), props);

    validateProperties(props);
    this.termDocumentMatrix = termDocumentMatrix;

    allTerms = new HashMap();
    for (int i = 0; i < terms.length; i++) {
      allTerms.put(terms[i], Integer.valueOf(i));
    }
    allDocumentIds = new HashMap();
    for (int i = 0; i < documents.length; i++) {
      allDocumentIds.put(documents[i], Integer.valueOf(i));
    }
    generateLowRankApproximation();
  }

  public DoubleMatrix2D getLsiTermDocumentMatrix()
  {
    return lsiTermDocumentMatrix;
  }

  public DoubleMatrix2D getLsiTransformMatrix()
  {
    return lsiTransform;
  }

  public DoubleMatrix1D getDocumentVector(String docId)
  {
    return lsiTermDocumentMatrix.viewColumn(((Integer)allDocumentIds.get(docId)).intValue());
  }

  public DoubleMatrix1D getVectorInSpace(DoubleMatrix1D weightVector)
  {
    DenseDoubleAlgebra matrixAlgebra = new DenseDoubleAlgebra();
    DoubleMatrix1D lsiWeightVector = matrixAlgebra.mult(lsiTransform, weightVector);
    return lsiWeightVector;
  }

  protected double[][] generateNewMap(int termsCount, int documentsCount)
  {
    if (termsCount < documentsCount)
      return new double[documentsCount][documentsCount];
    return new double[termsCount][documentsCount];
  }

  private void generateLowRankApproximation()
  {
    DoubleMatrix2D termDocumentMatrix = ensureRectangular();
    DenseDoubleSingularValueDecomposition svd = new DenseDoubleSingularValueDecomposition(termDocumentMatrix, true, false);

    DoubleMatrix2D u = svd.getU();
    DoubleMatrix2D s = svd.getS();

    if (lowRank == 0) {
      lowRank = ((int)Math.pow(this.termDocumentMatrix.rows() * this.termDocumentMatrix.columns(), 0.2D));
    }
    DenseDoubleAlgebra matrixOperations = new DenseDoubleAlgebra();

    DoubleMatrix2D uPrime = matrixOperations.subMatrix(u, 0, u.rows() - 1, 0, lowRank - 1);
    DoubleMatrix2D sPrime = matrixOperations.subMatrix(s, 0, lowRank - 1, 0, lowRank - 1);

    DoubleMatrix2D sPrimeInverse = matrixOperations.inverse(sPrime);
    DoubleMatrix2D uPrimeTranspose = matrixOperations.transpose(uPrime);

    lsiTransform = matrixOperations.mult(sPrimeInverse, uPrimeTranspose);

    lsiTermDocumentMatrix = matrixOperations.mult(lsiTransform, termDocumentMatrix);

    if(makeSparse) {
      DoubleMatrix2D sparseMatrix = new SparseDoubleMatrix2D(lsiTermDocumentMatrix.rows(), lsiTermDocumentMatrix.columns());
      sparseMatrix.assign(lsiTermDocumentMatrix);
      lsiTermDocumentMatrix = sparseMatrix;
    }
  }

  private DoubleMatrix2D ensureRectangular()
  {
    if (termDocumentMatrix.rows() < termDocumentMatrix.columns()) {
      makeSparse = true;
      int bounds = termDocumentMatrix.columns();
      DoubleMatrix2D newMatrix = new DenseDoubleMatrix2D(bounds, bounds);

      for (int i = 0; i < termDocumentMatrix.rows(); i++) {
        for (int j = 0; j < termDocumentMatrix.columns(); j++)
          newMatrix.set(i, j, termDocumentMatrix.get(i, j));
      }
      return newMatrix;
    }

    return termDocumentMatrix;
  }

  private void validateProperties(Properties props)
  {
    if (props.containsKey("lowRankValue")) {
      String lowRankProp = props.getProperty("lowRankValue");
      lowRank = Integer.parseInt(lowRankProp);
    }
  }
}