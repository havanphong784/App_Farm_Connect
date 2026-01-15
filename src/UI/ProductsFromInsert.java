package UI;

import Service.ProductsService;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static UI.LoginFrame.userid;

public class ProductsFromInsert extends JOptionPane {
    
    protected JLabel lblTitle;
    protected JLabel lblName, lblPrice, lblUnit, lblQuantiy, lblDes, lblExpiry;
    protected JTextField txtName, txtPrice, txtUnit, txtQuantity, txtDes, txtExpiry;
    protected JButton btnSubmit;

    public ProductsFromInsert() {
        setupPanel();
        setupTitle();
        setupFormFields();
        setupSubmitButton();
        setupInsertAction();
    }
    
    private void setupPanel() {
        this.setBackground(UIStyle.colorBg);
        this.setPreferredSize(new Dimension(400, 585));
        this.setLayout(null);
    }
    
    private void setupTitle() {
        this.lblTitle = UIStyle.setLabel(this.lblTitle, "Nhap thong tin san pham");
        this.lblTitle.setFont(UIStyle.font20);
        this.lblTitle.setHorizontalAlignment(JLabel.CENTER);
        this.lblTitle.setBounds(0, 10, 400, 40);
        this.add(this.lblTitle);
    }
    
    private void setupFormFields() {
        int y = 65;
        int fieldHeight = 40;
        int labelHeight = 22;
        int gap = 75;
        
        this.lblName = UIStyle.setLabel(this.lblName, "Ten san pham:");
        this.lblName.setBounds(20, y, 360, labelHeight);
        this.add(this.lblName);

        this.txtName = UIStyle.setTextField(this.txtName, "");
        this.txtName.setBounds(20, y + 25, 360, fieldHeight);
        this.txtName.setFont(UIStyle.font14);
        this.add(this.txtName);
        y += gap;

        this.lblPrice = UIStyle.setLabel(this.lblPrice, "Gia san pham:");
        this.lblPrice.setBounds(20, y, 360, labelHeight);
        this.add(this.lblPrice);

        this.txtPrice = UIStyle.setTextField(this.txtPrice, "");
        this.txtPrice.setBounds(20, y + 25, 360, fieldHeight);
        this.txtPrice.setFont(UIStyle.font14);
        this.add(this.txtPrice);
        y += gap;

        this.lblUnit = UIStyle.setLabel(this.lblUnit, "Don vi tinh:");
        this.lblUnit.setBounds(20, y, 360, labelHeight);
        this.add(this.lblUnit);

        this.txtUnit = UIStyle.setTextField(this.txtUnit, "");
        this.txtUnit.setBounds(20, y + 25, 360, fieldHeight);
        this.txtUnit.setFont(UIStyle.font14);
        this.add(this.txtUnit);
        y += gap;

        this.lblQuantiy = UIStyle.setLabel(this.lblQuantiy, "So luong:");
        this.lblQuantiy.setBounds(20, y, 360, labelHeight);
        this.add(this.lblQuantiy);

        this.txtQuantity = UIStyle.setTextField(this.txtQuantity, "");
        this.txtQuantity.setBounds(20, y + 25, 360, fieldHeight);
        this.txtQuantity.setFont(UIStyle.font14);
        this.add(this.txtQuantity);
        y += gap;

        this.lblDes = UIStyle.setLabel(this.lblDes, "Mo ta san pham:");
        this.lblDes.setBounds(20, y, 360, labelHeight);
        this.add(this.lblDes);

        this.txtDes = UIStyle.setTextField(this.txtDes, "");
        this.txtDes.setBounds(20, y + 25, 360, fieldHeight);
        this.txtDes.setFont(UIStyle.font14);
        this.add(this.txtDes);
        y += gap;

        this.lblExpiry = UIStyle.setLabel(this.lblExpiry, "Han su dung (so ngay):");
        this.lblExpiry.setBounds(20, y, 360, labelHeight);
        this.add(this.lblExpiry);

        this.txtExpiry = UIStyle.setTextField(this.txtExpiry, "");
        this.txtExpiry.setBounds(20, y + 25, 360, fieldHeight);
        this.txtExpiry.setFont(UIStyle.font14);
        this.add(this.txtExpiry);
    }
    
    private void setupSubmitButton() {
        this.btnSubmit = UIStyle.setBtnActive(this.btnSubmit, "Them san pham");
        this.btnSubmit.setBounds(20, 525, 360, 40);
        this.add(this.btnSubmit);
    }
    
    private void setupInsertAction() {
        this.btnSubmit.addActionListener(e -> {
            if (!validateFields()) {
                JOptionPane.showMessageDialog(null, "Vui long nhap day du thong tin san pham!");
                return;
            }

            try {
                String name = txtName.getText().trim();
                BigDecimal price = new BigDecimal(txtPrice.getText().trim());
                String unit = txtUnit.getText().trim();
                int quantity = Integer.parseInt(txtQuantity.getText().trim());
                String des = txtDes.getText().trim();
                int days = Integer.parseInt(txtExpiry.getText().trim());
                
                Timestamp expirationDate = Timestamp.from(
                    Instant.now().plus(days, ChronoUnit.DAYS)
                );

                boolean success = ProductsService.insertProduct(
                    name, des, quantity, price, unit, expirationDate, userid
                );
                
                if (success) {
                    JOptionPane.showMessageDialog(null, "Them san pham thanh cong!");
                    SwingUtilities.getWindowAncestor(this).dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Them san pham that bai!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Loi dinh dang so!");
            }
        });
    }
    
    private boolean validateFields() {
        return !txtName.getText().trim().isEmpty() 
            && !txtPrice.getText().trim().isEmpty() 
            && !txtUnit.getText().trim().isEmpty()
            && !txtQuantity.getText().trim().isEmpty() 
            && !txtDes.getText().trim().isEmpty()
            && !txtExpiry.getText().trim().isEmpty();
    }
}
