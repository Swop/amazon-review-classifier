package eit.amazon.classifier.corpusdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Infomations portant sur le corpus de documents
 */
public class CorpusInfos implements Serializable {
	/**
	 * Liste des reviews
	 */
	private List<ReviewInfos> reviews;

	/**
	 * Dictionnaire du vocabulaire
	 */
	private HashMap dictionary;

	/**
	 * Cree un nouveau corpus
	 */
	public CorpusInfos() {
		reviews = new ArrayList<ReviewInfos>();
		dictionary = new HashMap();
	}

	/**
	 * Retourne la liste des reveiws
	 * @return La liste des reviews
	 */
	public List<ReviewInfos> getReviews() {
		return reviews;
	}

	/**
	 * Retourne le dictionnaire
	 * @return Le dictionnaire
	 */
	public HashMap getDictionary() {
		return dictionary;
	}

	/**
	 * Retourne la taille de l'ensemble des avis negatifs
	 * @return la taille de l'ensemble des avis negatifs
	 */
	public double getNegativeReviewsLength() {
		int cpt = 0;
		for(ReviewInfos r : reviews) {
			cpt += r.getLength() * r.getFuzzyNegative();
		}
		return cpt;
	}

	/**
	 * Retourne la taille de l'ensemble des avis positifs
	 * @return la taille de l'ensemble des avis positifs
	 */
	public double getPositiveReviewsLength() {
		int cpt = 0;
		for(ReviewInfos r : reviews) {
			cpt += r.getLength() * r.getFuzzyPositive();
		}
		return cpt;
	}

	/**
	 * Retourne le nombre d'avis negatifs
	 * @return le nombre d'avis negatifs
	 */
	public double getNegativeReviewsNumber() {
		double cpt = 0;
		for(ReviewInfos r : reviews) {
			cpt += r.getFuzzyNegative();
		}
		return cpt;
	}

	/**
	 * Retourne le nombre d'avis positifs
	 * @return le nombre d'avis positifs
	 */
	public double getPositiveReviewsNumber() {
		double cpt = 0;
		for(ReviewInfos r : reviews) {
			cpt += r.getFuzzyPositive();
		}
		return cpt;
	}
}
