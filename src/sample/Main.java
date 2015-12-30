package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.elfkites.L;
import ru.elfkites.formdesign.SectionsWidthProportionalNervuresLengths;
import ru.elfkites.math.MathUtil;
import ru.elfkites.model.KiteForm;
import ru.elfkites.parsers.KiteFormParser;

import java.util.Arrays;

public class Main extends Application {


    public static final int WIDTH = 1200;
    public static final int HEIGHT = 600;
    public static final int SCALE = 100;
    public static final double DOT_SIZE = 4f / SCALE;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        draw(graphicsContext2D);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void draw(GraphicsContext gc) {
        String formeFilePath = "../elfkite_design/input/f8_c61.txt";
        KiteForm kiteForm = new KiteFormParser(formeFilePath).parse();
        L.d("KiteForm: " + kiteForm.toString());
        L.d("KiteForm.front x " + Arrays.toString(kiteForm.frontEdgeX)
                + " y " + Arrays.toString(kiteForm.frontEdgeY));


        L.d("KiteForm.front x " + Arrays.toString(kiteForm.backEdgeX)
                + " y " + Arrays.toString(kiteForm.backEdgeY));


        int n = 20;
        double[] frontEdgeCurveX = new double[n];
        double[] frontEdgeCurveY = new double[n];
        double[] backEdgeCurveX = new double[n];
        double[] backEdgeCurveY = new double[n];

        MathUtil.bezierCurve(kiteForm.frontEdgeX, kiteForm.frontEdgeY, n, frontEdgeCurveX, frontEdgeCurveY);
        MathUtil.bezierCurve(kiteForm.backEdgeX, kiteForm.backEdgeY, n, backEdgeCurveX, backEdgeCurveY);
        gc.translate(WIDTH / 2, HEIGHT / 2);
        gc.scale(SCALE, -SCALE);
        float px = 1f / SCALE;
        gc.setLineWidth(px);

        gc.setStroke(Color.GRAY);
        for (int i = -100; i < 100; i++) {
            gc.strokeLine(-100f, i, 100, i);
            gc.strokeLine(i, -100f, i, 100);
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2 * px);
        gc.strokeLine(-100f, 0, 100, 0);
        gc.strokeLine(0f, -100, 0, 100);

        gc.setLineWidth(px);
        for (int i = 0; i < n - 1; i++) {
            double x = frontEdgeCurveX[i];
            double y = frontEdgeCurveY[i];
            gc.setStroke(Color.GREEN);

            gc.strokeLine(x, y, frontEdgeCurveX[i + 1], frontEdgeCurveY[i + 1]);
            dot(gc, x, y);
        }
        dot(gc, frontEdgeCurveX[n - 1], frontEdgeCurveY[n - 1]);

        for (int i = 0; i < n - 1; i++) {
            gc.setStroke(Color.BLUE);
            double x = backEdgeCurveX[i];
            double y = backEdgeCurveY[i];
            gc.strokeLine(x, y, backEdgeCurveX[i + 1], backEdgeCurveY[i + 1]);
            dot(gc, x, y);
        }
        dot(gc, backEdgeCurveX[n - 1], backEdgeCurveY[n - 1]);

        double xend = kiteForm.frontEdgeX[kiteForm.frontEdgeX.length - 1];
        double step = 2 * xend / kiteForm.n;
        for (double x = 0; x <= xend; x += step) {
            double frontY = MathUtil.interpLin(frontEdgeCurveX, frontEdgeCurveY, x);
            double backY = MathUtil.interpLin(backEdgeCurveX, backEdgeCurveY, x);
            gc.setStroke(Color.MAGENTA);
            gc.strokeLine(x, frontY, x, backY);
        }

    }

    private void dot(GraphicsContext gc, double x, double y) {
        gc.setStroke(Color.RED);
        gc.strokeOval(x - DOT_SIZE / 2, y - DOT_SIZE / 2, DOT_SIZE, DOT_SIZE);
    }

    public static void main(String[] args) {
        launch(args);

    }
}
