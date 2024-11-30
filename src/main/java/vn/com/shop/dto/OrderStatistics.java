package vn.com.shop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderStatistics {
    private BigDecimal totalRevenue;
    private long totalOrders;
    private long completedOrders;
    private long cancelledOrders;
    private List<MonthlyStats> monthlyStats;
    private List<YearlyStats> yearlyStats;

    @Data
    public static class MonthlyStats {
        private int month;
        private int year;
        private BigDecimal revenue;
        private long totalOrders;
        private long completedOrders;
        private long cancelledOrders;
    }

    @Data
    public static class YearlyStats {
        private int year;
        private BigDecimal revenue;
        private long totalOrders;
        private long completedOrders;
        private long cancelledOrders;
    }
}
