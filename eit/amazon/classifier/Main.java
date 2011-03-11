package eit.amazon.classifier;

import eit.amazon.classifier.charting.Chart;
import eit.amazon.classifier.classification.*;
import eit.amazon.classifier.corpusdata.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;
import org.jfree.data.xy.XYSeries;

public class Main {

	public static final String APP_NAME = "Amazon Review Categorizer";
	public static final String APP_VERSION = "1.0";
	public static final String APP_AUTHORS = "Sylvain MAUDUIT & Olivier LE STER";

	private static File mainFolder = null;

	public static void main(String[] args) {
		if(args.length < 2) {
			printUsage();
			System.exit(1);
		}

		if(args[0].equals("test")) {
			if(args.length < 3) {
				printUsage();
				System.exit(1);
			}
			mainFolder = new File(args[2]);
			blindTest(new File(args[1]));
			System.exit(0);
		} else if(!args[0].equals("simulation")) {
			printUsage();
			System.exit(1);
		}

		printHeader();
		mainFolder = new File(args[1]);

		makeSimulations();
		makeTests();
	}

	private static void testBooks(Pattern pattern) {
//		Pattern pattern = Pattern.compile("[a-zA-Z]{2,}");
		CorpusInfos corpus = new CorpusInfos();
		System.out.println("==== BOOKS ====");
		
		System.out.println("-- SUPPERVISED LEARNING");
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/books/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- INFERENCE");
		double pourcOK = Inference.test(new File(mainFolder.getAbsolutePath()+"/books/test.txt"), corpus.getDictionary(), ModelType.MULTINOMIAL, false);
		System.out.println("Success : "+pourcOK+" %");
	}

	private static void testElectronics(Pattern pattern) {
//		Pattern pattern = Pattern.compile("[a-zA-Z]{2,}");
		CorpusInfos corpus = new CorpusInfos();
		System.out.println("==== ELECTRONICS ====");

		System.out.println("-- SUPPERVISED LEARNING");
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/electronics/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- INFERENCE");
		double pourcOK = Inference.test(new File(mainFolder.getAbsolutePath()+"/electronics/test.txt"), corpus.getDictionary(), ModelType.MULTINOMIAL, false);
		System.out.println("Success : "+pourcOK+" %");
	}

	private static void testSupervisedLearningKitchen(Pattern pattern) {
//		Pattern pattern = Pattern.compile("[a-zA-Z]{2,}");
		CorpusInfos corpus = new CorpusInfos();
		System.out.println("==== KITCHEN ====");

		System.out.println("-- SUPPERVISED LEARNING");
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/kitchen/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- INFERENCE");
		double pourcOK = Inference.test(new File(mainFolder.getAbsolutePath()+"/kitchen/test.txt"), corpus.getDictionary(), ModelType.MULTINOMIAL, false);
		System.out.println("Success : "+pourcOK+" %");
	}

	private static void testSemiSupervisedLearningKitchenWithTrueLabels(Pattern pattern) {
//		Pattern pattern = Pattern.compile("[a-zA-Z]{2,}");
		CorpusInfos corpus = new CorpusInfos();
		System.out.println("==== KITCHEN ====");

		System.out.println("-- SUPPERVISED LEARNING");
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/kitchen/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- ADD SEMI-SUPPERVISED LEARNING (with True Labels)");
		Learning.semiSupervisedLearning(new File(mainFolder.getAbsolutePath()+"/kitchen/unlab.txt"), ModelType.MULTINOMIAL, Learning.SemiSupervisedLearningAppoach.REAL_LABELS, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- INFERENCE");
		double pourcOK = Inference.test(new File(mainFolder.getAbsolutePath()+"/kitchen/dev.txt"), corpus.getDictionary(), ModelType.MULTINOMIAL, false);
		System.out.println("Success : "+pourcOK+" %");
	}

	private static void testSemiSupervisedLearningKitchenWithFuzzyLabels(Pattern pattern) {
//		Pattern pattern = Pattern.compile("[a-zA-Z]{2,}");
		CorpusInfos corpus = new CorpusInfos();
		System.out.println("==== KITCHEN ====");

		System.out.println("-- SUPPERVISED LEARNING");
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/kitchen/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- ADD SEMI-SUPPERVISED LEARNING (with Fuzzy Labels)");
		Learning.semiSupervisedLearning(new File(mainFolder.getAbsolutePath()+"/kitchen/unlab.txt"), ModelType.MULTINOMIAL, Learning.SemiSupervisedLearningAppoach.FUZZY_LABELS, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- INFERENCE");
		double pourcOK = Inference.test(new File(mainFolder.getAbsolutePath()+"/kitchen/dev.txt"), corpus.getDictionary(), ModelType.MULTINOMIAL, false);
		System.out.println("Success : "+pourcOK+" %");
	}

	private static void testSupervisedGlobalLearningDvds(Pattern pattern) {
//		Pattern pattern = Pattern.compile("[a-zA-Z]{2,}");
		CorpusInfos corpus = new CorpusInfos();
		System.out.println("==== DVD ====");

		System.out.println("-- SUPPERVISED LEARNING USING BOOKS");
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/books/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- ADD SUPPERVISED LEARNING USING ELECTRONICS");
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/electronics/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- ADD SUPPERVISED LEARNING USING KITCHEN");
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/kitchen/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- INFERENCE");
		double pourcOK = Inference.test(new File(mainFolder.getAbsolutePath()+"/dvd/dev.txt"), corpus.getDictionary(), ModelType.MULTINOMIAL, false);
		System.out.println("Success : "+pourcOK+" %");
	}

	private static void testSemiSupervisedGlobalLearningDvdsWithTrueLabels(Pattern pattern) {
//		Pattern pattern = Pattern.compile("[a-zA-Z]{2,}");
		CorpusInfos corpus = new CorpusInfos();
		System.out.println("==== DVD ====");

		System.out.println("-- SUPPERVISED LEARNING USING BOOKS");
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/books/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- ADD SUPPERVISED LEARNING USING ELECTRONICS");
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/electronics/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- ADD SUPPERVISED LEARNING USING KITCHEN");
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/kitchen/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- ADD SEMI-SUPPERVISED LEARNING (with True Labels)");
		Learning.semiSupervisedLearning(new File(mainFolder.getAbsolutePath()+"/dvd/unlab.txt"), ModelType.MULTINOMIAL, Learning.SemiSupervisedLearningAppoach.REAL_LABELS, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- INFERENCE");
		double pourcOK = Inference.test(new File(mainFolder.getAbsolutePath()+"/dvd/dev.txt"), corpus.getDictionary(), ModelType.MULTINOMIAL, false);
		System.out.println("Success : "+pourcOK+" %");
	}

	private static void testSemiSupervisedGlobalLearningDvdsWithFuzzyLabels(Pattern pattern) {
//		Pattern pattern = Pattern.compile("[a-zA-Z]{2,}");
		CorpusInfos corpus = new CorpusInfos();
		System.out.println("==== DVD ====");

		System.out.println("-- SUPPERVISED LEARNING USING BOOKS");
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/books/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- ADD SUPPERVISED LEARNING USING ELECTRONICS");
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/electronics/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- ADD SUPPERVISED LEARNING USING KITCHEN");
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/kitchen/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- ADD SEMI-SUPPERVISED LEARNING (with Fuzzy Labels)");
		Learning.semiSupervisedLearning(new File(mainFolder.getAbsolutePath()+"/dvd/unlab.txt"), ModelType.MULTINOMIAL, Learning.SemiSupervisedLearningAppoach.FUZZY_LABELS, corpus, pattern);
		System.out.println(corpus.getDictionary().size()+" entries in dictionnary");
		System.out.println(corpus.getReviews().size()+" reviews");

		System.out.println("-- INFERENCE");
		double pourcOK = Inference.test(new File(mainFolder.getAbsolutePath()+"/dvd/dev.txt"), corpus.getDictionary(), ModelType.MULTINOMIAL, false);
		System.out.println("Success : "+pourcOK+" %");
	}
	
	private static void simulationBooks() {
		Chart chart = null;
		String pathChart = "simulBooks.jpg";

		System.out.println("---- SIMULATION BOOKS");

		chart = new Chart("Performance Apprentissage suppervisé (Books)", "Taille minimale des mots accéptés ", "Taux de réussite");
		System.out.println("Chart destination : "+pathChart);

		for(ModelType type : ModelType.values()) {
			System.out.println("Model : "+type.toString());
			XYSeries serie = new XYSeries(type.toString());
			for(int i=1; i<=20; i++) {
				System.out.print("Iteration "+i+"/20 - Success : ");

				Pattern pattern = Pattern.compile("[a-zA-Z]{"+i+",}");
				CorpusInfos corpus = new CorpusInfos();
				Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/books/training.txt"), type, corpus, pattern);

				double pourcOK = Inference.test(new File(mainFolder.getAbsolutePath()+"/books/dev.txt"), corpus.getDictionary(), type, false);
				System.out.println(pourcOK);

				serie.add(i, pourcOK);
			}
			chart.getSeriesPoints().add(serie);
		}

		chart.buildChart();
		try {
			chart.saveChart(pathChart, 1000, 800, 100);
			System.out.println("Chart saved in "+pathChart);
		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {}
	}

	private static void simulationElectronics() {
		Chart chart = null;
		String pathChart = "simulElectronics.jpg";

		System.out.println("---- SIMULATION ELECTRONICS");

		chart = new Chart("Performance Apprentissage suppervisé (Electronics)", "Taille minimale des mots accéptés ", "Taux de réussite");
		System.out.println("Chart destination : "+pathChart);

		for(ModelType type : ModelType.values()) {
			System.out.println("Model : "+type.toString());
			XYSeries serie = new XYSeries(type.toString());
			for(int i=1; i<=20; i++) {
				System.out.print("Iteration "+i+"/20 - Success : ");

				Pattern pattern = Pattern.compile("[a-zA-Z]{"+i+",}");
				CorpusInfos corpus = new CorpusInfos();
				Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/electronics/training.txt"), type, corpus, pattern);

				double pourcOK = Inference.test(new File(mainFolder.getAbsolutePath()+"/electronics/dev.txt"), corpus.getDictionary(), type, false);
				System.out.println(pourcOK);

				serie.add(i, pourcOK);
			}
			chart.getSeriesPoints().add(serie);
		}

		chart.buildChart();
		try {
			chart.saveChart(pathChart, 1000, 800, 100);
			System.out.println("Chart saved in "+pathChart);
		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {}
	}

	private static void makeSimulations() {
		System.out.println("********** SIMULATIONS ***********");
		System.out.println("");
		System.out.println("mdsa directory : " + mainFolder.getAbsolutePath());
		System.out.println("");

		simulationBooks();
		System.out.println("");
		simulationElectronics();
		System.out.println("");
	}

	private static void makeTests() {
		System.out.println("********** TESTS ***********");
		System.out.println("");
		System.out.println("mdsa directory : " + mainFolder.getAbsolutePath());
		System.out.println("");

		Pattern pattern = Pattern.compile("[a-zA-Z]{1,}");

		Main.testBooks(pattern);
		System.out.println("");
		Main.testElectronics(pattern);
		System.out.println("");
		Main.testSupervisedLearningKitchen(pattern);
		System.out.println("");
		Main.testSemiSupervisedLearningKitchenWithTrueLabels(pattern);
		System.out.println("");
		Main.testSemiSupervisedLearningKitchenWithFuzzyLabels(pattern);
		System.out.println("");
		Main.testSupervisedGlobalLearningDvds(pattern);
		System.out.println("");
		Main.testSemiSupervisedGlobalLearningDvdsWithTrueLabels(pattern);
		System.out.println("");
		Main.testSemiSupervisedGlobalLearningDvdsWithFuzzyLabels(pattern);
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("Appname simulation path_to_mdsa_directory");
		System.out.println("\t\tOR");
		System.out.println("Appname test file_to_test path_to_mdsa_directory");
	}

	private static void printHeader() {
		System.out.println("**************************************");
		System.out.println();
		System.out.println(APP_NAME+ " (v"+APP_VERSION+")");
		System.out.println(APP_AUTHORS);
		System.out.println();
		System.out.println("**************************************");
		System.out.println();
	}

	private static void blindTest(File file) {
		Pattern pattern = Pattern.compile("[a-zA-Z]{2,}");

		CorpusInfos corpus = new CorpusInfos();

		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/books/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/electronics/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);
		Learning.supervisedLearning(new File(mainFolder.getAbsolutePath()+"/kitchen/training.txt"), ModelType.MULTINOMIAL, corpus, pattern);

		Learning.semiSupervisedLearning(new File(mainFolder.getAbsolutePath()+"/dvd/unlab.txt"), ModelType.MULTINOMIAL, Learning.SemiSupervisedLearningAppoach.FUZZY_LABELS, corpus, pattern);

		double pourcOK = Inference.test(file, corpus.getDictionary(), ModelType.MULTINOMIAL, true);
		System.out.println("Success : "+pourcOK+" %");
	}
}
