package gui;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {
    public static void main(String[] args) {
        // Весь блок try-catch отвечает за внешний вид окна, нужен, если указанного не будет
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Запускает асинхронную обработку события
        SwingUtilities.invokeLater(() -> {
            // Создаём главное окно/рамку
            MainApplicationFrame frame = new MainApplicationFrame();
            // Как-то распределяет объекты внутри окна
            frame.pack();
            // Делает окна видимыми
            frame.setVisible(true);
            // Растягивает главное окно на весь экран
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
