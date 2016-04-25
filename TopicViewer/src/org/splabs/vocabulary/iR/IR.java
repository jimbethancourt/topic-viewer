package org.splabs.vocabulary.iR;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.splabs.vocabulary.iR.util.TermWeightTuple;

public class IR
{
  private Map<String, Map<String, Integer>> documentTermMap;
  private IRPropertyKeys.ScoreCalculatorType calculatorType;
  private boolean isTFRelative;

  public IR(Map<String, Map<String, Integer>> documentTermMap, Properties properties)
  {
    this.documentTermMap = documentTermMap;
    loadProperties(properties);
  }

  public Map<String, List<TermWeightTuple>> calculate()
  {
    Map<String, List<TermWeightTuple>> idfMap = calculateIDFMap();
    Map<String, List<TermWeightTuple>> tfMap = calculateTFMap();

    if (isCalculator(IRPropertyKeys.ScoreCalculatorType.TF)) return tfMap;
    if (isCalculator(IRPropertyKeys.ScoreCalculatorType.IDF)) return idfMap;

    Map tfIdfMap = new HashMap();

    for (String identifier : idfMap.keySet()) {
      List<TermWeightTuple> tfs = tfMap.get(identifier);
      List<TermWeightTuple> idfs = idfMap.get(identifier);
      List tfIdfs = new LinkedList();

      for (TermWeightTuple idfTuple : idfs) {
        TermWeightTuple tfTuple = getTuple(idfTuple.getTerm(), tfs);
        TermWeightTuple merged = new TermWeightTuple(idfTuple.getTerm(), tfTuple.getWeight() * idfTuple.getWeight());
        tfIdfs.add(merged);
      }

      tfIdfMap.put(identifier, tfIdfs);
    }

    return tfIdfMap;
  }

  private Map<String, List<TermWeightTuple>> calculateIDFMap() {
    Map<String, List<TermWeightTuple>> idfMap = null;
    if ((isCalculator(IRPropertyKeys.ScoreCalculatorType.IDF)) || (isCalculator(IRPropertyKeys.ScoreCalculatorType.TFIDF))) {
      int numDocuments = this.documentTermMap.size();
      idfMap = new HashMap<>();
      Map<String, Double> idfTermsMap = new HashMap<>();

      for (String documentId : documentTermMap.keySet()) {
        for (String termWeights : documentTermMap.get(documentId).keySet()) {
          if (!idfTermsMap.containsKey(termWeights)) {
            idfTermsMap.put(termWeights, 0.0D);
          }
        }
      }

      for (String documentId : idfTermsMap.keySet()) {
        double documentFrequency = 0.0D;

        for (String documentId1 : documentTermMap.keySet()) {
          if ( documentTermMap.get(documentId1).containsKey(documentId)) {
            documentFrequency++;
          }
        }

        idfTermsMap.put(documentId, calculateIDF(documentFrequency, numDocuments));
      }

      for (String documentId : documentTermMap.keySet()) {
        List<TermWeightTuple> termWeights = new LinkedList<>();

        for (String term :  this.documentTermMap.get(documentId).keySet()) {
          termWeights.add(new TermWeightTuple(term, idfTermsMap.get(term)));
        }

        idfMap.put(documentId, termWeights);
      }
    }

    return idfMap;
  }

  private double calculateIDF(double documentFrequency, int numDocuments)
  {
    return documentFrequency == 0.0D ? 0.0D : Math.log10(numDocuments / (documentFrequency + 1.0D)) + 1.0D;
  }

  private Map<String, List<TermWeightTuple>> calculateTFMap()
  {
    Map tfMap = null;

    if ((isCalculator(IRPropertyKeys.ScoreCalculatorType.TF)) || (isCalculator(IRPropertyKeys.ScoreCalculatorType.TFIDF))) {
      tfMap = new HashMap();

      for (String documentId : documentTermMap.keySet()) {
        List termWeights = new LinkedList();

        double allTermOccurrences = 0.0D;
        if (isTFRelative) {
          Map<String, Integer> termOccurrenceMap = documentTermMap.get(documentId);
          for (String term : termOccurrenceMap.keySet()) {
            allTermOccurrences += termOccurrenceMap.get(term).intValue();
          }

        }

        Map<String, Integer> termOccurrenceMap = documentTermMap.get(documentId);
        for (String term : termOccurrenceMap.keySet()) {
          if (isTFRelative)
            termWeights.add(new TermWeightTuple(term, termOccurrenceMap.get(term).intValue() / allTermOccurrences));
          else {
            termWeights.add(new TermWeightTuple(term, termOccurrenceMap.get(term).intValue()));
          }
        }
        tfMap.put(documentId, termWeights);
      }
    }

    return tfMap;
  }

  private TermWeightTuple getTuple(String term, List<TermWeightTuple> termWeights)
  {
    for (TermWeightTuple termWeight : termWeights)
      if (term.equals(termWeight.getTerm()))
        return termWeight;
    return null;
  }

  private boolean isCalculator(IRPropertyKeys.ScoreCalculatorType calculatorType)
  {
    return this.calculatorType.toString().equals(calculatorType.toString());
  }

  private void loadProperties(Properties properties)
  {
    if (properties.containsKey("tfVariant")) {
      String tfVariantProperty = properties.getProperty("tfVariant");

      if (tfVariantProperty.equalsIgnoreCase(IRPropertyKeys.TermFrequencyVariant.ABSOLUTE.toString()))
        isTFRelative = false;
      else if (tfVariantProperty.equalsIgnoreCase(IRPropertyKeys.TermFrequencyVariant.RELATIVE.toString())) {
        isTFRelative = true;
      }
    }
    if (properties.containsKey("scoreCalculator")) {
      String scoreCalcProperty = properties.getProperty("scoreCalculator");

      if (scoreCalcProperty.equalsIgnoreCase(IRPropertyKeys.ScoreCalculatorType.TF.toString()))
        calculatorType = IRPropertyKeys.ScoreCalculatorType.TF;
      else if (scoreCalcProperty.equalsIgnoreCase(IRPropertyKeys.ScoreCalculatorType.IDF.toString()))
        calculatorType = IRPropertyKeys.ScoreCalculatorType.IDF;
      else if (scoreCalcProperty.equalsIgnoreCase(IRPropertyKeys.ScoreCalculatorType.TFIDF.toString()))
        calculatorType = IRPropertyKeys.ScoreCalculatorType.TFIDF;
      else calculatorType = IRPropertyKeys.ScoreCalculatorType.TF;
    }
  }
}