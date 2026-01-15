import UI.UIStyle;

public class App {
    static void main(String[] args) {
        UIStyle.setDefaultsTheme();
        new UI.LoginFrame().setVisible(true);
    }
}