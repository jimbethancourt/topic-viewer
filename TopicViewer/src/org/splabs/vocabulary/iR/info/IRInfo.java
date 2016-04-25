package org.splabs.vocabulary.iR.info;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.splabs.vocabulary.iR.util.TermWeightTuple;

public class IRInfo extends RetrievedInfo
{
  public IRInfo(Map<String, List<TermWeightTuple>> tupleMap, Properties props)
  {
    super(tupleMap, props);
  }

  protected DoubleMatrix1D getVectorInSpace(DoubleMatrix1D vector) {
    return vector;
  }

  protected double[][] generateNewMap(int termsCount, int documentsCount)
  {
    return new double[termsCount][documentsCount];
  }
}