package Service;

import DBConnect.ProductsDAO;
import Model.Products;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import static DBConnect.ProductsDAO.getProductIdByName;
import static UI.LoginFrame.userid;

public class ProductsService {

    public static Object[][] toTableData(ArrayList<Products> list) {
        Object[][] data = new Object[list.size()][6];

        for (int r = 0; r < list.size(); r++) {
            Products p = list.get(r);

            BigDecimal x = p.getPrice().multiply(BigDecimal.valueOf(  (100 - p.getPricePercent()) /100 ));
            BigDecimal y = x.setScale(0, RoundingMode.HALF_UP);

            data[r] = new Object[] {
                    p.getProName(),
                    y,
                    p.getUnit(),
                    p.getQuantity(),
                    p.getDes()
            };
        }
        return data;
    }

    public static boolean insertProduct(String name, String desc, int Quantity, BigDecimal price, String unit, Timestamp expirationDate, int userID) {
        Products products = new Products(name,desc,Quantity,price,unit,expirationDate,userID);
        return ProductsDAO.insertProduct(products);
    }

    public static boolean updateProduct(String name ,String desc,int Quantity, BigDecimal price,float pricePercent ,String unit) {
        Products products = new Products(getProductIdByName(name),name,desc,Quantity,price,unit,pricePercent,userid);
        return ProductsDAO.update(products);
    }

    public static boolean updateCell(int quantity, int proId) {
        Products products = new Products(quantity, proId);
        return ProductsDAO.updateCell(products);
    }

}
