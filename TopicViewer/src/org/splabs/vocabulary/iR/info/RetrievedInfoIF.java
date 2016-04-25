package org.splabs.vocabulary.iR.info;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import java.util.List;
import java.util.Map;

public abstract interface RetrievedInfoIF
{
  public abstract DoubleMatrix2D getTermDocumentMatrix();

  public abstract List<String> getAllTerms();

  public abstract List<String> getAllDocumentIds();

  public abstract Map<String, Integer> getAllTermIdsMap();

  public abstract Map<String, Integer> getAllDocumentIdsMap();

  public abstract DoubleMatrix1D getDocumentVector(String paramString);

  public abstract DoubleMatrix1D getTermVector(String paramString);

  public abstract double getDistance(DoubleMatrix1D paramDoubleMatrix1D1, DoubleMatrix1D paramDoubleMatrix1D2);
}