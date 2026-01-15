package UI;

import DBConnect.UserDAO;
import javax.swing.*;

public class RegisterFrame extends LoginFrame {
    private JLabel lblAgainPass, lblEmail;
    private JPasswordField txtAgainPass;
    private JTextField txtEmail;
    private JButton btnBack;

    public RegisterFrame() {
        super();
        
        pnRight.remove(btnLogin);
        pnRight.remove(lblOr);
        
        lblTitle.setText("Vui long nhap thong tin");
        
        setupConfirmPasswordField();
        setupEmailField();
        setupButtons();
        setupRegisterAction();
        setupBackAction();
    }
    
    private void setupConfirmPasswordField() {
        this.lblAgainPass = new JLabel("Nhap lai mat khau");
        this.lblAgainPass.setFont(UIStyle.font16);
        this.lblAgainPass.setBounds(80, 470, 340, 30);
        pnRight.add(lblAgainPass);

        this.txtAgainPass = UIStyle.setPassField(this.txtAgainPass);
        this.txtAgainPass.setFont(UIStyle.font16);
        this.txtAgainPass.setBounds(60, 510, 340, 50);
        pnRight.add(txtAgainPass);
    }
    
    private void setupEmailField() {
        this.lblEmail = new JLabel("Email");
        this.lblEmail.setFont(UIStyle.font16);
        this.lblEmail.setBounds(80, 580, 340, 30);
        pnRight.add(lblEmail);

        this.txtEmail = UIStyle.setTextField(this.txtEmail, "");
        this.txtEmail.setFont(UIStyle.font16);
        this.txtEmail.setBounds(60, 620, 340, 50);
        pnRight.add(txtEmail);
    }
    
    private void setupButtons() {
        btnRegister.setBounds(60, 690, 340, 50);
        pnRight.add(this.btnRegister);

        this.btnBack = UIStyle.setBtnSecondary(this.btnBack, "Quay lai");
        btnBack.setBounds(60, 760, 340, 40);
        pnRight.add(btnBack);
    }
    
    private void setupRegisterAction() {
        this.btnRegister.removeActionListener(al);
        
        this.btnRegister.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String againPass = new String(txtAgainPass.getPassword()).trim();

            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || againPass.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui long nhap day du thong tin!");
                return;
            }

            if (!againPass.equals(password)) {
                JOptionPane.showMessageDialog(null, "Mat khau nhap lai khong trung khop!");
                return;
            }

            if (!email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
                JOptionPane.showMessageDialog(null, "Email khong hop le (chi chap nhan @gmail.com)!");
                return;
            }
            
            boolean success = UserDAO.chekRegistration(username, password, email);
            if (success) {
                JOptionPane.showMessageDialog(null, "Dang ky thanh cong!");
                new LoginFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Dang ky that bai! Email da ton tai.");
            }
        });
    }
    
    private void setupBackAction() {
        this.btnBack.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }
}
