package eit.amazon.classifier.charting;

import com.sun.image.codec.jpeg.*;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Gere le graphique de performance
 */
public class Chart {
	/**
	 * Graphique JFreeChart
	 */
	private JFreeChart chart;
	/**
	 * Series de points
	 */
	private List<XYSeries> seriesPoints;
	/**
	 * Titre du graphique
	 */
	private String title;
	/**
	 * Legende en abscisse
	 */
	private String xLabel;
	/**
	 * Legende en ordonee
	 */
	private String yLabel;

	/**
	 * Cree un nouveau graph
	 * @param title Titre du graph
	 * @param xLabel Etiquette en x
	 * @param yLabel Etiquette en y
	 */
	public Chart(String title, String xLabel, String yLabel) {
		seriesPoints = new ArrayList<XYSeries>();
		this.title = title;
		this.xLabel = xLabel;
		this.yLabel = yLabel;

	}

	/**
	 * Retourne la liste de series
	 * @return La liste de series
	 */
	public List<XYSeries> getSeriesPoints() {
		return seriesPoints;
	}

	/**
	 * Construit le graphique une fois tou sles points ajoutees
	 */
	public void buildChart() {
		XYSeriesCollection dataset = new XYSeriesCollection();
        for(XYSeries serie : seriesPoints)
			dataset.addSeries(serie);
		chart = ChartFactory.createXYLineChart(title, xLabel, yLabel, dataset, PlotOrientation.VERTICAL, true, false, false);
	}

	/**
	 * Affiche une fenetre contenant le graphique
	 * @param frameTitle Titre de la fenetre
	 */
	public void displayChart(String frameTitle) {
		ChartFrame frame = new ChartFrame(frameTitle, chart);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Sauvegarde le graph dans un fichier
	 * @param aFileName Chemin vers le fichier de destination
	 * @param width Largeur en pixel
	 * @param height Hauteur en pixel
	 * @param quality Qualitee de l'image
	 * @throws FileNotFoundException Si le fichier ne peut etre cree
	 * @throws IOException S'il y a une erreur dans l'ecriture du fichier
	 */
	public void saveChart(String aFileName,
			int width,
			int height,
			double quality)
			throws FileNotFoundException, IOException {

		BufferedImage img = draw(width, height);

		FileOutputStream fos = new FileOutputStream(aFileName);
		JPEGImageEncoder encoder2 =
				JPEGCodec.createJPEGEncoder(fos);
		JPEGEncodeParam param2 =
				encoder2.getDefaultJPEGEncodeParam(img);
		param2.setQuality((float) quality, true);
		encoder2.encode(img, param2);
		fos.close();
	}

	/**
	 * Dessine l'image du graph dans un buffer
	 * @param width Largeur en pixel
	 * @param height Hauteur en pixel
	 * @return Le buffer de l'image
	 */
	protected BufferedImage draw(int width, int height) {
		BufferedImage img =
				new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();

		chart.draw(g2, new Rectangle2D.Double(0, 0, width, height));

		g2.dispose();
		return img;
	}
}
