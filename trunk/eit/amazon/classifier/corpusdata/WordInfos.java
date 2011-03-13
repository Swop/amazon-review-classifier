package eit.amazon.classifier.corpusdata;

import eit.amazon.classifier.classification.ModelType;

/**
 * Informations a propos du mot
 */
public class WordInfos {
	/**
	 * Le mot
	 */
	private String word;
	/**
	 * Le nombre de fois qu'il apparait dans tous les documents positifs
	 */
	private double positiveOccurence = 0;
	/**
	 * Le nombre de fois qu'il apparait dans tous les documents negatifs
	 */
	private double negativeOccurence = 0;
	/**
	 * Valeur Betan negaif
	 */
	private double betaNegative = 0;
	/**
	 * Valeur Beta positif
	 */
	private double betaPositive = 0;

	/**
	 * Valeur de Epsilon (pour le lissage)
	 */
	private static float epsilon = 1;

	/**
	 * Cree un nouveau mot
	 * @param word Le mot
	 */
	public WordInfos(String word) {
		this.word = word;
	}

	/**
	 * Retourne la valeur Beta positif du mot
	 * @return valeur Beta positif du mot
	 */
	public double getBetaPositive() {
		return betaPositive;
	}

	/**
	 * Retourne la valeur Beta negatif du mot
	 * @return valeur Beta negatif du mot
	 */
	public double getBetaNegative() {
		return betaNegative;
	}

	/**
	 * Retourne le mot
	 * @return le mot
	 */
	public String getWord() {
		return word;
	}

	/**
	 * Calcul la valeur Beta positif du mot
	 * @param model Le model addpte (binomial, multinomial)
	 * @param corpus Le corpus
	 */
	public void computeBetaPositive(ModelType model, CorpusInfos corpus) {
		switch(model) {
			case BINOMIAL:
				this.betaPositive = (double) (this.getPositiveOccurence() + WordInfos.epsilon)/(double)(corpus.getPositiveReviewsNumber() + 2*WordInfos.epsilon);
				break;
			case MULTINOMIAL:
				this.betaPositive = (double) (this.getPositiveOccurence() + WordInfos.epsilon)/(double)(corpus.getPositiveReviewsLength() + corpus.getDictionary().size()*WordInfos.epsilon);
				break;
		}
	}

	/**
	 * Calcul la valeur Beta negatif du mot
	 * @param model Le model addpte (binomial, multinomial)
	 * @param corpus Le corpus
	 */
	public void computeBetaNegative(ModelType model, CorpusInfos corpus) {
		switch(model) {
			case BINOMIAL:
				this.betaNegative = (double) (this.getNegativeOccurence() + WordInfos.epsilon)/(double)(corpus.getNegativeReviewsNumber() + 2*WordInfos.epsilon);
				break;
			case MULTINOMIAL:
				this.betaNegative = (double) (this.getNegativeOccurence() + WordInfos.epsilon)/(double)(corpus.getNegativeReviewsLength() + corpus.getDictionary().size()*WordInfos.epsilon);
				break;
		}
	}

	/**
	 * Retourne le nombre de fois qu'il apparait dans tous les documents positifs
	 * @return Le nombre de fois qu'il apparait dans tous les documents positifs
	 */
	public double getPositiveOccurence() {
		return positiveOccurence;
	}

	/**
	 * Retourne le nombre de fois qu'il apparait dans tous les documents negatifs
	 * @return Le nombre de fois qu'il apparait dans tous les documents negatifs
	 */
	public double getNegativeOccurence() {
		return negativeOccurence;
	}

	/**
	 * Retourne le nombre de fois qu'il apparait dans tous les avis
	 * @return Le nombre de fois qu'il apparait dans tous les avis
	 */
	public double getAllOccurences() {
		return positiveOccurence + negativeOccurence;
	}

	/**
	 * Ajoute un certain nombre d'occurence, avec une probabilite d'appartenance a priori a la classe negative (etiquette floue)
	 * @param occurence Le nombre d'occurence a ajouter
	 * @param fuzzyNegative Pourcentage d'appartenence a la classe negative (etiquette floue)
	 */
	public void addNegativeOccurence(int occurence, double fuzzyNegative) {
		negativeOccurence += occurence * fuzzyNegative;
	}

	/**
	 * Ajoute un certain nombre d'occurence, avec une probabilite d'appartenance a priori a la classe positive (etiquette floue)
	 * @param occurence Le nombre d'occurence a ajouter
	 * @param fuzzyPositive Pourcentage d'appartenence a la classe positive (etiquette floue)
	 */
	public void addPositiveOccurence(int occurence, double fuzzyPositive) {
		positiveOccurence += occurence * fuzzyPositive;
	}
}
