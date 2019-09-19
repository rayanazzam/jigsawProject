import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;

public class PuzzleButton extends JButton {

    private boolean isLastButton;

    public PuzzleButton() {
        this.isLastButton = false;
        this.setBorder(BorderFactory.createLineBorder(Color.gray));

        this.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.pink,5));
            }
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.gray));
            }
        });
    }

    public void setLastButton() {
        isLastButton = true;
    }

    public boolean isLastButton() {
        return isLastButton;
    }
}