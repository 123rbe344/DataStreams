import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.*;
public class DataStreamFrame extends JFrame
{
    JPanel mainPnl;
    JPanel searchPnl;
    JPanel displayPnl;
    JPanel buttonPnl;
    JLabel fileLbl;
    JLabel searchLbl;
    JTextField searchTF;
    JTextArea originalTA;
    JTextArea filteredTA;
    JScrollPane originalScroller;
    JScrollPane filteredScroller;
    TitledBorder originalBorder;
    TitledBorder filteredBorder;
    JButton loadBtn;
    JButton searchBtn;
    JButton quitBtn;
    Path filePath;
    Font labelFont = new Font("SansSerif", Font.PLAIN, 15);
    Font textFont = new Font("Monospaced", Font.PLAIN, 13);
    Font buttonFont = new Font("SansSerif", Font.BOLD, 16);
    public DataStreamFrame()
    {
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());
        createSearchPanel();
        mainPnl.add(searchPnl, BorderLayout.NORTH);
        createDisplayPanel();
        mainPnl.add(displayPnl, BorderLayout.CENTER);
        createButtonPanel();
        mainPnl.add(buttonPnl, BorderLayout.SOUTH);
        add(mainPnl);
    }
    private void createSearchPanel()
    {
        searchPnl = new JPanel();
        searchPnl.setLayout(new BorderLayout(10, 5));
        searchPnl.setBorder(new TitledBorder("Search"));
        fileLbl = new JLabel("File: (none loaded)");
        fileLbl.setFont(labelFont);
        searchLbl = new JLabel("Search string:");
        searchLbl.setFont(labelFont);
        searchTF = new JTextField();
        searchTF.setFont(labelFont);
        searchTF.addActionListener(e -> searchFile());
        searchPnl.add(fileLbl, BorderLayout.NORTH);
        searchPnl.add(searchLbl, BorderLayout.WEST);
        searchPnl.add(searchTF, BorderLayout.CENTER);
    }
    private void createDisplayPanel()
    {
        displayPnl = new JPanel();
        displayPnl.setLayout(new GridLayout(1, 2, 10, 0));
        originalTA = new JTextArea();
        originalTA.setFont(textFont);
        originalTA.setEditable(false);
        originalScroller = new JScrollPane(originalTA);
        originalBorder = new TitledBorder("Original File");
        originalScroller.setBorder(originalBorder);
        filteredTA = new JTextArea();
        filteredTA.setFont(textFont);
        filteredTA.setEditable(false);
        filteredScroller = new JScrollPane(filteredTA);
        filteredBorder = new TitledBorder("Filtered Results");
        filteredScroller.setBorder(filteredBorder);
        displayPnl.add(originalScroller);
        displayPnl.add(filteredScroller);
    }
    private void createButtonPanel()
    {
        buttonPnl = new JPanel();
        buttonPnl.setLayout(new GridLayout(1, 3));
        loadBtn = new JButton("Load File");
        searchBtn = new JButton("Search File");
        quitBtn = new JButton("Quit");
        for (JButton btn : new JButton[]{loadBtn, searchBtn, quitBtn})
        {
            btn.setFont(buttonFont);
            buttonPnl.add(btn);
        }
        searchBtn.setEnabled(false);
        loadBtn.addActionListener(e -> loadFile());
        searchBtn.addActionListener(e -> searchFile());
        quitBtn.addActionListener(e -> System.exit(0));
    }
    private void loadFile()
    {
        JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.dir")));
        chooser.setDialogTitle("Choose a text file to search");
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
        {
            return;
        }
        Path chosen = chooser.getSelectedFile().toPath();
        try (Stream<String> lines = Files.lines(chosen))
        {
            originalTA.setText(lines.collect(Collectors.joining(System.lineSeparator())));
            originalTA.setCaretPosition(0);
            filePath = chosen;
            fileLbl.setText("File: " + filePath.getFileName());
            originalBorder.setTitle("Original File: " + filePath.getFileName());
            filteredBorder.setTitle("Filtered Results");
            filteredTA.setText("");
            searchBtn.setEnabled(true);
            displayPnl.repaint();
        }
        catch (IOException | UncheckedIOException ex)
        {
            JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
        }
    }
    private void searchFile()
    {
        if (filePath == null)
        {
            JOptionPane.showMessageDialog(this, "Load a file before searching.");
            return;
        }
        String searchString = searchTF.getText();
        if (searchString.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Enter a search string first.");
            return;
        }
        try (Stream<String> lines = Files.lines(filePath))
        {
            List<String> matches = lines
                    .filter(line -> line.contains(searchString))
                    .collect(Collectors.toList());
            filteredTA.setText(String.join(System.lineSeparator(), matches));
            filteredTA.setCaretPosition(0);
            filteredBorder.setTitle("Filtered Results: \"" + searchString + "\" (" + matches.size() + " lines)");
            displayPnl.repaint();
        }
        catch (IOException | UncheckedIOException ex)
        {
            JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
        }
    }
}
