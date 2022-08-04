import java.sql.SQLException;

public class FirstBank {
    public static  void main(String[] args) throws SQLException {
        Account rounak = new Account("Shilpa Panwar" ,  "SP00001");
        rounak.showMenu();
    }
}
