package Service;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;

public class InvoicePDFUtil {

    public static void exportFromTable(
            DefaultTableModel model,
            int[] rows,
            String filePath
    ) {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Resilient font loading
            Font titleFont, headerFont, normalFont;
            try {
                BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                titleFont = new Font(bf, 18, Font.BOLD);
                headerFont = new Font(bf, 12, Font.BOLD);
                normalFont = new Font(bf, 12);
            } catch (Exception e) {
                System.err.println("Arial not found, using default Helvetica (Vietnamese may not show correctly)");
                titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                normalFont = new Font(Font.FontFamily.HELVETICA, 12);
            }

            Paragraph title = new Paragraph("HOA DON FARM CONNECT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 1, 2, 2});

            addHeader(table, headerFont,
                    "Ten san pham", "So luong", "Don gia", "Thanh tien");

            // Use US locale for parsing internal string if they use dots/commas
            NumberFormat vnd = NumberFormat.getInstance(java.util.Locale.forLanguageTag("vi-VN"));
            double total = 0;

            for (int r : rows) {
                String name = model.getValueAt(r, 0).toString();
                Object priceVal = model.getValueAt(r, 1);
                Object qtyVal = model.getValueAt(r, 2);
                Object sumVal = model.getValueAt(r, 3);

                double price = parseInternalDouble(priceVal);
                int qty = Integer.parseInt(qtyVal.toString());
                double sum = parseInternalDouble(sumVal);
                total += sum;

                table.addCell(new Phrase(name, normalFont));
                table.addCell(new Phrase(String.valueOf(qty), normalFont));
                table.addCell(new Phrase(vnd.format(price), normalFont));
                table.addCell(new Phrase(vnd.format(sum), normalFont));
            }

            document.add(table);

            Paragraph totalP = new Paragraph(
                    "\nTONG TIEN: " + vnd.format(total) + " VND",
                    headerFont
            );
            totalP.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalP);

            document.close();
            JOptionPane.showMessageDialog(null, "Xuất hóa đơn thành công tại: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi xuất PDF: " + e.getMessage());
        }
    }

    private static double parseInternalDouble(Object val) {
        if (val == null) return 0;
        if (val instanceof Number) return ((Number) val).doubleValue();
        String s = val.toString().replace(".", "").replace(",", "");
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0;
        }
    }

    private static void addHeader(PdfPTable table, Font font, String... titles) {
        for (String t : titles) {
            PdfPCell cell = new PdfPCell(new Phrase(t, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPadding(6);
            table.addCell(cell);
        }
    }

    public static void openPDF(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportFromTableModel(
            DefaultTableModel model,
            String filePath,
            java.math.BigDecimal totalAmount
    ) {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont, headerFont, normalFont;
            try {
                BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                titleFont = new Font(bf, 18, Font.BOLD);
                headerFont = new Font(bf, 12, Font.BOLD);
                normalFont = new Font(bf, 12);
            } catch (Exception e) {
                titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                normalFont = new Font(Font.FontFamily.HELVETICA, 12);
            }

            Paragraph title = new Paragraph("HOA DON FARM CONNECT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 1, 2, 2});

            addHeader(table, headerFont,
                    "Ten san pham", "So luong", "Don gia", "Thanh tien");

            for (int i = 0; i < model.getRowCount(); i++) {
                String name = model.getValueAt(i, 0).toString();
                String qty = model.getValueAt(i, 1).toString();
                String price = model.getValueAt(i, 2).toString();
                String sum = model.getValueAt(i, 3).toString();

                table.addCell(new Phrase(name, normalFont));
                table.addCell(new Phrase(qty, normalFont));
                table.addCell(new Phrase(price, normalFont));
                table.addCell(new Phrase(sum, normalFont));
            }

            document.add(table);

            NumberFormat vnd = NumberFormat.getInstance(java.util.Locale.forLanguageTag("vi-VN"));
            Paragraph totalP = new Paragraph(
                    "\nTONG TIEN: " + vnd.format(totalAmount) + " VND",
                    headerFont
            );
            totalP.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalP);

            document.close();
            JOptionPane.showMessageDialog(null, "Xuất hóa đơn thành công tại: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi xuất PDF: " + e.getMessage());
        }
    }
}

