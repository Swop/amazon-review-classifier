package eit.amazon.classifier.classification;

import eit.amazon.classifier.classification.Learning.Label;
import eit.amazon.classifier.corpusdata.WordInfos;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

public class Inference {

	public static double[] testReviewFuzzy(String reviewText, HashMap dictionary, ModelType model) {
		double[] fuzzyValues = new double[2];

		double probaPos = 0;
		double probaNeg = 0;

		double betaPos = 0;
		double betaNeg = 0;

		StringTokenizer st = new StringTokenizer(reviewText);

		switch(model) {
			case BERNOULLI:
				HashMap revDictionary = new HashMap();

				while (st.hasMoreTokens()) {
					String word = st.nextToken();
					revDictionary.put(word, 1);
				}

				for (Object w : dictionary.keySet()) {
					String word = (String) w;
					WordInfos wdInfos = (WordInfos) (dictionary.get(word));

					if (revDictionary.containsKey(word)) {
						probaPos += Math.log(wdInfos.getBetaPositive());
						probaNeg += Math.log(wdInfos.getBetaNegative());
					} else {
						probaPos += Math.log(1 - wdInfos.getBetaPositive());
						probaNeg += Math.log(1 - wdInfos.getBetaNegative());
					}

				}
				break;
			case MULTINOMIAL:

				while (st.hasMoreTokens()) {
					String word = st.nextToken();

					if(dictionary.containsKey(word)) {
						betaPos = ((WordInfos)dictionary.get(word)).getBetaPositive();
						betaNeg = ((WordInfos)dictionary.get(word)).getBetaNegative();
						probaPos += Math.log(betaPos);
						probaNeg += Math.log(betaNeg);
					}
				}
				break;
		}
		
		fuzzyValues[0] = probaPos/Math.abs(probaPos+probaNeg) + 1;
		fuzzyValues[1] = probaNeg/Math.abs(probaPos+probaNeg) + 1;

		return fuzzyValues;
	}

	public static Learning.Label testReview(String reviewText, HashMap dictionary, ModelType model) {
		double[] fuzzyValues = testReviewFuzzy(reviewText, dictionary, model);

		if (fuzzyValues[0] > fuzzyValues[1])
			return Label.POSITIVE;
		else
			if(fuzzyValues[0] < fuzzyValues[1])
				return Label.NEGATIVE;
			else
				return Label.POSITIVE;
	}

	public static double test(File file, HashMap dictionnary, ModelType model, boolean printReview) {
		int cptOK = 0;
		int cptKO = 0;
		int cptReviews = 0;

		//System.out.println("---- INFERENCE ----");

		boolean inReview = false;
		Label reviewTrueLabel = null;
		Label reviewLabel = null;
		String reviewText = "";
		String reviewId = "";

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line;
			while ((line = br.readLine()) != null) {
				if(!inReview) {
					Matcher matcher = Learning.reviewStartMarkupPattern.matcher(line);
					if(matcher.matches()) {
						inReview = true;
						reviewText = "";
						String reviewRate = matcher.group(2);
						reviewId = matcher.group(1);
						if(reviewRate.equals("negative")) {
							reviewTrueLabel = Label.NEGATIVE;
						} else if(reviewRate.equals("positive")) {
							reviewTrueLabel = Label.POSITIVE;
						} else
							reviewTrueLabel = Label.UNKNOWN;

						continue;
					}

				} else {
					Matcher matcher = Learning.reviewEndMarkupPattern.matcher(line);
					if(matcher.matches()) {
						inReview = false;
						reviewLabel = Inference.testReview(reviewText, dictionnary, model);

						if (reviewTrueLabel.equals(reviewLabel)) {
							//System.out.println("Review "+reviewId+" : "+reviewLabel.toString()+" -> OK");
							cptOK++;
						} else {
							//System.out.println("Review "+reviewId+" : "+reviewLabel.toString()+" -> KO");
							cptKO++;
						}
						cptReviews++;

						if(printReview) {
							String printLabel = "";
							if(reviewLabel.equals(Label.NEGATIVE))
								printLabel = "negative";
							else if(reviewLabel.equals(Label.POSITIVE))
								printLabel = "positive";
							System.out.println("<review id='"+reviewId+"' class='"+printLabel+"'>");
						}

						continue;
					}
					reviewText += line+" ";
				}
			}
			br.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		double pourcOK = (Math.ceil(((double) cptOK / (double) cptReviews) * 10000)) / 100;
		double pourcKO = (Math.ceil(((double) cptKO / (double) cptReviews) * 10000)) / 100;

		//System.out.println("	OK : " + cptOK + "/" + cptReviews + " ( " + pourcOK + " %)");
		//System.out.println("	KO : " + cptKO + "/" + cptReviews + " ( " + pourcKO + " %)");

		return pourcOK;
	}
}
