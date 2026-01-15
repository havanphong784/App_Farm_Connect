package Service;

import DBConnect.ProductsDAO;
import DBConnect.StatisticsDAO;
import Model.CartItem;
import Model.OrderItem;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticsService {

    public static DefaultCategoryDataset createRevenueDataset(int userId) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, BigDecimal> revenueData = StatisticsDAO.getRevenueByDate(userId);
        
        for (Map.Entry<String, BigDecimal> entry : revenueData.entrySet()) {
            String date = entry.getKey();
            if (date != null && date.length() >= 10) {
                date = date.substring(8, 10) + "/" + date.substring(5, 7);
            }
            dataset.addValue(entry.getValue(), "Doanh thu", date);
        }
        
        return dataset;
    }

    public static DefaultCategoryDataset createTopProductsDataset(int limit, int userId) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> topProducts = StatisticsDAO.getTopSellingProducts(limit, userId);
        
        for (Map.Entry<String, Integer> entry : topProducts.entrySet()) {
            String name = entry.getKey();
            if (name != null && name.length() > 15) {
                name = name.substring(0, 12) + "...";
            }
            dataset.addValue(entry.getValue(), "Số lượng bán", name);
        }
        
        return dataset;
    }

    public static DefaultPieDataset createProductTypeDataset(int userId) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Integer> productTypes = StatisticsDAO.getProductCountByType(userId);
        
        for (Map.Entry<String, Integer> entry : productTypes.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        
        return dataset;
    }

    public static DefaultCategoryDataset createTopCustomersDataset(int limit) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, java.math.BigDecimal> topCustomers = DBConnect.CustomerDAO.getTopCustomersByRevenue(limit);
        
        for (Map.Entry<String, java.math.BigDecimal> entry : topCustomers.entrySet()) {
            String name = entry.getKey();
            if (name != null && name.length() > 15) {
                name = name.substring(0, 12) + "...";
            }
            dataset.addValue(entry.getValue().divide(java.math.BigDecimal.valueOf(1000)), "Doanh thu (nghìn VND)", name);
        }
        
        return dataset;
    }

    public static String getFormattedTotalRevenue(int userId) {
        BigDecimal total = StatisticsDAO.getTotalRevenue(userId);
        return String.format("%,.0f VNĐ", total);
    }

    public static int getTotalOrders(int userId) {
        return StatisticsDAO.getTotalOrders(userId);
    }

    public static int getTotalProducts(int userId) {
        return StatisticsDAO.getTotalProducts(userId);
    }

    public static class CartService {
        private static CartService instance;
        private List<CartItem> cartItems;

        private CartService() {
            cartItems = new ArrayList<>();
        }

        public static CartService getInstance() {
            if (instance == null) {
                instance = new CartService();
            }
            return instance;
        }

        public List<CartItem> getCartItems() {
            return cartItems;
        }

        public void addToCart(int proId, String proName, BigDecimal unitPrice, int quantity) {
            for (CartItem item : cartItems) {
                if (item.getProId() == proId) {
                    item.setQuantity(item.getQuantity() + quantity);
                    return;
                }
            }
            cartItems.add(new CartItem(proId, proName, unitPrice, quantity));
        }

        public void removeItem(int index) {
            if (index >= 0 && index < cartItems.size()) {
                cartItems.remove(index);
            }
        }

        public void clearCart() {
            cartItems.clear();
        }

        public BigDecimal calculateTotal() {
            BigDecimal total = BigDecimal.ZERO;
            for (CartItem item : cartItems) {
                total = total.add(item.getSubtotal());
            }
            return total;
        }

        public int getItemCount() {
            return cartItems.size();
        }

        public boolean isEmpty() {
            return cartItems.isEmpty();
        }

        /**
         * Handles the checkout process.
         * @param userId The ID of the user creating the order
         * @param customerName Customer name
         * @param customerSdt Customer phone
         * @return Order ID if successful, -1 for stock error, 0 for other errors.
         * @throws Exception if payment fails
         */
        public int checkout(int userId, String customerName, String customerSdt) throws Exception {
            // Validate stock first
            for (CartItem item : cartItems) {
                int currentStock = ProductsDAO.getProductQuantityById(item.getProId());
                if (currentStock < item.getQuantity()) {
                    throw new Exception("Sản phẩm '" + item.getProName() + "' không đủ số lượng. Còn lại: " + currentStock);
                }
            }

            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem item : cartItems) {
                orderItems.add(new OrderItem(item.getProId(), item.getQuantity(), item.getUnitPrice()));
            }

            // Delegate to OrderServer/DAO
            return OrderService.createOrder(userId, orderItems, customerName, customerSdt);
        }
    }
}
