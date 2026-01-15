package UI;

import DBConnect.UserDAO;
import javax.swing.*;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    protected JPanel pnLeft, pnRight;
    protected JLabel lblTitle, lblAppName, lblUsername, lblPassword, lblOr;
    protected JTextField txtUsername;
    protected JPasswordField txtPassword;
    protected JButton btnLogin, btnRegister;
    protected ActionListener al;
    public static String username;
    public static int userid;
    public LoginFrame() {
        setupFrame();
        setupLeftPanel();
        setupRightPanel();
        setupFormFields();
        setupButtons();
        setupActions();
    }
    
    private void setupFrame() {
        this.setSize(1200, 850);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);
        this.setTitle("FarmConnect - Login");
    }
    
    private void setupLeftPanel() {
        this.pnLeft = new JPanel();
        this.pnLeft.setLayout(null);
        this.pnLeft.setBounds(0, 0, 720, 850);
        
        JLabel img = new JLabel("");
        img.setIcon(new ImageIcon(LoginFrame.class.getResource("/Image/login720x850.png")));
        img.setBounds(0, 0, 720, 850);
        this.pnLeft.add(img);
        
        this.add(this.pnLeft);
    }
    
    private void setupRightPanel() {
        this.pnRight = new JPanel();
        this.pnRight.setLayout(null);
        this.pnRight.setBounds(720, 0, 480, 850);
        this.pnRight.setBackground(UIStyle.colorBg);
        this.add(this.pnRight);

        this.lblAppName = new JLabel("Farm Connect");
        this.lblAppName.setFont(UIStyle.font30);
        this.lblAppName.setForeground(UIStyle.colorPrimary);
        this.lblAppName.setBounds(130, 95, 250, 40);
        this.pnRight.add(lblAppName);

        this.lblTitle = new JLabel("Chào mừng trở lại!");
        this.lblTitle.setFont(UIStyle.font20);
        this.lblTitle.setForeground(UIStyle.colorPrimary);
        this.lblTitle.setBounds(60, 180, 300, 30);
        this.pnRight.add(lblTitle);
    }
    
    private void setupFormFields() {
        this.lblUsername = new JLabel("Tên đăng nhập");
        this.lblUsername.setFont(UIStyle.font16);
        this.lblUsername.setForeground(UIStyle.colorLabel);
        this.lblUsername.setBounds(80, 250, 200, 30);
        this.pnRight.add(lblUsername);

        this.txtUsername = UIStyle.setTextField(this.txtUsername, "");
        this.txtUsername.setBounds(60, 290, 340, 50);
        this.txtUsername.setFont(UIStyle.font16);
        this.pnRight.add(txtUsername);

        this.lblPassword = new JLabel("Mật khẩu");
        this.lblPassword.setFont(UIStyle.font16);
        this.lblPassword.setForeground(UIStyle.colorLabel);
        this.lblPassword.setBounds(80, 360, 200, 30);
        this.pnRight.add(lblPassword);

        this.txtPassword = UIStyle.setPassField(txtPassword);
        this.txtPassword.setBounds(60, 400, 340, 50);
        this.txtPassword.setFont(UIStyle.font16);
        this.pnRight.add(txtPassword);
    }
    
    private void setupButtons() {
        this.btnLogin = UIStyle.setBtnActive(this.btnLogin, "Đăng nhập");
        this.btnLogin.setBounds(60, 520, 340, 50);
        this.pnRight.add(btnLogin);

        this.lblOr = new JLabel("Hoặc");
        this.lblOr.setFont(UIStyle.font16);
        this.lblOr.setForeground(UIStyle.colorLabel);
        this.lblOr.setBounds(210, 580, 50, 40);
        this.pnRight.add(lblOr);

        this.btnRegister = UIStyle.setBtnSecondary(this.btnRegister, "Đăng ký");
        this.btnRegister.setBounds(60, 630, 340, 50);
        this.pnRight.add(btnRegister);
    }
    
    private void setupActions() {
        al = e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        };
        this.btnRegister.addActionListener(al);

        btnLogin.addActionListener(e -> {
            username = txtUsername.getText().trim();
            String pass = new String(txtPassword.getPassword());
            
            if (username.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập tên đăng nhập và mật khẩu!");
                return;
            }

            boolean ktra = UserDAO.checkLogin(username, pass);
            if (ktra) {
                userid = UserDAO.getUserIdByEmail(username);
                new MainFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Tài khoản hoặc mật khẩu không đúng!");
            }
        });
    }
}