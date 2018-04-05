import com.mathworks.engine.MatlabEngine;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by maxtar.
 */
public class Synthesis extends Application {

    @Override
    public void start(Stage stage) throws Exception {
//        showWithPerturbations(stage);
        showWithoutPerturbations(stage);
    }

    private void showWithPerturbations(Stage stage) throws Exception {
        stage.setTitle("Synthesis including perturbations");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("length");
        yAxis.setLabel("height");
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        MatlabEngine eng = MatlabEngine.startMatlab();
        eng.eval("addpath('/home/maxtar/matlab2017b/MATLABR2017b/scripts')");

        MessageFormat base = new MessageFormat("[l, h] = synthesisIncludingPerturbations{0};");
        List<String> options = new ArrayList<>(Arrays.asList(
                //(G, Q, Qv,           Qvv,      t0, tM)
                "(10, 0, 0.03,         0,        0,  40 )",
                "(10, 0, 0,            0.00003,  0,  35 )",
                "(10, 0, 0,            0.00006,  0,  52 )",
                "(10, 0, 0.0471238898, 0,        0,  91 )",
                "(0,  0, 0,            0.000078, 0,  107)",
                "(40, 0, 0,            0.000003, 0,  23 )"));

        for (String option : options) {
            System.out.println((options.indexOf(option) + 1) + " from " + options.size());
            String eval = base.format(new Object[]{option});
            eng.eval(eval);
            double[] l = eng.getVariable("l");
            double[] h = eng.getVariable("h");
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(String.valueOf(options.indexOf(option) + 1));
            for (int i = 0; i < l.length; i++) {
                series.getData().add(new XYChart.Data<>(l[i], h[i]));
            }
            lineChart.getData().add(series);
        }

        eng.eval("rmpath('/home/maxtar/matlab2017b/MATLABR2017b/scripts')");
        eng.close();

        Scene scene = new Scene(lineChart, 1000, 900);
        stage.setScene(scene);
        stage.show();
    }

    private void showWithoutPerturbations(Stage stage) throws Exception {
        stage.setTitle("Synthesis excluding perturbations");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("length");
        yAxis.setLabel("height");
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        MatlabEngine eng = MatlabEngine.startMatlab();
        eng.eval("addpath('/home/maxtar/matlab2017b/MATLABR2017b/scripts')");

        MessageFormat base = new MessageFormat("[l, h] = synthesisExcludingPerturbations{0};");
        List<String> options = new ArrayList<>(Arrays.asList(
                //(Q, Qv,           Qvv,      t0, tM)
                "(0,  0,            0,        0,  29 )",
                "(0,  0.03,         0,        0,  58 )",
                "(0,  0,            0.000078, 0,  111)"));

        for (String option : options) {
            System.out.println((options.indexOf(option) + 1) + " from " + options.size());
            String eval = base.format(new Object[]{option});
            eng.eval(eval);
            double[] l = eng.getVariable("l");
            double[] h = eng.getVariable("h");
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(String.valueOf(options.indexOf(option) + 1));
            for (int i = 0; i < l.length; i++) {
                series.getData().add(new XYChart.Data<>(l[i], h[i]));
            }
            lineChart.getData().add(series);
        }

        eng.eval("rmpath('/home/maxtar/matlab2017b/MATLABR2017b/scripts')");
        eng.close();

        Scene scene = new Scene(lineChart, 1000, 900);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}
