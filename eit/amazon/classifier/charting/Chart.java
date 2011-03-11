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

public class Chart {
	private JFreeChart chart;
	private List<XYSeries> seriesPoints;
	private String title;
	private String xLabel;
	private String yLabel;

	public Chart(String title, String xLabel, String yLabel) {
		seriesPoints = new ArrayList<XYSeries>();
		this.title = title;
		this.xLabel = xLabel;
		this.yLabel = yLabel;

	}

	public List<XYSeries> getSeriesPoints() {
		return seriesPoints;
	}

	public void buildChart() {
		XYSeriesCollection dataset = new XYSeriesCollection();
        for(XYSeries serie : seriesPoints)
			dataset.addSeries(serie);
		chart = ChartFactory.createXYLineChart(title, xLabel, yLabel, dataset, PlotOrientation.VERTICAL, true, false, false);
	}

	public void displayChart(String frameTitle) {
		ChartFrame frame = new ChartFrame(frameTitle, chart);
		frame.pack();
		frame.setVisible(true);
	}
	
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
