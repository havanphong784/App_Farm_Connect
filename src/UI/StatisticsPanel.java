package UI;

import Service.StatisticsService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StatisticsPanel extends JPanel {
    private ChartPanel cpRevenue, cpTopProducts, cpProductType, cpStockStatus;
    private JFreeChart fcRevenue, fcTopProducts, fcProductType, fcStockStatus;
    private JPanel pnCharts;

    public StatisticsPanel() {
        this.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.setLayout(new BorderLayout());
        this.setBackground(UIStyle.colorBg);

        JLabel lblTitle = new JLabel("  📊 Biểu Đồ Thống Kê");
        lblTitle.setFont(UIStyle.font24Bold);
        lblTitle.setForeground(new Color(30, 90, 150));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel pnChartsWrapper = new JPanel(new BorderLayout());
        pnChartsWrapper.setBackground(new Color(240, 248, 255));
        pnChartsWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 240), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        pnCharts = new JPanel(new GridLayout(2, 2, 15, 15));
        pnCharts.setBackground(new Color(240, 248, 255));
        pnCharts.setOpaque(false);
        pnChartsWrapper.add(pnCharts, BorderLayout.CENTER);

        createRevenueChart();
        createTopProductsChart();
        createProductTypeChart();
        createStockStatusChart();

        this.add(pnChartsWrapper, BorderLayout.CENTER);
    }

    private void createRevenueChart() {
        DefaultCategoryDataset dataset = StatisticsService.createRevenueDataset(UI.LoginFrame.userid);

        fcRevenue = ChartFactory.createLineChart(
            "Doanh Thu 7 Ngày",
            "Ngày",
            "VND",
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );

        customizeChart(fcRevenue);
        CategoryPlot plot = fcRevenue.getCategoryPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, UIStyle.colorInfo);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesShapesVisible(0, true);

        cpRevenue = createChartPanel(fcRevenue);
        pnCharts.add(cpRevenue);
    }

    private void createTopProductsChart() {
        DefaultCategoryDataset dataset = StatisticsService.createTopProductsDataset(5, UI.LoginFrame.userid);

        fcTopProducts = ChartFactory.createBarChart(
            "Top 5 Sản Phẩm Bán Chạy",
            "Sản phẩm",
            "Số lượng",
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );

        customizeChart(fcTopProducts);
        CategoryPlot plot = fcTopProducts.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, UIStyle.colorSuccess);
        renderer.setDrawBarOutline(false);

        cpTopProducts = createChartPanel(fcTopProducts);
        pnCharts.add(cpTopProducts);
    }

    private void createProductTypeChart() {
        DefaultPieDataset dataset = StatisticsService.createProductTypeDataset(UI.LoginFrame.userid);

        fcProductType = ChartFactory.createPieChart(
            "Phân Bố Theo Loại",
            dataset,
            true, true, false
        );

        customizeChart(fcProductType);
        PiePlot plot = (PiePlot) fcProductType.getPlot();
        plot.setBackgroundPaint(UIStyle.colorBgCard);
        plot.setOutlineVisible(false);
        plot.setLabelFont(UIStyle.font12);
        plot.setShadowPaint(null);

        Color[] colors = {
            new Color(34, 139, 34),
            new Color(23, 162, 184),
            new Color(255, 193, 7),
            new Color(220, 53, 69),
            new Color(111, 66, 193),
            new Color(253, 126, 20),
            new Color(32, 201, 151)
        };

        int i = 0;
        for (Object key : dataset.getKeys()) {
            plot.setSectionPaint((Comparable<?>) key, colors[i % colors.length]);
            i++;
        }

        cpProductType = createChartPanel(fcProductType);
        pnCharts.add(cpProductType);
    }

    private void createStockStatusChart() {
        DefaultCategoryDataset dataset = StatisticsService.createTopCustomersDataset(5);

        fcStockStatus = ChartFactory.createBarChart(
            "Top Khách Hàng Doanh Thu Cao",
            "Khách hàng",
            "Doanh thu (K VND)",
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );

        customizeChart(fcStockStatus);
        CategoryPlot plot = fcStockStatus.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(139, 92, 246));
        renderer.setDrawBarOutline(false);

        cpStockStatus = createChartPanel(fcStockStatus);
        pnCharts.add(cpStockStatus);
    }

    private void customizeChart(JFreeChart chart) {
        chart.setBackgroundPaint(UIStyle.colorBgCard);

        TextTitle title = chart.getTitle();
        title.setFont(UIStyle.font16Bold);
        title.setPaint(UIStyle.colorText);

        if (chart.getLegend() != null) {
            chart.getLegend().setBackgroundPaint(UIStyle.colorBgCard);
            chart.getLegend().setItemFont(UIStyle.font12);
        }

        if (chart.getPlot() instanceof CategoryPlot) {
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            plot.setBackgroundPaint(UIStyle.colorBgCard);
            plot.setRangeGridlinePaint(UIStyle.colorBorder);
            plot.setDomainGridlinePaint(UIStyle.colorBorder);
            plot.getDomainAxis().setTickLabelFont(UIStyle.font12);
            plot.getDomainAxis().setLabelFont(UIStyle.font14);
            plot.getRangeAxis().setTickLabelFont(UIStyle.font12);
            plot.getRangeAxis().setLabelFont(UIStyle.font14);
        }
    }

    private ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart);
        panel.setBackground(UIStyle.colorBgCard);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyle.colorBorder, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setMouseWheelEnabled(true);
        return panel;
    }

    public void refreshCharts() {
        pnCharts.removeAll();
        createRevenueChart();
        createTopProductsChart();
        createProductTypeChart();
        createStockStatusChart();

        this.revalidate();
        this.repaint();
    }
}
