package trialtops;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolViewer;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tops.view.diagram.DiagramDrawer;

public class JmolTest extends Application {
    
    public static class JmolPane {
        
        private final Pane view = new Pane();
        
        public JmolViewer viewer;
        
        final int blackArgb = 0xFF << 24 ;
        
        public JmolPane() {
            this.viewer = JmolViewer.allocateViewer(this, new SmarterJmolAdapter());
            
            WritableImage image = new WritableImage(100, 100);
            for (int i =0; i < 100; i++) {
                image.getPixelWriter().setArgb(i, i, blackArgb);
            }
            ImageView imageView = new ImageView(image);
            imageView.imageProperty().bind(new ObservableValue<Image>() {

                @Override
                public void addListener(InvalidationListener arg0) {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void removeListener(InvalidationListener arg0) {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void addListener(ChangeListener<? super Image> arg0) {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public Image getValue() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public void removeListener(ChangeListener<? super Image> arg0) {
                    // TODO Auto-generated method stub
                    
                }
                
            }); 
            view.getChildren().add(imageView);
        }
        
        public Pane getView() {
            return view;
        }
    }
    
    public WritableImage getImage(String vertices, String edges) {
        int w = 500;
        int h = 300;
        DiagramDrawer dd = new DiagramDrawer(vertices, edges, null, w, h);
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.getGraphics();
        dd.paint(graphics);
        
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        JmolPane pane = new JmolPane();
//        primaryStage.setScene(new Scene(pane.getView()));
        Pane pane = new Pane();
        VBox vBox = new VBox();
//        vBox.getChildren().add(new ImageView(getImage("NEEC", "1:2P")));
//        vBox.getChildren().add(new ImageView(getImage("NEeC", "1:2A")));
        DiagramPane d1 = new DiagramPane(300, 350, "NEEC", "1:2P");
//        d1.setPadding(new Insets(20));
        vBox.getChildren().add(d1);
        DiagramPane d2 = new DiagramPane(300, 350, "NEeC", "1:2A");
//        d2.setPadding(new Insets(20));
        vBox.getChildren().add(d2);
        primaryStage.setScene(new Scene(vBox, 700, 600));
        primaryStage.show();
    }
    

    public static void main(String[] args) {
        launch(args);
    }

}
