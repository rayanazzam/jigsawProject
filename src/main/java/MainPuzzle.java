import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainPuzzle extends JFrame {

    private int rowSize;
    private int colSize;
    private int totalButtons;
    private JPanel mainPanel;
    private HintPanel hintPanel;
    private BufferedImage source = Frame.source;
    private BufferedImage resizedSourceImage;
    private PuzzleButton lastButton;
    private int width, height;
    private ArrayList<PuzzleButton> buttonsList;
    private ArrayList<Integer> solution;

    private int gameWidth = 1000;

    public MainPuzzle(int rowSize, int colSize) {
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.totalButtons = this.rowSize * this.colSize;

        populateSolutionList();
        this.buttonsList = new ArrayList<>();

        this.mainPanel = new JPanel();
        this.mainPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        this.mainPanel.setLayout(new GridLayout(rowSize,colSize, 0, 0));

        this.hintPanel = new HintPanel(this.gameWidth);
        this.add(hintPanel, BorderLayout.EAST);

        try {
            int h = getNewHeight (source.getWidth(), source.getHeight());
            this.resizedSourceImage = resizeImage(source, gameWidth, h,
                    BufferedImage.TYPE_INT_ARGB);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not load image", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        this.width = resizedSourceImage.getWidth(null);
        this.height = resizedSourceImage.getHeight(null);

        this.add(mainPanel, BorderLayout.CENTER);

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                PuzzleButton button = new PuzzleButton();
                button.setIcon(new ImageIcon( putImageOnButton(j, i)));
                button.putClientProperty("position", i * colSize + j);

                if (i == rowSize-1 && j == colSize-1) {
                    lastButton = new PuzzleButton();
                    lastButton.setBorderPainted(false);
                    lastButton.setContentAreaFilled(false);
                    lastButton.setLastButton();
                    lastButton.putClientProperty("position", i * colSize + j);
                } else {
                    buttonsList.add(button);
                }
            }
        }

        Collections.shuffle(buttonsList);
        ensureSolvability();
        buttonsList.add(lastButton);

        for (int i = 0; i < totalButtons; i++) {
            PuzzleButton btn = buttonsList.get(i);
            btn.addActionListener(new ClickAction());
            mainPanel.add(btn);
        }
    }

    //returns height of image in proportion to our set width
    private int getNewHeight(int origImageWidth, int origImageHeight) {
        double ratio = gameWidth / (double) origImageWidth;
        int newHeight = (int) (origImageHeight * ratio);
        return newHeight;
    }

    //returns resizedSourceImage image
    private BufferedImage resizeImage(BufferedImage originalImage, int width,
                                      int height, int type) throws IOException {
        BufferedImage resizedSourceImageImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedSourceImageImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        return resizedSourceImageImage;
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

    //compares current indices of button list against solution list
    private void checkSolution() {
        ArrayList<Integer> current = new ArrayList<>();
        for (JButton btn : buttonsList) {
            current.add((Integer) btn.getClientProperty("position"));
        }
        if (solution.toString().equals(current.toString())) {
            JOptionPane.showMessageDialog(mainPanel, "CONGRATULATIONS!",
                    "GAME WON!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    // simple maths to crop image at point (x, y) for a particular button at (x, y)
    private Image putImageOnButton (int x, int y) {
        return createImage(new FilteredImageSource
                (resizedSourceImage.getSource(),
                        new CropImageFilter(x * width / colSize, y * height / rowSize, (width / colSize), height / rowSize)));
    }
    // populate solution list with proper index
    private void populateSolutionList () {
        this.solution = new ArrayList<>();
        for (int i = 0; i < this.totalButtons; i ++) {
            this.solution.add(i);
        }
    }

    /***
     * Random shuffling of buttons has 50% chance that it may not be solvable.
     * This happens when the inversion count of the total puzzle, which is
     * the sum of mislocations of each button piece has an odd polarity.
     * If it does, you need to make one swap to reverse polarity to even
     */
    private void ensureSolvability () {
        int inversionCount = 0;

        for (int i = 0; i < this.totalButtons-1; i ++) {
            int bPosition = (Integer) this.buttonsList.get(i).getClientProperty("position");
            if (bPosition > i) {
                inversionCount += (bPosition - i);
            }
        }
        //check polarity
        if (inversionCount % 2 == 1) {
            Collections.swap (this.buttonsList, 0, 1);
        }
    }
}
