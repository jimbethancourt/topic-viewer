package org.splabs.vocabulary.iR;

public class IRPropertyKeys
{
  public static final String IR_FUNCTION_TYPE = "irFunctionType";
  public static final String TERM_FREQUENCY_VARIANT_TYPE = "tfVariant";
  public static final String SCORE_CALCULATOR_TYPE = "scoreCalculator";
  public static final String DISTANCE_FUNCTION = "distanceFunction";
  public static final String LSI_LOW_RANK_VALUE = "lowRankValue";

  public static enum DistanceFunctionType
  {
    COSINE, 
    EUCLIDEAN, 
    BRAY_CURTIS, 
    CANBERRA, 
    TANIMOTO;
  }

  public static enum IRFunctionType
  {
    IR, 
    LSI;
  }

  public static enum ScoreCalculatorType
  {
    TF, 
    IDF, 
    TFIDF;
  }

  public static enum TermFrequencyVariant
  {
    ABSOLUTE, 
    RELATIVE;
  }
}