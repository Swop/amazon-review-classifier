package eit.amazon.classifier.classification;

import eit.amazon.classifier.corpusdata.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Methodes permettant les etapes d'apprentissage
 */
public class Learning {
	/**
	 * Reggex de debut de reveiw
	 */
	public final static Pattern reviewStartMarkupPattern = Pattern.compile("<review id='([0-9]+)' class='(.+)'>");
	/**
	 * Regex de fin de review
	 */
	public final static Pattern reviewEndMarkupPattern = Pattern.compile("^\\s*</review>\\s*$");

	/**
	 * Labels possibles pour une review
	 */
	public enum Label { POSITIVE, NEGATIVE, UNKNOWN };

	/**
	 * Approches possibles pour l'apprentissage semi-suppervise
	 */
	public enum SemiSupervisedLearningAppoach { REAL_LABELS, FUZZY_LABELS };

	/**
	 * Effecture l'apprentissage supervise par rapport a un fichier de reviews
	 * @param file Le fichier de reviews
	 * @param model Le model addpte (binomial, multinomial)
	 * @param corpus Le corpus ou stoquer le dictionnaire
	 * @param pattern La regex a utiliser pour l'accteptation des mots
	 */
	public static void supervisedLearning(File file, ModelType model, CorpusInfos corpus, Pattern pattern) {
		List<ReviewInfos> reviews = getReviewsFromFile(file, model, SemiSupervisedLearningAppoach.REAL_LABELS, corpus, pattern, false);

		for (Object w : corpus.getDictionary().keySet()) {
			String word = (String) w;
			WordInfos wdInfos = (WordInfos) corpus.getDictionary().get(word);

			wdInfos.computeBetaPositive(model, corpus);
			wdInfos.computeBetaNegative(model, corpus);
		}
	}

	/**
	 * Effecture l'apprentissage supervise par rapport a un fichier de reviews
	 * @param file Le fichier de reviews
	 * @param model Le model addpte (binomial, multinomial)
	 * @param approach Approche pour l'apprentissage semi-suppervise (logique floue ou label reels)
	 * @param corpus Le corpus ou stoquer le dictionnaire
	 * @param pattern La regex a utiliser pour l'accteptation des mots
	 */
	public static void semiSupervisedLearning(File file, ModelType model, SemiSupervisedLearningAppoach approach, CorpusInfos corpus, Pattern pattern) {
		List<ReviewInfos> reviews = getReviewsFromFile(file, model, approach, corpus, pattern, true);

		for (Object w : corpus.getDictionary().keySet()) {
			String word = (String) w;
			WordInfos wdInfos = (WordInfos) corpus.getDictionary().get(word);

			wdInfos.computeBetaPositive(model, corpus);
			wdInfos.computeBetaNegative(model, corpus);
		}
	}

	/**
	 * Recupere les infos des reviews a partir du fichier
	 * @param file Le fichier a annalyser
	 * @param model Le model addpte (binomial, multinomial)
	 * @param approach Approche pour l'apprentissage semi-suppervise (logique floue ou label reels)
	 * @param corpus Le corpus ou stoquer le dictionnaire
	 * @param pattern La regex a utiliser pour l'accteptation des mots
	 * @param semiSupp TRUE si l'apprentissage est semi-suppervise
	 * @return La liste des reviews
	 */
	public static List<ReviewInfos> getReviewsFromFile(File file, ModelType model, SemiSupervisedLearningAppoach approach, CorpusInfos corpus, Pattern pattern, boolean semiSupp) {
		List<ReviewInfos> reviews = new ArrayList<ReviewInfos>();
		HashMap reviewDictionary = null;

		boolean inReview = false;
		Label reviewLabel = null;
		String reviewText = "";

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
						if(reviewRate.equals("negative")) {
							reviewLabel = Label.NEGATIVE;
						} else if(reviewRate.equals("positive")) {
							reviewLabel = Label.POSITIVE;
						} else
							reviewLabel = Label.UNKNOWN;

						reviewDictionary = new HashMap();

						continue;
					}

				} else {
					Matcher matcher = Learning.reviewEndMarkupPattern.matcher(line);
					if(matcher.matches()) {
						inReview = false;
						double fuzzyPos = 0, fuzzyNeg = 0;

						if(reviewLabel.equals(Label.UNKNOWN)) {
							if(approach.equals(SemiSupervisedLearningAppoach.REAL_LABELS)) {
								reviewLabel = Inference.testReview(reviewText, corpus.getDictionary(), model);
							} else {
								double[] fuzz = Inference.testReviewFuzzy(reviewText, corpus.getDictionary(), model);
								fuzzyPos = fuzz[0];
								fuzzyNeg = fuzz[1];

								double a = fuzzyPos - fuzzyNeg;
								int b = 1+1;
							}
						}

						switch(reviewLabel) {
							case NEGATIVE:
								fuzzyPos = 0;
								fuzzyNeg = 1;
								break;
							case POSITIVE:
								fuzzyPos = 1;
								fuzzyNeg = 0;
								break;
							default:
								break;
						}
						
						int cptLength = 0;
						for (Object w : reviewDictionary.keySet()) {
							String word = (String) w;
							WordInfos wdInfos = (WordInfos) (corpus.getDictionary().get(word));

							boolean newWord = false;
							if (wdInfos == null) {
								wdInfos = new WordInfos(word);
								newWord = true;
							}

							if(!semiSupp || (semiSupp && !newWord)) {

								int occurences = (Integer)reviewDictionary.get(word);
								cptLength += occurences;

								wdInfos.addPositiveOccurence(occurences, fuzzyPos);
								wdInfos.addNegativeOccurence(occurences, fuzzyNeg);

								if (newWord) {
									corpus.getDictionary().put(word, wdInfos);
								}
							}
						}

						ReviewInfos revInfos = new ReviewInfos(file, fuzzyPos, fuzzyNeg, reviewText);
						revInfos.setLength(cptLength);
						reviews.add(revInfos);
						corpus.getReviews().add(revInfos);

						continue;
					}

					StringTokenizer st = new StringTokenizer(line);
					while (st.hasMoreTokens()) {
						String word = st.nextToken();//.toLowerCase();
						if (acceptWord(word, pattern)) {
							switch(model) {
								case BINOMIAL:
									reviewDictionary.put(word, 1);
									break;
								case MULTINOMIAL:
									if(reviewDictionary.containsKey(word))
										reviewDictionary.put(word, (Integer)reviewDictionary.get(word)+1);
									else
										reviewDictionary.put(word, 1);
									break;
							}
						}
					}

					reviewText += line+" ";
				}
			}
			br.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return reviews;
	}

	/**
	 * Teste si un mot est accepteee ou non
	 * @param word Le mot
	 * @param pattern La regex
	 * @return Le resultat du test
	 */
	public static boolean acceptWord(String word, Pattern pattern) {
		Matcher matcher = pattern.matcher(word);
		return matcher.matches();
	}
}
