package UI;

import Service.ProductsService;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.math.BigDecimal;

public class ProductsFormUpdate extends ProductsFromInsert {
    
    private JLabel lblSale;
    private JTextField txtPricePercent;
    
    public ProductsFormUpdate(TableModel model, int row) {
        super();
        
        this.lblTitle.setText("Cap nhat thong tin san pham");
        this.btnSubmit.setText("Cap nhat");
        
        this.txtName.setText(String.valueOf(model.getValueAt(row, 0)));
        this.txtPrice.setText(String.valueOf(model.getValueAt(row, 1)));
        this.txtUnit.setText(String.valueOf(model.getValueAt(row, 2)));
        this.txtQuantity.setText(String.valueOf(model.getValueAt(row, 3)));
        this.txtDes.setText(String.valueOf(model.getValueAt(row, 4)));
        
        remove(lblExpiry);
        remove(txtExpiry);

        lblPrice.setBounds(20, 140, 170, 22);
        txtPrice.setBounds(20, 165, 170, 40);

        this.lblSale = UIStyle.setLabel(this.lblSale, "Giam gia (%):");
        this.lblSale.setBounds(210, 140, 170, 22);
        this.add(this.lblSale);

        this.txtPricePercent = UIStyle.setTextField(this.txtPricePercent, "0");
        this.txtPricePercent.setBounds(210, 165, 170, 40);
        this.add(this.txtPricePercent);

        this.txtName.setEditable(false);
        this.txtName.setBackground(new Color(240, 240, 240));


        setupUpdateAction();
    }
    
    private void setupUpdateAction() {
        if (this.btnSubmit.getActionListeners().length > 0) {
            this.btnSubmit.removeActionListener(this.btnSubmit.getActionListeners()[0]);
        }
        
        this.btnSubmit.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                BigDecimal price = new BigDecimal(txtPrice.getText().trim());
                String unit = txtUnit.getText().trim();
                int quantity = Integer.parseInt(txtQuantity.getText().trim());
                float pricePercent = Float.parseFloat(txtPricePercent.getText().trim());
                String des = txtDes.getText().trim();

                boolean isUpdated = ProductsService.updateProduct(name, des, quantity, price, pricePercent, unit);
                
                if (isUpdated) {
                    JOptionPane.showMessageDialog(null, "Cap nhat san pham thanh cong!");
                    SwingUtilities.getWindowAncestor(this).dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Cap nhat san pham that bai!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Vui long nhap dung dinh dang so!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Da xay ra loi: " + ex.getMessage());
            }
        });
    }
}
