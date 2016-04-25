package org.splabs.vocabulary.iR.info;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra;
import cern.colt.matrix.tdouble.algo.DoubleStatistic;
import cern.colt.matrix.tdouble.impl.SparseDoubleMatrix2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.splabs.vocabulary.iR.IRPropertyKeys;
import org.splabs.vocabulary.iR.util.TermWeightTuple;

public abstract class RetrievedInfo
  implements RetrievedInfoIF
{
  protected Map<String, Integer> allTerms;
  protected Map<String, Integer> allDocumentIds;
  protected Map<String, List<TermWeightTuple>> tupleMap;
  protected DoubleMatrix2D termDocumentMatrix;
  private IRPropertyKeys.DistanceFunctionType distanceFunctionType;

  public RetrievedInfo(Map<String, List<TermWeightTuple>> tupleMap, Properties props)
  {
    this.tupleMap = tupleMap;
    validateProperties(props);
    updateTermDocumentMatrix();
  }

  public DoubleMatrix2D getTermDocumentMatrix() {
    return termDocumentMatrix;
  }

  public List<String> getAllTerms() {
    String[] allTermsArray = new String[allTerms.keySet().size()];
    for (String term : allTerms.keySet())
      allTermsArray[((Integer)allTerms.get(term)).intValue()] = term;
    return Arrays.asList(allTermsArray);
  }

  public List<String> getAllDocumentIds() {
    String[] allDocIdsArray = new String[allDocumentIds.keySet().size()];
    for (String docId : allDocumentIds.keySet())
      allDocIdsArray[((Integer)allDocumentIds.get(docId)).intValue()] = docId;
    return Arrays.asList(allDocIdsArray);
  }

  public Map<String, Integer> getAllTermIdsMap() {
    return allTerms;
  }

  public Map<String, Integer> getAllDocumentIdsMap() {
    return allDocumentIds;
  }

  public DoubleMatrix1D getDocumentVector(String docId) {
    DoubleMatrix1D weightVector = termDocumentMatrix.viewColumn(((Integer)allDocumentIds.get(docId)).intValue());
    return getVectorInSpace(weightVector);
  }

  public DoubleMatrix1D getTermVector(String term) {
    DoubleMatrix1D termVector = termDocumentMatrix.viewRow(((Integer)allTerms.get(term)).intValue());
    return termVector;
  }

  protected final void updateTermDocumentMatrix()
  {
    allTerms = generateAllTerms();
    allDocumentIds = new HashMap();

    int documentCount = tupleMap.keySet().size();
    double[][] array2D = generateNewMap(allTerms.size(), documentCount);

    int docIndex = 0;
    for (String docId : tupleMap.keySet())
    {
      allDocumentIds.put(docId, new Integer(docIndex));

      List<TermWeightTuple> tuples = tupleMap.get(docId);

      for (TermWeightTuple tuple : tuples) {
        array2D[((Integer)allTerms.get(tuple.getTerm())).intValue()][docIndex] = tuple.getWeight();
      }
      docIndex++;
    }

    termDocumentMatrix = new SparseDoubleMatrix2D(array2D);
  }

  private Map<String, Integer> generateAllTerms() {
    int termIndex = 0;
    Map<String, Integer> allTerms = new HashMap<>();

    for (String docId : this.tupleMap.keySet()) {

      for (TermWeightTuple tuple : tupleMap.get(docId)) {
        String term = tuple.getTerm();
        if (!allTerms.containsKey(term)) {
          allTerms.put(term, termIndex);
          termIndex++;
        }
      }
    }

    return allTerms;
  }

  protected abstract double[][] generateNewMap(int paramInt1, int paramInt2);

  protected abstract DoubleMatrix1D getVectorInSpace(DoubleMatrix1D paramDoubleMatrix1D);

  public double getDistance(DoubleMatrix1D docVector1, DoubleMatrix1D docVector2)
  {
    if (distanceFunctionType.toString().equals(IRPropertyKeys.DistanceFunctionType.BRAY_CURTIS.toString()))
      return getBrayCurtisDistance(docVector1, docVector2);
    if (distanceFunctionType.toString().equals(IRPropertyKeys.DistanceFunctionType.CANBERRA.toString()))
      return getCanberraDistance(docVector1, docVector2);
    if (distanceFunctionType.toString().equals(IRPropertyKeys.DistanceFunctionType.COSINE.toString()))
      return getCosineSimilarity(docVector1, docVector2);
    if (distanceFunctionType.toString().equals(IRPropertyKeys.DistanceFunctionType.EUCLIDEAN.toString()))
      return getEuclideanDistance(docVector1, docVector2);
    if (distanceFunctionType.toString().equals(IRPropertyKeys.DistanceFunctionType.TANIMOTO.toString())) {
      return getTanimotoDistance(docVector1, docVector2);
    }
    return 0.0D;
  }

  private double getCosineSimilarity(DoubleMatrix1D docVector1, DoubleMatrix1D docVector2) {
    double cosineSimilarity = docVector1.zDotProduct(docVector2);
    cosineSimilarity /= Math.sqrt(docVector1.zDotProduct(docVector1) * docVector2.zDotProduct(docVector2));
    return cosineSimilarity;
  }

  private double getEuclideanDistance(DoubleMatrix1D docVector1, DoubleMatrix1D docVector2) {
    DoubleStatistic.VectorVectorFunction euclideanFunction = DoubleStatistic.EUCLID;
    return euclideanFunction.apply(docVector1, docVector2);
  }

  private double getCanberraDistance(DoubleMatrix1D docVector1, DoubleMatrix1D docVector2) {
    DoubleStatistic.VectorVectorFunction euclideanFunction = DoubleStatistic.CANBERRA;
    return euclideanFunction.apply(docVector1, docVector2);
  }

  private double getBrayCurtisDistance(DoubleMatrix1D docVector1, DoubleMatrix1D docVector2) {
    DoubleStatistic.VectorVectorFunction euclideanFunction = DoubleStatistic.BRAY_CURTIS;
    return euclideanFunction.apply(docVector1, docVector2);
  }

  private double getTanimotoDistance(DoubleMatrix1D docVector1, DoubleMatrix1D docVector2) {
    double dotProduct = docVector1.zDotProduct(docVector2);

    DenseDoubleAlgebra algebra = new DenseDoubleAlgebra();
    double docVector1Magnitude = algebra.norm2(docVector1);
    double docVector2Magnitude = algebra.norm2(docVector2);

    return dotProduct / (Math.pow(docVector1Magnitude, 2.0D) + Math.pow(docVector2Magnitude, 2.0D) - dotProduct);
  }

  private void validateProperties(Properties props)
  {
    if (props.containsKey("distanceFunction")) {
      String distanceFunctionTypeProp = props.getProperty("distanceFunction");

      if (distanceFunctionTypeProp.equalsIgnoreCase(IRPropertyKeys.DistanceFunctionType.COSINE.toString()))
        distanceFunctionType = IRPropertyKeys.DistanceFunctionType.COSINE;
      else if (distanceFunctionTypeProp.equalsIgnoreCase(IRPropertyKeys.DistanceFunctionType.EUCLIDEAN.toString()))
        distanceFunctionType = IRPropertyKeys.DistanceFunctionType.EUCLIDEAN;
      else if (distanceFunctionTypeProp.equalsIgnoreCase(IRPropertyKeys.DistanceFunctionType.BRAY_CURTIS.toString()))
        distanceFunctionType = IRPropertyKeys.DistanceFunctionType.BRAY_CURTIS;
      else if (distanceFunctionTypeProp.equalsIgnoreCase(IRPropertyKeys.DistanceFunctionType.CANBERRA.toString()))
        distanceFunctionType = IRPropertyKeys.DistanceFunctionType.CANBERRA;
      else if (distanceFunctionTypeProp.equalsIgnoreCase(IRPropertyKeys.DistanceFunctionType.TANIMOTO.toString()))
        distanceFunctionType = IRPropertyKeys.DistanceFunctionType.TANIMOTO;
      else distanceFunctionType = IRPropertyKeys.DistanceFunctionType.COSINE;
    }
  }
}