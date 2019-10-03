package trialtops;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import port.Histogram;
import tops.engine.TParser;
 
/**
 * @web http://java-buddy.blogspot.com
 */
public class CompressionHistogram extends Application {
    
    StackPane root;
     
    Histogram histogram;
    
    DiagramPane dp1;
    DiagramPane dp2;
    
    Map<String, String> golden;
    Map<String, String> port;
     
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
         
        String baseDir = "/home/gilleain/Data/topsstrings/";
        File goldenFile = new File(baseDir, "golden.txt");
        File portFile = new File(baseDir, "port.txt");
        golden = toMap(goldenFile);
        port = toMap(portFile);
        
        histogram = getData();
         
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> barChart = new BarChart<>(xAxis,yAxis);
        barChart.setCategoryGap(0);
        barChart.setBarGap(0);
         
        xAxis.setLabel("Compression");       
        yAxis.setLabel("Count");
         
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Histogram");       
        
        for (Histogram.Bin bin : histogram) {
            series1.getData().add(new XYChart.Data<>(bin.rangeLabel(), bin.size()));
        }
        barChart.getData().add(series1);
         
        ListView<String> itemList = new ListView<>();
        itemList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        itemList.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> showStrings(newSelection));
        
        HBox hBox = new HBox();
//        JmolPane jmolPane = new JmolPane();
        VBox vBox = new VBox();
        dp1 = new DiagramPane(500, 300);
        dp2 = new DiagramPane(500, 300);
        vBox.getChildren().addAll(dp1, dp2);
        hBox.getChildren().addAll(barChart, itemList, vBox);
         
        root = new StackPane();
        root.getChildren().add(hBox);
         
        Scene scene = new Scene(root, 1200, 500);
         
        primaryStage.setTitle("Compression Counts");
        primaryStage.setScene(scene);
        
        for (XYChart.Series<String, Number> s : barChart.getData()) {
            for (XYChart.Data<String, Number> item : s.getData()) {
                item.getNode().setOnMousePressed(e -> {
                    Histogram.Bin bin = histogram.getBin(item.getXValue());
                    System.out.println("you clicked " + bin.rangeLabel());
                    ObservableList<String> items = FXCollections.observableArrayList();
                    for (String value : bin.values) {
                        items.add(value.trim());
                    }
                    itemList.setItems(items);
                });
            }
        }
        
        primaryStage.show();
    }
    
    private Map<String, String> toMap(File file) throws FileNotFoundException {
        Map<String, String> map = new HashMap<>();
        
        try (BufferedReader r =new BufferedReader(new FileReader(file))) {
            for (String line : r.lines().collect(Collectors.toList())) {
                String[] s = line.split(" ");
                map.put(s[0], line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
    
    public void showStrings(String s) {
        System.out.println("Showing [" + s + "]");
        String gString = golden.get(s);
        String pString = port.get(s);
        set(gString, dp1);
        set(pString, dp2);
        root.requestLayout();
    }
    
    private void set(String tString, DiagramPane pane) {
        TParser parser = new TParser();
        parser.load(tString);
        pane.setData(parser.getVertexString(), parser.getEdgeString());
        pane.requestLayout();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
    
    private Histogram getData() {
        String dir = "/home/gilleain/Code/public/tops";
        String trialLog = "port_comparison.txt";
        
        // get the last line of the file for now...
        File file = new File(dir, trialLog);
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            line = reader.readLine();
            return Histogram.fromString(line);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
    }
}
