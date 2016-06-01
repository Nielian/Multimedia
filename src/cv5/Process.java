package cv5;

import java.net.URL;
import java.util.ResourceBundle;

import Jama.Matrix;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import ij.ImagePlus;

/**
 * @author Zuzana
 *
 */
/**
 * @author Zuzana
 *
 */
/**
 * @author Zuzana
 *
 */
public class Process implements Initializable {
	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;
	public static final int Y = 4;
	public static final int CB = 5;
	public static final int CR = 6;
	public static final int S444 = 7;
	public static final int S422 = 8;
	public static final int S420 = 9;
	public static final int S411 = 10;
	private int vzorkovani = S444;

	private Quality quality;

	private ImagePlus imagePlus;
	private ColorTransform colorTransform;
	private ColorTransform colorTransformOrig;

	@FXML
	private Button redButton;
	@FXML
	private Button greenButton;
	@FXML
	private Button blueButton;
	@FXML
	private Button yButton;
	@FXML
	private Button cbButton;
	@FXML
	private Button crButton;
	@FXML
	private Button qualityButton;
	@FXML
	private Label psnrLabel;
	@FXML
	private Label mseLabel;
	@FXML
	private ToggleGroup kvantizGroup;
	@FXML
	private Slider slider;

	public void test1() {
		imagePlus.show("Original Image");
		colorTransform.getRGB();
		colorTransform.convertRgbToYcbcr();
		colorTransform.convertYcbcrToRgb();
		colorTransform.setImageFromRGB(colorTransform.getRed().length,
				colorTransform.getRed()[0].length, colorTransform.getRed(),
				colorTransform.getGreen(), colorTransform.getBlue()).show(
				"Transformed Image");

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		nactiOrigObraz();
		this.colorTransform.convertRgbToYcbcr();
		imagePlus.setTitle("Original Image");
		imagePlus.show("Original Image");

	}

	public void nactiOrigObraz() {
		this.imagePlus = new ImagePlus("lena_std.jpg");
		this.colorTransformOrig = new ColorTransform(
				imagePlus.getBufferedImage());
		this.colorTransform = new ColorTransform(imagePlus.getBufferedImage());
		this.colorTransform.getRGB();
		colorTransformOrig.getRGB();
	}

	public ImagePlus getComponent(int component) {
		ImagePlus imagePlus = null;
		switch (component) {
		case RED:
			imagePlus = colorTransform.setImageFromRGB(
					colorTransform.getImageWidth(),
					colorTransform.getImageHeight(), colorTransform.getRed(),
					"RED");
			break;
		// podobnì vytvoøte case pro GREEN a BLUE
		case GREEN:
			imagePlus = colorTransform.setImageFromRGB(
					colorTransform.getImageWidth(),
					colorTransform.getImageHeight(), colorTransform.getGreen(),
					"GREEN");
			break;
		case BLUE:
			imagePlus = colorTransform.setImageFromRGB(
					colorTransform.getImageWidth(),
					colorTransform.getImageHeight(), colorTransform.getBlue(),
					"BLUE");
			break;
		case Y:
			imagePlus = colorTransform.setImageFromRGB(colorTransform.getY()
					.getColumnDimension(), colorTransform.getY()
					.getRowDimension(), colorTransform.getY(), "Y");
			break;
		case CB:
			imagePlus = colorTransform.setImageFromRGB(colorTransform.getcB()
					.getColumnDimension(), colorTransform.getcB()
					.getRowDimension(), colorTransform.getcB(), "Cb");
			break;
		case CR:
			imagePlus = colorTransform.setImageFromRGB(colorTransform.getcR()
					.getColumnDimension(), colorTransform.getcR()
					.getRowDimension(), colorTransform.getcR(), "Cr");
		default:
			break;
		}
		return imagePlus;
	}

	public void downsample(int downsampleType) {
		colorTransform.convertRgbToYcbcr();
		Matrix cB = new Matrix(colorTransform.getcB().getArray());
		Matrix cR = new Matrix(colorTransform.getcR().getArray());
		switch (downsampleType) {
		case S444:
			break;
		case S422:
			cB = colorTransform.downsample(cB);
			colorTransform.setcB(cB);

			cR = colorTransform.downsample(cR);
			colorTransform.setcR(cR);
			break;

		case S420:
			cB = colorTransform.downsample(cB);
			cB = cB.transpose();
			cB = colorTransform.downsample(cB);
			cB = cB.transpose();
			colorTransform.setcB(cB);

			cR = colorTransform.downsample(cR);
			cR = cR.transpose();
			cR = colorTransform.downsample(cR);
			cR = cR.transpose();
			colorTransform.setcR(cR);
			break;

		case S411:
			cB = colorTransform.downsample(cB);
			colorTransform.setcB(cB);
			cB = new Matrix(colorTransform.getcB().getArray());
			cB = colorTransform.downsample(cB);
			colorTransform.setcB(cB);

			cR = colorTransform.downsample(cR);
			colorTransform.setcR(cR);
			cR = new Matrix(colorTransform.getcR().getArray());
			cR = colorTransform.downsample(cR);
			colorTransform.setcR(cR);
			break;
		}
	}

	public void oversample(int oversample) {
		Matrix cB;
		Matrix cR;
		switch (oversample) {
		case S444:

			break;
		case S422:
			cB = new Matrix(colorTransform.getcB().getArray());
			cB = colorTransform.oversample(cB);
			colorTransform.setcB(cB);
			cR = new Matrix(colorTransform.getcR().getArray());
			cR = colorTransform.oversample(cR);
			colorTransform.setcR(cR);
			break;

		case S420:
			cB = new Matrix(colorTransform.getcB().getArray()).transpose();
			cB = colorTransform.oversample(cB);
			colorTransform.setcB(cB);
			cB = new Matrix(colorTransform.getcB().getArray()).transpose();
			cB = colorTransform.oversample(cB);
			colorTransform.setcB(cB);

			cR = new Matrix(colorTransform.getcR().getArray()).transpose();
			cR = colorTransform.oversample(cR);
			colorTransform.setcR(cR);
			cR = new Matrix(colorTransform.getcR().getArray()).transpose();
			cR = colorTransform.oversample(cR);
			colorTransform.setcR(cR);
			break;

		case S411:
			cB = new Matrix(colorTransform.getcB().getArray());
			cB = colorTransform.oversample(cB);
			colorTransform.setcB(cB);
			cB = new Matrix(colorTransform.getcB().getArray());
			cB = colorTransform.oversample(cB);
			colorTransform.setcB(cB);

			cR = new Matrix(colorTransform.getcR().getArray());
			cR = colorTransform.oversample(cR);
			colorTransform.setcR(cR);
			cR = new Matrix(colorTransform.getcR().getArray());
			cR = colorTransform.oversample(cR);
			colorTransform.setcR(cR);
			break;

		default:
			break;
		}
		colorTransform.convertYcbcrToRgb();
	}

	public double getMse() {
		colorTransform.convertYcbcrToRgb();
		quality = new Quality();
		double a = quality.getMse(colorTransformOrig.getRed(),
				colorTransform.getRed());
		double b = quality.getMse(colorTransformOrig.getGreen(),
				colorTransform.getGreen());
		double c = quality.getMse(colorTransformOrig.getBlue(),
				colorTransform.getBlue());
		return ((a + b + c) / 3.0);

	}

	public double getPsnr() {
		colorTransform.convertYcbcrToRgb();
		quality = new Quality();
		double result = quality.getPsnr(colorTransformOrig.getRed(),
				colorTransform.getRed());
		return result;
	}

	// Ovládání grafických komponent
	public void yButtonPressed(ActionEvent event) {
		getComponent(Process.Y).show("Y Component");
	}

	public void cbButtonPressed(ActionEvent event) {
		getComponent(Process.CB).show("Cb Component");
	}

	public void crButtonPressed(ActionEvent event) {
		getComponent(Process.CR).show("Cr Component");
	}

	public void rButtonPressed(ActionEvent event) {
		getComponent(Process.RED).show("Red Component");
	}

	public void gButtonPressed(ActionEvent event) {
		getComponent(Process.GREEN).show("Green Component");
	}

	public void bButtonPressed(ActionEvent event) {
		getComponent(Process.BLUE).show("Blue Component");
	}

	public void dS444ButtonPressed(ActionEvent event) {
		nactiOrigObraz();
		vzorkovani = S444;
		downsample(S444);
	}

	public void dS422ButtonPressed(ActionEvent event) {
		nactiOrigObraz();
		vzorkovani = S422;
		downsample(S422);
	}

	public void dS420ButtonPressed(ActionEvent event) {
		nactiOrigObraz();
		vzorkovani = S420;
		downsample(S420);
	}

	public void dS411ButtonPressed(ActionEvent event) {
		nactiOrigObraz();
		vzorkovani = S411;
		downsample(S411);
	}

	public void overSampleButtonPressed(ActionEvent event) {
		oversample(vzorkovani);
	}

	public void qualityButtonPressed(ActionEvent event) {
		psnrLabel.setText("PSNR = " + getPsnr());
		mseLabel.setText("MSE = " + getMse());
	}

	// implementovany blokove transformace
	public void dctTransform(ActionEvent event) {
		Matrix yPom = new Matrix(512, 512);
		Matrix cbPom = new Matrix(512, 512);
		Matrix crPom = new Matrix(512, 512);
		yPom = colorTransform.blockTransformation(false, colorTransform.getY(),
				8, (int) slider.getValue(), true);
		cbPom = colorTransform.blockTransformation(false,
				colorTransform.getcB(), 8, (int) slider.getValue(), false);
		crPom = colorTransform.blockTransformation(false,
				colorTransform.getcR(), 8, (int) slider.getValue(), false);
		colorTransform.setY(yPom);
		colorTransform.setcB(cbPom);
		colorTransform.setcR(crPom);
	}

	public void idctTransform(ActionEvent event) {
		// Matrix dct = new TransformMatrix().getDctMatrix(512);
		Matrix yPom = new Matrix(512, 512);
		Matrix cbPom = new Matrix(512, 512);
		Matrix crPom = new Matrix(512, 512);
		yPom = colorTransform.inverseBlockTransformation(false,
				colorTransform.getY(), 8, (int) slider.getValue(), true);
		cbPom = colorTransform.inverseBlockTransformation(false,
				colorTransform.getcB(), 8, (int) slider.getValue(), false);
		crPom = colorTransform.inverseBlockTransformation(false,
				colorTransform.getcR(), 8, (int) slider.getValue(), false);
		colorTransform.setY(yPom);
		colorTransform.setcB(cbPom);
		colorTransform.setcR(crPom);
	}

	// tohle nefunguje
	public void whtTransform(ActionEvent event) {
		Matrix yPom = new Matrix(512, 512);
		Matrix cbPom = new Matrix(512, 512);
		Matrix crPom = new Matrix(512, 512);
		yPom = colorTransform.blockTransformation(true, colorTransform.getY(),
				8, (int) slider.getValue(), true);
		cbPom = colorTransform.blockTransformation(true,
				colorTransform.getcB(), 8, (int) slider.getValue(), false);
		crPom = colorTransform.blockTransformation(true,
				colorTransform.getcR(), 8, (int) slider.getValue(), false);
		colorTransform.setY(yPom);
		colorTransform.setcB(cbPom);
		colorTransform.setcR(crPom);
	}

	public void iWhtTransform(ActionEvent event) {
		Matrix yPom = new Matrix(512, 512);
		Matrix cbPom = new Matrix(512, 512);
		Matrix crPom = new Matrix(512, 512);
		yPom = colorTransform.inverseBlockTransformation(true,
				colorTransform.getY(), 8, (int) slider.getValue(), true);
		cbPom = colorTransform.inverseBlockTransformation(true,
				colorTransform.getcB(), 8, (int) slider.getValue(), false);
		crPom = colorTransform.inverseBlockTransformation(true,
				colorTransform.getcR(), 8, (int) slider.getValue(), false);
		colorTransform.setY(yPom);
		colorTransform.setcB(cbPom);
		colorTransform.setcR(crPom);
	}

	public void showResult() {
		colorTransform.convertYcbcrToRgb();
		colorTransform.setImageFromRGB(colorTransform.getRed().length,
				colorTransform.getRed()[0].length, colorTransform.getRed(),
				colorTransform.getGreen(), colorTransform.getBlue()).show(
				"Transformed Image");
	}
}
