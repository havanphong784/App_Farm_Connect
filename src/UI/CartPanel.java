package UI;

import DBConnect.CustomerDAO;
import Model.CartItem;
import Model.Customer;
import Service.StatisticsService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static UI.LoginFrame.userid;

public class CartPanel extends JPanel {
    private static CartPanel instance;
    
    private JTable cartTable;
    private DefaultTableModel cartModel;
    private JLabel lblTotal;
    private JLabel lblItemCount;
    private JButton btnCheckout;
    private JButton btnClear;
    private JComboBox<Customer> cboCustomer;
    private JTextField txtCustomerName;
    private JTextField txtCustomerSdt;
    private List<Customer> customerList;
    
    // Logic moved to CartService
    private StatisticsService.CartService cartService;
    
    public static CartPanel getInstance() {
        if (instance == null) {
            instance = new CartPanel();
        }
        return instance;
    }
    
    private CartPanel() {
        cartService = StatisticsService.CartService.getInstance();
        
        setLayout(new BorderLayout(15, 15));
        setBackground(UIStyle.colorBg);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        initUI();
    }
    
    private void initUI() {

        JPanel pnHeader = createHeader();
        add(pnHeader, BorderLayout.NORTH);
        

        JScrollPane scrollPane = createCartTable();
        add(scrollPane, BorderLayout.CENTER);
        

        JPanel southContainer = createSouthPanel();
        add(southContainer, BorderLayout.SOUTH);
    }
    
    private JPanel createHeader() {
        JPanel pnHeader = new JPanel(new BorderLayout());
        pnHeader.setBackground(UIStyle.colorBg);
        pnHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitle = new JLabel("Giỏ Hàng");
        lblTitle.setFont(UIStyle.font24Bold);
        lblTitle.setForeground(UIStyle.colorText);
        pnHeader.add(lblTitle, BorderLayout.WEST);
        
        lblItemCount = new JLabel("0 sản phẩm");
        lblItemCount.setFont(UIStyle.font14);
        lblItemCount.setForeground(UIStyle.colorTextSecondary);
        pnHeader.add(lblItemCount, BorderLayout.EAST);
        
        return pnHeader;
    }
    
    private JScrollPane createCartTable() {
        String[] columns = {"Tên Sản Phẩm", "Đơn Giá", "Số Lượng", "Thành Tiền", "Xóa"};
        cartModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        
        cartTable = new JTable(cartModel);
        UIStyle.styleTable(cartTable);
        
        cartTable.getColumn("Xóa").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton btn = new JButton("X");
            btn.setBackground(UIStyle.colorDanger);
            btn.setForeground(Color.WHITE);
            btn.setFont(UIStyle.font14);
            return btn;
        });
        
        cartTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int column = cartTable.columnAtPoint(e.getPoint());
                int row = cartTable.rowAtPoint(e.getPoint());
                if (column == 4 && row >= 0) {
                    removeItem(row);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyle.colorBorder, 1));
        return scrollPane;
    }
    
    private JPanel createSouthPanel() {
        JPanel southContainer = new JPanel();
        southContainer.setLayout(new BoxLayout(southContainer, BoxLayout.Y_AXIS));
        southContainer.setBackground(UIStyle.colorBg);
        
        JPanel customerSection = createCustomerSection();
        southContainer.add(customerSection);
        southContainer.add(Box.createVerticalStrut(15));
        
        JPanel actionPanel = createActionPanel();
        southContainer.add(actionPanel);
        
        return southContainer;
    }
    
    private JPanel createCustomerSection() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UIStyle.colorBgCard);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyle.colorBorder, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblSectionTitle = new JLabel("Thông Tin Khách Hàng");
        lblSectionTitle.setFont(UIStyle.font16Bold);
        lblSectionTitle.setForeground(UIStyle.colorText);
        lblSectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        wrapper.add(lblSectionTitle, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIStyle.colorBgCard);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblSelect = new JLabel("Chọn khách hàng:");
        lblSelect.setFont(UIStyle.font14);
        lblSelect.setForeground(UIStyle.colorLabel);
        formPanel.add(lblSelect, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        customerList = CustomerDAO.getAllCustomers();
        cboCustomer = new JComboBox<>();
        cboCustomer.addItem(null);
        for (Customer c : customerList) {
            cboCustomer.addItem(c);
        }
        cboCustomer.setFont(UIStyle.font14);
        cboCustomer.setPreferredSize(new Dimension(300, 38));
        cboCustomer.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("-- Thêm khách hàng mới --");
                } else {
                    setText(((Customer) value).toString());
                }
                return this;
            }
        });
        cboCustomer.addActionListener(e -> {
            Customer selected = (Customer) cboCustomer.getSelectedItem();
            if (selected != null) {
                txtCustomerName.setText(selected.getCustomerName());
                txtCustomerSdt.setText(selected.getCustomerSdt() != null ? selected.getCustomerSdt() : "");
            } else {
                txtCustomerName.setText("");
                txtCustomerSdt.setText("");
            }
        });
        formPanel.add(cboCustomer, gbc);
        
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblName = new JLabel("Tên khách hàng:");
        lblName.setFont(UIStyle.font14);
        lblName.setForeground(UIStyle.colorLabel);
        formPanel.add(lblName, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtCustomerName = new JTextField();
        txtCustomerName.setFont(UIStyle.font14);
        txtCustomerName.setPreferredSize(new Dimension(180, 38));
        txtCustomerName.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyle.colorBorder, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(txtCustomerName, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lblPhone = new JLabel("Số điện thoại:");
        lblPhone.setFont(UIStyle.font14);
        lblPhone.setForeground(UIStyle.colorLabel);
        formPanel.add(lblPhone, gbc);
        
        gbc.gridx = 3; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtCustomerSdt = new JTextField();
        txtCustomerSdt.setFont(UIStyle.font14);
        txtCustomerSdt.setPreferredSize(new Dimension(150, 38));
        txtCustomerSdt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyle.colorBorder, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(txtCustomerSdt, gbc);
        
        wrapper.add(formPanel, BorderLayout.CENTER);
        return wrapper;
    }
    
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(UIStyle.colorBgCard);
        actionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyle.colorPrimary, 2),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
        totalPanel.setBackground(UIStyle.colorBgCard);
        
        JLabel lblTotalLabel = new JLabel("Tổng thanh toán:");
        lblTotalLabel.setFont(UIStyle.font14);
        lblTotalLabel.setForeground(UIStyle.colorTextSecondary);
        
        lblTotal = new JLabel("0 VNĐ");
        lblTotal.setFont(UIStyle.font24Bold);
        lblTotal.setForeground(UIStyle.colorPrimary);
        
        totalPanel.add(lblTotalLabel);
        totalPanel.add(Box.createVerticalStrut(5));
        totalPanel.add(lblTotal);
        
        actionPanel.add(totalPanel, BorderLayout.WEST);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setBackground(UIStyle.colorBgCard);
        
        btnClear = createButton("Xóa Tất Cả", UIStyle.colorTextSecondary, Color.WHITE);
        btnClear.addActionListener(e -> clearCart());
        btnPanel.add(btnClear);
        
        btnCheckout = createButton("Thanh Toán", UIStyle.colorPrimary, Color.WHITE);
        btnCheckout.setPreferredSize(new Dimension(160, 45));
        btnCheckout.addActionListener(e -> checkout());
        btnPanel.add(btnCheckout);
        
        actionPanel.add(btnPanel, BorderLayout.EAST);
        
        return actionPanel;
    }
    
    private JButton createButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 42));
        btn.setFont(UIStyle.font14);
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        final Color hoverColor = bgColor.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }
    
    public void addToCart(int proId, String proName, BigDecimal unitPrice, int quantity) {
        cartService.addToCart(proId, proName, unitPrice, quantity);
        refreshTable();
    }
    
    private void removeItem(int index) {
        cartService.removeItem(index);
        refreshTable();
    }
    
    public void clearCart() {
        cartService.clearCart();
        cboCustomer.setSelectedIndex(0);
        txtCustomerName.setText("");
        txtCustomerSdt.setText("");
        refreshTable();
    }
    
    private void refreshTable() {
        cartModel.setRowCount(0);
        NumberFormat vnd = NumberFormat.getInstance(Locale.of("vi", "VN"));
        
        List<CartItem> items = cartService.getCartItems();
        
        for (CartItem item : items) {
            BigDecimal subtotal = item.getSubtotal();
            cartModel.addRow(new Object[]{
                item.getProName(),
                vnd.format(item.getUnitPrice()) + " đ",
                item.getQuantity(),
                vnd.format(subtotal) + " đ",
                "X"
            });
        }
        
        BigDecimal total = cartService.calculateTotal();
        lblTotal.setText(vnd.format(total) + " VNĐ");
        lblItemCount.setText(cartService.getItemCount() + " sản phẩm");
    }
    
    private void checkout() {
        if (cartService.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
            return;
        }
        
        try {
            String customerName = txtCustomerName.getText().trim();
            String customerSdt = txtCustomerSdt.getText().trim();
            
            int orderId = cartService.checkout(userid, customerName, customerSdt);
            
            if (orderId > 0) {
                JOptionPane.showMessageDialog(this, "✅ Đặt hàng thành công!\nMã đơn hàng: " + orderId);
                clearCart();
                
                HistoryPanel.refreshOrdersTable();
                DashboardPanel.refreshDashboard();
                ProductListPanel.refreshTableData();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Đặt hàng thất bại!\nVui lòng kiểm tra database hoặc kết nối.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    public int getItemCount() {
        return cartService.getItemCount();
    }
    
    public boolean isEmpty() {
        return cartService.isEmpty();
    }
}

