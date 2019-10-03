package trialtops;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolViewer;

import javafx.embed.swing.SwingNode;
import javafx.scene.layout.Region;

public class JmolPane extends Region {
    
    private SwingNode swingNode;
    
    public JmolPane() {
        swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);
    }
    
    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JmolPanel panel = new JmolPanel();
                swingNode.setContent(panel);
            }
        });
    }
    
    // xxx nasty - can we not just use javafx?
    private class JmolPanel extends JPanel {
        
        public JmolViewer viewer;
        private final Dimension currentSize = new Dimension(500, 500);
        
        public JmolPanel() {
            this.viewer = JmolViewer.allocateViewer(this, new SmarterJmolAdapter());
            this.viewer.setStringProperty("backgroundColor", "white");
            this.setPreferredSize(this.currentSize);
        }
        
        @Override
        public void paintComponent(Graphics g) {
            int width = (int) currentSize.getWidth();
            int height = (int) currentSize.getHeight();
            this.viewer.renderScreenImage(g, width, height);
        }
        
    }

}
