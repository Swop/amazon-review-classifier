package eit.amazon.classifier.corpusdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CorpusInfos implements Serializable {
	private List<ReviewInfos> reviews;

	private HashMap dictionary;

	public CorpusInfos() {
		reviews = new ArrayList<ReviewInfos>();
		dictionary = new HashMap();
	}

	public List<ReviewInfos> getReviews() {
		return reviews;
	}

	public HashMap getDictionary() {
		return dictionary;
	}

	public double getNegativeReviewsLength() {
		int cpt = 0;
		for(ReviewInfos r : reviews) {
			cpt += r.getLength() * r.getFuzzyNegative();
		}
		return cpt;
	}

	public double getPositiveReviewsLength() {
		int cpt = 0;
		for(ReviewInfos r : reviews) {
			cpt += r.getLength() * r.getFuzzyPositive();
		}
		return cpt;
	}

	public double getNegativeReviewsNumber() {
		double cpt = 0;
		for(ReviewInfos r : reviews) {
			cpt += r.getFuzzyNegative();
		}
		return cpt;
	}

	public double getPositiveReviewsNumber() {
		double cpt = 0;
		for(ReviewInfos r : reviews) {
			cpt += r.getFuzzyPositive();
		}
		return cpt;
	}
}
