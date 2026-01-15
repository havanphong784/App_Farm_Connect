package Service;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;

import javax.swing.table.DefaultTableModel;
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
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

            Paragraph title = new Paragraph("HOA DON FARM CONNECT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 1, 2, 2});

            addHeader(table, headerFont,
                    "Ten san pham", "So luong", "Don gia", "Thanh tien");

            NumberFormat vnd = NumberFormat.getInstance(java.util.Locale.of("vi", "VN"));
            double total = 0;

            for (int r : rows) {
                String name = model.getValueAt(r, 0).toString();
                double price = Double.parseDouble(model.getValueAt(r, 1).toString());
                int qty = Integer.parseInt(model.getValueAt(r, 2).toString());
                double sum = Double.parseDouble(model.getValueAt(r, 3).toString());
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

        } catch (Exception e) {
            e.printStackTrace();
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
            Desktop.getDesktop().open(new File(path));
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
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

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
                int qty = Integer.parseInt(model.getValueAt(i, 1).toString());
                String price = model.getValueAt(i, 2).toString();
                String sum = model.getValueAt(i, 3).toString();

                table.addCell(new Phrase(name, normalFont));
                table.addCell(new Phrase(String.valueOf(qty), normalFont));
                table.addCell(new Phrase(price, normalFont));
                table.addCell(new Phrase(sum, normalFont));
            }

            document.add(table);

            NumberFormat vnd = NumberFormat.getInstance(java.util.Locale.of("vi", "VN"));
            Paragraph totalP = new Paragraph(
                    "\nTONG TIEN: " + vnd.format(totalAmount) + " VND",
                    headerFont
            );
            totalP.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalP);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

