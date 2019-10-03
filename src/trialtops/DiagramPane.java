package trialtops;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import tops.view.diagram.DiagramDrawer;

public class DiagramPane extends Region {
    
    private Canvas canvas;
    
    private String vertexString;
    
    private String edgeString;
    
    public DiagramPane(int w, int h){ 
        this.canvas = new Canvas(w, h);
        getChildren().add(canvas);
    }
    
    public DiagramPane(int w, int h, String vertexString, String edgeString) {
        this(w, h);
        setData(vertexString, edgeString);
    }
    
    public void setData(String vertexString, String edgeString) {
        this.vertexString = vertexString;
        this.edgeString = edgeString;
    }
    
    @Override
    protected void layoutChildren() {
        if (vertexString == null || edgeString == null) return; // XXX?
        double w = getWidth() - getPadding().getLeft() - getPadding().getRight() ;
        double h = getHeight() - getPadding().getTop() - getPadding().getBottom() ;

        canvas.setWidth(w+1);
        canvas.setHeight(h+1);

        canvas.setLayoutX(getPadding().getLeft());
        canvas.setLayoutY(getPadding().getRight());
        
//        System.out.println("w " + w + " h = " + h);
        if (w > 0 && h > 0) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, w, h);
            gc.drawImage(getImage(w, h), 0, 0);
        }
    }
    
    public WritableImage getImage(double w, double h) {
        DiagramDrawer dd = new DiagramDrawer(vertexString, edgeString, null, (int)w, (int)h);
        BufferedImage bufferedImage = new BufferedImage((int)w, (int)h, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.getGraphics();
        dd.paint(graphics);
        
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

}
