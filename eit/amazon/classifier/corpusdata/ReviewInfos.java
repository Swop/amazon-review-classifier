package eit.amazon.classifier.corpusdata;

import java.io.File;

public class ReviewInfos {
	private File file;
	private double fuzzyPositive;
	private double fuzzyNegative;
	private int length;
	private String reviewText;

	public ReviewInfos(File file, double fuzzyPositive, double fuzzyNegative, String text) {
		this.file = file;
		this.fuzzyNegative = fuzzyNegative;
		this.fuzzyPositive = fuzzyPositive;
		length = 0;
		this.reviewText = text;
	}

	public File getFile() {
		return file;
	}

	public int getLength() {
		return length;
	}

	public double getFuzzyNegative() {
		return fuzzyNegative;
	}

	public double getFuzzyPositive() {
		return fuzzyPositive;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getReviewText() {
		return reviewText;
	}
}
