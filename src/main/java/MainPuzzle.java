import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Dimension;

public class MainPuzzle extends JFrame {

    private int rowSize = Frame.rowSize;
    private int colSize = Frame.columnSize;
    private int totalButtons = rowSize*colSize;
    private int gameWidth = 500;
    private JPanel mainPanel;
    private JPanel secondPanel;
    private BufferedImage source = Frame.source;
    private BufferedImage resized;
    private Image image;
    private JLabel label;
    private PuzzleButton lastButton;
    private int width, height;
    private JButton imageButton;
    private ArrayList<PuzzleButton> buttonsList;
    private ArrayList<Point> solution;

    public MainPuzzle() {
        solution = new ArrayList<>();
        for(int i = 0; i < rowSize; i ++){
            for(int j = 0;j < colSize;j ++)
                solution.add(new Point(i,j));
        }

        buttonsList = new ArrayList<>();
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        mainPanel.setLayout(new GridLayout(rowSize,colSize, 0, 0));
        secondPanel=new JPanel();
        secondPanel.setLayout(new GridLayout(2,0));
        label=new JLabel();
        label.setPreferredSize(new Dimension(200,150));
        imageButton = new JButton("Display ordered Image");
        imageButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        label.setIcon(resizeForLabel(source));
                    }
            });
        secondPanel.add(label);
        secondPanel.add(imageButton);

        try {

            int h = getNewHeight(source.getWidth(), source.getHeight());
            resized = resizeImage(source, gameWidth, h,
                    BufferedImage.TYPE_INT_ARGB);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not load image", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        width = resized.getWidth(null);
        height = resized.getHeight(null);

        add(mainPanel, BorderLayout.CENTER);
        add(secondPanel,BorderLayout.EAST);

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                image = createImage(new FilteredImageSource(resized.getSource(),new CropImageFilter(j * width / colSize, i * height / rowSize,(width / colSize), height / rowSize)));

                PuzzleButton button = new PuzzleButton();
                button.setIcon(new ImageIcon(image));
                button.putClientProperty("position", new Point(i, j));

                if (i == rowSize-1 && j == colSize-1) {
                    lastButton = new PuzzleButton();
                    lastButton.setBorderPainted(false);
                    lastButton.setContentAreaFilled(false);
                    lastButton.setLastButton();
                    lastButton.putClientProperty("position", new Point(i, j));
                } else {
                    buttonsList.add(button);
                }
            }
        }

        Collections.shuffle(buttonsList);
        buttonsList.add(lastButton);

        for (int i = 0; i < totalButtons; i++) {
            PuzzleButton btn = buttonsList.get(i);
            mainPanel.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(Color.gray));
            btn.addActionListener(new ClickAction());
        }
    }

    //returns height of image in proportion to our set width
    private int getNewHeight(int w, int h) {
        double ratio = gameWidth / (double) w;
        int newHeight = (int) (h * ratio);
        return newHeight;
    }

    //returns resized image
    private BufferedImage resizeImage(BufferedImage originalImage, int width,
                                      int height, int type) throws IOException {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        return resizedImage;
    }

    //resizes image to display for user on a JLabel
    private ImageIcon resizeForLabel(BufferedImage img){
        Image image1 = img.getScaledInstance(label.getWidth(),label.getHeight(),Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(image1);
        return image;
    }

    private class ClickAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            moveButton(e);
            checkSolution();
        }
        /* Method checks if current button is directly above
         * directly below, or to the right, or to the left of
         * the last button
         */
        private void moveButton(ActionEvent e) {
            int lidx = 0;
            for (PuzzleButton button : buttonsList) {
                if (button.isLastButton()) {
                    lidx = buttonsList.indexOf(button);
                }
            }

            JButton button = (JButton) e.getSource();
            int bidx = buttonsList.indexOf(button);
            if ((bidx - 1 == lidx) || (bidx + 1 == lidx)|| (bidx - colSize == lidx) || (bidx + colSize == lidx)) {
                Collections.swap(buttonsList, bidx, lidx);
                updateButtons();
            }
        }
        private void updateButtons() {
            mainPanel.removeAll();
            for (JButton btn : buttonsList) {
                mainPanel.add(btn);
            }
            mainPanel.validate();
        }
    }

    private void checkSolution() {
        List<Point> current = new ArrayList<>();
        for (JButton btn : buttonsList) {
            current.add((Point) btn.getClientProperty("position"));
        }
        if (solution.toString().contentEquals(current.toString())) {
            JOptionPane.showMessageDialog(mainPanel, "YOU WON!",
                    "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
