package gui;

//import com.sun.codemodel.internal.JOp;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import log.Logger;
import sun.awt.WindowClosingListener;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                onClose();
            }
        });
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void onClose() {
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        int select = JOptionPane.showConfirmDialog(this, "Закрыть программу?", "Программа", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (select == 0)
            System.exit(0);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = getJMenu("Режим отображения", KeyEvent.VK_V, "Управление режимом отображения приложения");

        addMenuItem(lookAndFeelMenu, "Изначальная схема", (event) -> {
            setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            this.invalidate();
        }, KeyEvent.VK_1);

        addMenuItem(lookAndFeelMenu, "Системная схема", (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        }, KeyEvent.VK_2);

        addMenuItem(lookAndFeelMenu, "Белая схема", (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        }, KeyEvent.VK_3);

        JMenu testMenu = getJMenu("Тесты", KeyEvent.VK_T, "Тестовые команды");

        addMenuItem(testMenu, "Сообщение в лог", (event) -> {
            Logger.debug("Новая строка");
        }, KeyEvent.VK_N);

        JMenu documentMenu = getJMenu("Документ", KeyEvent.VK_C, "Работа с документом");

        addMenuItem(documentMenu, "Загрузить логику робота", (event) -> changeLogic(), KeyEvent.VK_L);
        addMenuItem(documentMenu, "Закрыть", (event) -> onClose(), KeyEvent.VK_Q);

        menuBar.add(documentMenu);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }

    private void changeLogic() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Robot logic class (.class)", "class"));
        chooser.setCurrentDirectory(new File("."));
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == chooser.APPROVE_OPTION)
            System.out.println(chooser.getSelectedFile());




    }

    private void addMenuItem(JMenu lookAndFeelMenu, String name, ActionListener actionListener, int keyEvent) {
        JMenuItem menuItem = new JMenuItem(name, keyEvent);
        menuItem.addActionListener(actionListener);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                keyEvent, ActionEvent.ALT_MASK));
        lookAndFeelMenu.add(menuItem);
    }

    private JMenu getJMenu(String name, int underlinedKey, String description) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(underlinedKey);
        menu.getAccessibleContext().setAccessibleDescription(
                description);
        return menu;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
        }
    }
}
