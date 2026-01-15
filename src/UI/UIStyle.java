package UI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UIStyle {
    
    public static Color colorPrimary = new Color(16, 185, 129);
    public static Color colorPrimaryDark = new Color(6, 95, 70);
    public static Color colorPrimaryLight = new Color(167, 243, 208);
    
    public static Color colorBg = new Color(249, 250, 251);
    public static Color colorBgDark = new Color(243, 244, 246);
    public static Color colorBgCard = new Color(255, 255, 255);
    
    public static Color colorText = new Color(17, 24, 39);
    public static Color colorTextSecondary = new Color(107, 114, 128);
    public static Color colorLabel = new Color(55, 65, 81);
    
    public static Color colorTextField = new Color(255, 255, 255);
    public static Color colorBorder = new Color(209, 213, 219);
    public static Color colorHeader = new Color(243, 244, 246);
    
    public static Color colorSuccess = new Color(34, 197, 94);
    public static Color colorWarning = new Color(245, 158, 11);
    public static Color colorDanger = new Color(239, 68, 68);
    public static Color colorRed = new Color(239, 68, 68);
    public static Color colorInfo = new Color(59, 130, 246);
    
    public static Color colorTableHeader = new Color(236, 253, 245);
    public static Color colorTableRowEven = new Color(255, 255, 255);
    public static Color colorTableRowOdd = new Color(249, 250, 251);
    public static Color colorTableSelected = new Color(167, 243, 208);

    public static Font font12 = new Font("Segoe UI", Font.PLAIN, 12);
    public static Font font14 = new Font("Segoe UI", Font.PLAIN, 14);
    public static Font font16 = new Font("Segoe UI", Font.PLAIN, 16);
    public static Font font16Bold = new Font("Segoe UI", Font.BOLD, 16);
    public static Font font18 = new Font("Segoe UI", Font.PLAIN, 18);
    public static Font font18Bold = new Font("Segoe UI", Font.BOLD, 18);
    public static Font font20 = new Font("Segoe UI", Font.PLAIN, 20);
    public static Font font24Bold = new Font("Segoe UI", Font.BOLD, 24);
    public static Font font30 = new Font("Segoe UI", Font.BOLD, 30);
    public static Font fontEmoji = new Font("Segoe UI Emoji", Font.PLAIN, 24);

    public static void setDefaultsTheme() {
        UIManager.put("OptionPane.background", colorBg);
        UIManager.put("Panel.background", colorBg);
        UIManager.put("OptionPane.messageFont", font16);
        UIManager.put("OptionPane.buttonFont", font16Bold);
        UIManager.put("OptionPane.messageForeground", colorText);
        Border messagePad = BorderFactory.createEmptyBorder(15, 20, 10, 20);
        Border buttonPad = BorderFactory.createEmptyBorder(10, 20, 15, 20);
        UIManager.put("OptionPane.messageAreaBorder", messagePad);
        UIManager.put("OptionPane.buttonAreaBorder", buttonPad);
        
        UIManager.put("Button.background", colorPrimary);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", font16);
    }

    public static JButton setBtnActive(JButton btn, String text) {
        final JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, 45));
        button.setForeground(Color.WHITE);
        button.setBackground(colorPrimary);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(font16Bold);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setOpaque(true);
        

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(colorPrimaryDark);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(colorPrimary);
            }
        });
        return button;
    }

    public static JButton setBtnSecondary(JButton btn, String text) {
        btn = new JButton(text);
        btn.setPreferredSize(new Dimension(300, 45));
        btn.setForeground(colorPrimary);
        btn.setBackground(colorBgCard);
        btn.setFocusPainted(false);
        btn.setFont(font16);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(colorPrimary, 2));
        return btn;
    }

    public static JButton setBtnDanger(JButton btn, String text) {
        final JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, 45));
        button.setForeground(Color.WHITE);
        button.setBackground(colorDanger);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(font16Bold);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setOpaque(true);
        
        final Color hoverColor = new Color(220, 38, 38);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(colorDanger);
            }
        });
        return button;
    }

    public static JButton setButtonDB(JButton btn, String text) {
        final JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(250, 50));
        button.setMaximumSize(new Dimension(250, 50));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(font16);
        button.setBackground(colorPrimary);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(JButton.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setOpaque(true);
        

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(colorPrimaryDark);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(colorPrimary);
            }
        });
        return button;
    }

    public static JTextField setTextField(JTextField textField, String text) {
        textField = new JTextField(text);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorBorder, 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        textField.setBackground(colorTextField);
        textField.setForeground(colorText);
        textField.setCaretColor(colorPrimary);
        textField.setFont(font16);
        return textField;
    }

    public static JPasswordField setPassField(JPasswordField passwordField) {
        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorBorder, 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        passwordField.setBackground(colorTextField);
        passwordField.setForeground(colorText);
        passwordField.setCaretColor(colorPrimary);
        passwordField.setFont(font16);
        return passwordField;
    }

    public static JLabel setLabelPrimary(JLabel label, String text) {
        label = new JLabel(text);
        label.setForeground(colorPrimary);
        label.setFont(font16Bold);
        return label;
    }

    public static JLabel setLabel(JLabel label, String text) {
        label = new JLabel(text);
        label.setForeground(colorLabel);
        label.setFont(font16);
        return label;
    }

    public static void styleTable(JTable table) {
        table.setFont(font14);
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(colorBorder);
        table.setBackground(colorBgCard);
        table.setSelectionBackground(colorTableSelected);
        table.setSelectionForeground(colorText);
        table.setIntercellSpacing(new Dimension(0, 1));
        
        table.getTableHeader().setFont(font16Bold);
        table.getTableHeader().setBackground(colorTableHeader);
        table.getTableHeader().setForeground(colorText);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, colorPrimary));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
    }

    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(colorBgCard);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorBorder, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return card;
    }

    public static JLabel createSectionTitle(String text) {
        JLabel title = new JLabel(text);
        title.setFont(font18Bold);
        title.setForeground(colorText);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        return title;
    }
}
