import javax.swing.JFrame;

public class AppFrame extends JFrame{
    AppFrame(){
        setTitle("Music Player");
        setSize(1000,1000);
        setLocationRelativeTo(null);
        AppPanel aPanel = new AppPanel();
        add(aPanel);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
