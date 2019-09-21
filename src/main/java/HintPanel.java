import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class HintPanel extends JPanel {
    private JButton showImageButton;
    private JLabel imageLabel;
    private BufferedImage source = Frame.source;

    public HintPanel(int gameWidth) {
        this.setLayout(new GridLayout(2,0));

        this.imageLabel = new JLabel();
        this.imageLabel.setPreferredSize(new Dimension(gameWidth/2,gameWidth/2));
        this.add(imageLabel);

        this.showImageButton = new JButton("DISPLAY ORDERED IMAGE");
        this.showImageButton.setFont(new Font("Georgia", Font.BOLD, 30));
        this.showImageButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                imageLabel.setIcon(resizeForLabel());
            }
        });
        this.add(showImageButton);
    }

    //resize image to display for user on a JLabel
    private ImageIcon resizeForLabel(){
        Image image1 = source.getScaledInstance(this.imageLabel.getWidth(),this.imageLabel.getHeight(),Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(image1);
        return image;
    }
}
