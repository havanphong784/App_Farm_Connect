package UI;

import DBConnect.StatisticsDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardPanel extends JPanel {
    
    private static DashboardPanel instance;
    
    private JPanel pnCards;
    private JPanel pnExpired;
    private JTable tableExpired;
    private DefaultTableModel modelExpired;
    
    public DashboardPanel() {
        instance = this;
        setLayout(new BorderLayout(15, 15));
        setBackground(UIStyle.colorBg);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        initUI();
    }
    
    private void initUI() {
        JPanel pnHeader = new JPanel(new BorderLayout());
        pnHeader.setBackground(UIStyle.colorBg);
        
        JLabel lblWelcome = new JLabel("Tổng Quan");
        lblWelcome.setFont(UIStyle.font24Bold);
        lblWelcome.setForeground(UIStyle.colorText);
        pnHeader.add(lblWelcome, BorderLayout.WEST);
        
        JLabel lblDate = new JLabel(java.time.LocalDate.now().toString());
        lblDate.setFont(UIStyle.font16);
        lblDate.setForeground(UIStyle.colorTextSecondary);
        pnHeader.add(lblDate, BorderLayout.EAST);
        
        add(pnHeader, BorderLayout.NORTH);
        
        JPanel pnMain = new JPanel();
        pnMain.setLayout(new BoxLayout(pnMain, BoxLayout.Y_AXIS));
        pnMain.setBackground(UIStyle.colorBg);
        
        createSummaryCards();
        pnMain.add(pnCards);
        pnMain.add(Box.createVerticalStrut(20));
        
        createExpiredProductsPanel();
        pnMain.add(pnExpired);
        
        JScrollPane scrollPane = new JScrollPane(pnMain);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void createSummaryCards() {
        pnCards = new JPanel(new GridLayout(1, 4, 15, 0));
        pnCards.setBackground(UIStyle.colorBg);
        pnCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        
        BigDecimal totalRevenue = StatisticsDAO.getTotalRevenue(UI.LoginFrame.userid);
        int totalOrders = StatisticsDAO.getTotalOrders(UI.LoginFrame.userid);
        int totalProducts = StatisticsDAO.getTotalProducts(UI.LoginFrame.userid);
        Object[][] expiredProducts = StatisticsDAO.getExpiredProducts(UI.LoginFrame.userid);
        
        NumberFormat vnd = NumberFormat.getInstance(Locale.of("vi", "VN"));
        
        pnCards.add(createCard(
            "Tổng Doanh Thu",
            vnd.format(totalRevenue) + " VNĐ",
            new Color(16, 185, 129),
            "$"
        ));
        
        pnCards.add(createCard(
            "Đơn Hàng",
            String.valueOf(totalOrders),
            new Color(59, 130, 246),
            "#"
        ));
        
        pnCards.add(createCard(
            "Sản Phẩm Tồn Kho",
            String.valueOf(totalProducts),
            new Color(139, 92, 246),
            "*"
        ));
        
        pnCards.add(createCard(
            "Sắp Hết Hạn",
            String.valueOf(expiredProducts.length),
            expiredProducts.length > 0 ? new Color(239, 68, 68) : new Color(34, 197, 94),
            "!"
        ));
    }
    
    private JPanel createCard(String title, String value, Color accentColor, String emoji) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblIcon = new JLabel(emoji);
        lblIcon.setFont(UIStyle.font24Bold);
        lblIcon.setForeground(accentColor);
        lblIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblIcon);
        card.add(Box.createVerticalStrut(8));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIStyle.font14);
        lblTitle.setForeground(UIStyle.colorTextSecondary);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(5));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(UIStyle.font20);
        lblValue.setForeground(accentColor);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblValue);
        
        return card;
    }
    
    private void createExpiredProductsPanel() {
        pnExpired = new JPanel(new BorderLayout(10, 10));
        pnExpired.setBackground(new Color(255, 248, 245));
        pnExpired.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        pnExpired.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 200, 180), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel("  [!] Sản Phẩm Hết Hạn / Sắp Hết Hạn");
        lblTitle.setFont(UIStyle.font18Bold);
        lblTitle.setForeground(UIStyle.colorDanger);
        pnExpired.add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"Tên Sản Phẩm", "Số Lượng", "Đơn Vị", "Ngày Hết Hạn", "Status"};
        Object[][] data = StatisticsDAO.getExpiredProducts(UI.LoginFrame.userid);

        modelExpired = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableExpired = new JTable(modelExpired);
        UIStyle.styleTable(tableExpired);
        tableExpired.getTableHeader().setBackground(new Color(255, 230, 230));
        tableExpired.getTableHeader().setForeground(UIStyle.colorDanger);
        tableExpired.setSelectionBackground(new Color(255, 220, 220));

        if (data.length > 0) {
            tableExpired.getColumnModel().getColumn(4).setMinWidth(0);
            tableExpired.getColumnModel().getColumn(4).setMaxWidth(0);
            tableExpired.getColumnModel().getColumn(4).setWidth(0);

            Color colorExpired = new Color(220, 53, 69);
            Color colorExpiring = new Color(255, 152, 0);
            Color colorExpiredBg = new Color(255, 235, 238);
            Color colorExpiringBg = new Color(255, 243, 224);

            DefaultTableCellRenderer colorRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    
                    String status = (String) table.getModel().getValueAt(row, 4);
                    
                    if (!isSelected) {
                        if ("expired".equals(status)) {
                            c.setBackground(colorExpiredBg);
                            c.setForeground(colorExpired);
                        } else {
                            c.setBackground(colorExpiringBg);
                            c.setForeground(colorExpiring);
                        }
                    }
                    
                    if (column == 1 || column == 2) {
                        setHorizontalAlignment(SwingConstants.CENTER);
                    } else {
                        setHorizontalAlignment(SwingConstants.LEFT);
                    }
                    
                    return c;
                }
            };

            for (int i = 0; i < 4; i++) {
                tableExpired.getColumnModel().getColumn(i).setCellRenderer(colorRenderer);
            }
        }

        JScrollPane scrollPane = new JScrollPane(tableExpired);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(0, 250));

        if (data.length == 0) {
            JLabel lblNoData = new JLabel("Không có sản phẩm hết hạn - Tất cả sản phẩm đều tốt!", JLabel.CENTER);
            lblNoData.setFont(UIStyle.font16);
            lblNoData.setForeground(UIStyle.colorSuccess);
            pnExpired.add(lblNoData, BorderLayout.CENTER);
        } else {
            pnExpired.add(scrollPane, BorderLayout.CENTER);
            
            int expiredCount = 0;
            int expiringCount = 0;
            for (Object[] row : data) {
                if ("expired".equals(row[4])) expiredCount++;
                else expiringCount++;
            }
            
            JPanel pnFooter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
            pnFooter.setBackground(new Color(255, 248, 245));
            pnFooter.setBorder(new EmptyBorder(10, 0, 0, 0));
            
            JLabel lblExpired = new JLabel("[X] Đã hết hạn: " + expiredCount);
            lblExpired.setFont(UIStyle.font14);
            lblExpired.setForeground(new Color(220, 53, 69));
            
            JLabel lblExpiring = new JLabel("[!] Sắp hết hạn: " + expiringCount);
            lblExpiring.setFont(UIStyle.font14);
            lblExpiring.setForeground(new Color(255, 152, 0));
            
            JLabel lblTotal = new JLabel("| Tổng: " + data.length + " sản phẩm");
            lblTotal.setFont(UIStyle.font14);
            lblTotal.setForeground(UIStyle.colorTextSecondary);
            
            pnFooter.add(lblExpired);
            pnFooter.add(lblExpiring);
            pnFooter.add(lblTotal);
            
            pnExpired.add(pnFooter, BorderLayout.SOUTH);
        }
    }
    
    public void refresh() {
        removeAll();
        initUI();
        revalidate();
        repaint();
    }
    
    public static void refreshDashboard() {
        if (instance != null) {
            instance.refresh();
        }
    }
}
