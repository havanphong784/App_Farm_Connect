package UI;

import javax.swing.*;
import java.awt.*;

import static UI.LoginFrame.username;

public class MainFrame extends JFrame {
    
    private JPanel pnNavigation, pnContent, pnHeader, pnCard;
    
    private JButton btnDashboard, btnProducts, btnCart, btnStatistics, btnHistory, btnLogout, btnMenu;
    
    private JLabel lblNameApp, lblAvatar, lblRole, lblUsername;

    public MainFrame() {
        setupFrame();
        setupNavigationPanel();
        setupContentPanel();
        setupCardLayout();
        setupNavigationActions();
    }
    
    private void setupFrame() {
        this.setSize(1200, 850);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("FarmConnect - Quản lý nông sản");
    }
    
    private void setupNavigationPanel() {
        this.pnNavigation = new JPanel();
        this.pnNavigation.setLayout(new BoxLayout(pnNavigation, BoxLayout.Y_AXIS));
        this.pnNavigation.setPreferredSize(new Dimension(240, 0));
        this.pnNavigation.setBackground(UIStyle.colorPrimary);
        this.add(this.pnNavigation, BorderLayout.WEST);

        this.lblNameApp = UIStyle.setLabelPrimary(this.lblNameApp, "Farm Connect");
        this.lblNameApp.setFont(UIStyle.font30);
        this.lblNameApp.setPreferredSize(new Dimension(240, 80));
        this.lblNameApp.setMaximumSize(new Dimension(240, 80));
        this.lblNameApp.setHorizontalAlignment(JLabel.CENTER);
        this.lblNameApp.setVerticalAlignment(JLabel.CENTER);
        this.lblNameApp.setForeground(Color.white);
        this.pnNavigation.add(this.lblNameApp);

        this.btnDashboard = UIStyle.setButtonDB(this.btnDashboard, "Tổng quan");
        this.pnNavigation.add(this.btnDashboard);
        
        this.btnProducts = UIStyle.setButtonDB(this.btnProducts, "Sản phẩm");
        this.pnNavigation.add(this.btnProducts);
        
        this.btnCart = UIStyle.setButtonDB(this.btnCart, "Giỏ hàng");
        this.pnNavigation.add(this.btnCart);
        
        this.btnStatistics = UIStyle.setButtonDB(this.btnStatistics, "Thống kê");
        this.pnNavigation.add(this.btnStatistics);
        
        this.btnHistory = UIStyle.setButtonDB(this.btnHistory, "Lịch sử bán hàng");
        this.pnNavigation.add(this.btnHistory);

        this.pnNavigation.add(Box.createVerticalGlue());

        this.btnLogout = UIStyle.setButtonDB(this.btnLogout, "Đăng xuất");
        this.btnLogout.setFont(UIStyle.font16);
        this.btnLogout.setForeground(UIStyle.colorRed);
        this.pnNavigation.add(this.btnLogout);

        this.pnNavigation.setVisible(false);
    }
    
    private void setupContentPanel() {
        this.pnContent = new JPanel(new BorderLayout());

        this.pnHeader = new JPanel();
        this.pnHeader.setLayout(new BoxLayout(this.pnHeader, BoxLayout.X_AXIS));
        this.pnHeader.setBackground(UIStyle.colorHeader);
        this.pnHeader.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        this.btnMenu = new JButton("[=] Menu");
        this.btnMenu.setFont(UIStyle.font16Bold);
        this.btnMenu.setBackground(UIStyle.colorPrimary);
        this.btnMenu.setForeground(Color.WHITE);
        this.btnMenu.setFocusable(false);
        this.btnMenu.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        this.btnMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.pnHeader.add(this.btnMenu);
        
        btnMenu.addActionListener(e -> toggleNavigation());

        this.pnHeader.add(Box.createHorizontalGlue());

        setupUserInfo();

        this.pnContent.add(this.pnHeader, BorderLayout.NORTH);
        this.add(this.pnContent, BorderLayout.CENTER);
    }
    
    private void setupUserInfo() {
        JPanel pnUserInfo = new JPanel();
        pnUserInfo.setLayout(new BoxLayout(pnUserInfo, BoxLayout.Y_AXIS));
        pnUserInfo.setBackground(UIStyle.colorHeader);

        this.lblUsername = new JLabel(username);
        this.lblUsername.setFont(UIStyle.font16Bold);
        this.lblUsername.setForeground(UIStyle.colorPrimary);
        this.lblUsername.setAlignmentX(Component.RIGHT_ALIGNMENT);

        this.lblRole = new JLabel("Quan tri vien");
        this.lblRole.setFont(UIStyle.font14);
        this.lblRole.setForeground(UIStyle.colorText);
        this.lblRole.setAlignmentX(Component.RIGHT_ALIGNMENT);

        pnUserInfo.add(this.lblUsername);
        pnUserInfo.add(Box.createVerticalStrut(4));
        pnUserInfo.add(this.lblRole);

        this.lblAvatar = new JLabel("FC");
        this.lblAvatar.setFont(new Font("Arial", Font.BOLD, 24));
        this.lblAvatar.setForeground(UIStyle.colorPrimary);
        this.lblAvatar.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));

        this.pnHeader.add(pnUserInfo);
        this.pnHeader.add(this.lblAvatar);
    }
    
    private void setupCardLayout() {
        this.pnCard = new JPanel(new CardLayout());
        this.pnCard.add(new DashboardPanel(), "Dashboard");
        this.pnCard.add(new ProductListPanel(), "Products");
        this.pnCard.add(new StatisticsPanel(), "Statistics");
        this.pnCard.add(new HistoryPanel(), "History");
        this.pnCard.add(CartPanel.getInstance(), "Cart");
        
        showCard("Dashboard");

        this.pnContent.add(this.pnCard, BorderLayout.CENTER);
    }
    
    private void setupNavigationActions() {
        btnDashboard.addActionListener(e -> {
            showCard("Dashboard");
            hideNavigation();
        });

        btnProducts.addActionListener(e -> {
            showCard("Products");
            hideNavigation();
        });

        btnCart.addActionListener(e -> {
            showCard("Cart");
            hideNavigation();
        });

        btnStatistics.addActionListener(e -> {
            showCard("Statistics");
            hideNavigation();
        });

        btnHistory.addActionListener(e -> {
            showCard("History");
            hideNavigation();
        });

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }
    
    private void toggleNavigation() {
        if (pnNavigation.isVisible()) {
            pnNavigation.setVisible(false);
            btnMenu.setText("[=] Menu");
        } else {
            pnNavigation.setVisible(true);
            btnMenu.setText("[X] Dong");
        }
        revalidate();
        repaint();
    }
    
    private void hideNavigation() {
        pnNavigation.setVisible(false);
        btnMenu.setText("[=] Menu");
    }
    
    private void showCard(String cardName) {
        CardLayout cl = (CardLayout) pnCard.getLayout();
        cl.show(pnCard, cardName);
    }
}
