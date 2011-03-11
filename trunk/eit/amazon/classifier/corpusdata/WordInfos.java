package eit.amazon.classifier.corpusdata;

import eit.amazon.classifier.classification.ModelType;

public class WordInfos {

	private String word;
	private double positiveOccurence = 0;
	private double negativeOccurence = 0;
	private double betaNegative = 0;
	private double betaPositive = 0;

	private static float epsilon = 1;

	public WordInfos(String word) {
		this.word = word;
	}

	public double getBetaPositive() {
		return betaPositive;
	}

	public double getBetaNegative() {
		return betaNegative;
	}

	public String getWord() {
		return word;
	}

	public void computeBetaPositive(ModelType model, CorpusInfos corpus) {
		switch(model) {
			case BERNOULLI:
				this.betaPositive = (double) (this.getPositiveOccurence() + WordInfos.epsilon)/(double)(corpus.getPositiveReviewsNumber() + 2*WordInfos.epsilon);
				break;
			case MULTINOMIAL:
				this.betaPositive = (double) (this.getPositiveOccurence() + WordInfos.epsilon)/(double)(corpus.getPositiveReviewsLength() + corpus.getDictionary().size()*WordInfos.epsilon);
				break;
		}
	}

	public void computeBetaNegative(ModelType model, CorpusInfos corpus) {
		switch(model) {
			case BERNOULLI:
				this.betaNegative = (double) (this.getNegativeOccurence() + WordInfos.epsilon)/(double)(corpus.getNegativeReviewsNumber() + 2*WordInfos.epsilon);
				break;
			case MULTINOMIAL:
				this.betaNegative = (double) (this.getNegativeOccurence() + WordInfos.epsilon)/(double)(corpus.getNegativeReviewsLength() + corpus.getDictionary().size()*WordInfos.epsilon);
				break;
		}
	}

	public double getPositiveOccurence() {
		return positiveOccurence;
	}

	public double getNegativeOccurence() {
		return negativeOccurence;
	}

	public double getAllOccurences() {
		return positiveOccurence + negativeOccurence;
	}

	public void addNegativeOccurence(int occurence, double fuzzyNegative) {
		negativeOccurence += occurence * fuzzyNegative;
	}

	public void addPositiveOccurence(int occurence, double fuzzyPositive) {
		positiveOccurence += occurence * fuzzyPositive;
	}
}
