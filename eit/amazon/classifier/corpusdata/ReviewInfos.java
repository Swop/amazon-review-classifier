package eit.amazon.classifier.corpusdata;

import java.io.File;

/**
 * Information a propos de l'avis
 */
public class ReviewInfos {
	/**
	 * Fichier qui contient l'avis
	 */
	private File file;
	/**
	 * Pourcentage d'appartenence a la classe positive (etiquette floue)
	 */
	private double fuzzyPositive;
	/**
	 * Pourcentage d'appartenence a la classe negative (etiquette floue)
	 */
	private double fuzzyNegative;

	/**
	 * Taille de l'avis
	 */
	private int length;

	/**
	 * Texte de l'avis
	 */
	private String reviewText;

	/**
	 * Cree un nouvel avis
	 * @param file Fichier qui contient l'avis
	 * @param fuzzyPositive Pourcentage d'appartenence a la classe positive (etiquette floue)
	 * @param fuzzyNegative Pourcentage d'appartenence a la classe negative (etiquette floue)
	 * @param text Texte de l'avis
	 */
	public ReviewInfos(File file, double fuzzyPositive, double fuzzyNegative, String text) {
		this.file = file;
		this.fuzzyNegative = fuzzyNegative;
		this.fuzzyPositive = fuzzyPositive;
		length = 0;
		this.reviewText = text;
	}

	/**
	 * Retourne le fichier contenant l'avis
	 * @return Le fichier contenant l'avis
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Retourne la taille de l'avis
	 * @return La taille de l'avis
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Retourne le pourcentage d'appartenence a la classe negative (etiquette floue)
	 * @return le pourcentage d'appartenence a la classe negative (etiquette floue)
	 */
	public double getFuzzyNegative() {
		return fuzzyNegative;
	}

	/**
	 * Retourne le pourcentage d'appartenence a la classe positive (etiquette floue)
	 * @return Le pourcentage d'appartenence a la classe positive (etiquette floue)
	 */
	public double getFuzzyPositive() {
		return fuzzyPositive;
	}

	/**
	 * Modifie la taille de l'avis
	 * @param length La nouvelle taille de l'avis
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Retourne le texte de l'avis
	 * @return Le texte de l'avis
	 */
	public String getReviewText() {
		return reviewText;
	}
}
