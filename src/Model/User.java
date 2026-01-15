package Model;

public class User {
    private int ID;
    private String Email,Password,Name,Addres;

    public User(int ID, String email, String password, String name, String addres) {
        this.ID = ID;
        Email = email;
        Password = password;
        Name = name;
        Addres = addres;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddres() {
        return Addres;
    }

    public void setAddres(String addres) {
        Addres = addres;
    }
}
