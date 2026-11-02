import javax.swing.*;
public class DataStreamRunner
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception ex)
            {
            }
            DataStreamFrame frame = new DataStreamFrame();
            frame.setTitle("Data Stream File Search");
            frame.setSize(1100, 700);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
