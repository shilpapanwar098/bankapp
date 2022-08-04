import java.sql.*;

public class conn {
    Connection con;
    Statement s;
    public  conn() {
        try {
            con = DriverManager.getConnection("jdbc:mysql:///bankaccount", "root", "rgmbps176");
            s = con.createStatement();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
