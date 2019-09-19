import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/***
 *
 */
public class Frame extends JFrame {

    private JTextField enterRow;
    private JTextField enterColumn;
    private JLabel mainLabel;
    private JLabel imgLabel;
    private MainPuzzle puzzle;

    // todo pass these as constructors to puzzle class
    static int rowSize;
    static int columnSize;
    static BufferedImage source;

    private final int boxWidth = 1000;
    private final int boxHeight = 1000;

    public Frame() {

        this.setBounds(0, 0, boxWidth, boxHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setLocationRelativeTo(null);

        mainLabel = new JLabel("READY TO PLAY!", SwingConstants.CENTER);
        mainLabel.setForeground(Color.RED);
        mainLabel.setFont(new Font("Georgia", Font.BOLD, 60));
        mainLabel.setBounds(boxWidth/10,boxHeight/5, boxWidth-250, boxHeight/10);
        this.add(mainLabel);

        enterRow = new JTextField("ENTER ROW");
        enterRow.setHorizontalAlignment(JTextField.CENTER);
        enterRow.setFont(new Font ("Gerogia", Font.BOLD, 30));
        enterRow.setBounds(boxWidth/10, boxHeight/5 + 150, 275, 75);
        this.add(enterRow);

        enterColumn = new JTextField("ENTER COLUMN");
        enterColumn.setHorizontalAlignment(JTextField.CENTER);
        enterColumn.setFont(new Font ("Gerogia", Font.BOLD, 30));
        enterColumn.setBounds(boxWidth/10, boxHeight/5 + 250, 275, 75);
        this.add(enterColumn);

        JButton loadImage = new JButton("LOAD IMAGE");
        loadImage.setFont(new Font("Georgia", Font.BOLD, 30));
        loadImage.setForeground(Color.RED);
        loadImage.setBounds(500,boxHeight/5 + 150,275,75);
        loadImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(new File (System.getProperty("user.home") + "/Pictures" ));
                fileChooser.setPreferredSize(new Dimension(1500, 1000));
                setFileChooserFont(fileChooser.getComponents());
                FileNameExtensionFilter filter = new FileNameExtensionFilter("allow only image files to get loaded","png", "jpg");
                fileChooser.addChoosableFileFilter(filter);

                int returnVal = fileChooser.showOpenDialog(Frame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    try {
                        source = ImageIO.read(file);
                        imgLabel.setText(file.getName());
                    } catch(IOException ex){
                        System.out.println("Image Not found");
                    }
                }
            }
        });
        this.add(loadImage);

        imgLabel = new JLabel("");
        imgLabel.setFont(new Font("Georgia", Font.BOLD, 30));
        imgLabel.setBounds(500,boxHeight/5 + 250, 275, 75);
        this.add(imgLabel);

        JButton btnStartPuzzle = new JButton("LOAD PUZZLE");
        btnStartPuzzle.setFont(new Font("Georgia", Font.BOLD, 30));
        btnStartPuzzle.setBounds(350, boxHeight - 350, 275, 75);
        btnStartPuzzle.setForeground(Color.RED);
        btnStartPuzzle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    rowSize = Integer.parseInt(enterRow.getText());
                    columnSize = Integer.parseInt(enterColumn.getText());
                    puzzle = new MainPuzzle();
                    puzzle.setTitle("MainPuzzle");
                    puzzle.setResizable(false);
                    puzzle.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    puzzle.setVisible(true);
                    puzzle.pack();
                    puzzle.setLocationRelativeTo(null);
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Please give a valid input", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.add(btnStartPuzzle);


    }
    /***
     * private method to update font sizes of JFileChooser.
     * Used for convenience.
     *
     * Todo: update sizes of icons too. Right now only updates text sizes
     */
    private void setFileChooserFont (Component [] componenets) {
        for (Component comp : componenets) {
            if (comp instanceof Container) {
                setFileChooserFont(((Container)comp).getComponents());
            }
            try {
                comp.setFont(comp.getFont().deriveFont(comp.getFont().getSize()*3f));
            } catch (Exception e){}
        }
    }

}

