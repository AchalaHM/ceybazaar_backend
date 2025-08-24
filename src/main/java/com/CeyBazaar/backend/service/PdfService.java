package com.CeyBazaar.backend.service;

import com.CeyBazaar.backend.entity.Order;
import com.CeyBazaar.backend.entity.OrderItem;
import com.CeyBazaar.backend.entity.Product;
import com.CeyBazaar.backend.repository.ProductRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    @Autowired
    private ProductRepository productRepository;

    public byte[] generateOrderPdf(Order order) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Header
            document.add(new Paragraph("CeyBazaar")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            
            document.add(new Paragraph("Order Confirmation")
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));
            
            document.add(new Paragraph("Order #" + order.getOrderId())
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            // Customer Info
            document.add(new Paragraph("Customer Information").setBold().setFontSize(14));
            document.add(new Paragraph("Name: " + order.getOrderDetails().getCustomerName()));
            document.add(new Paragraph("Email: " + order.getOrderDetails().getCustomerEmail()));
            document.add(new Paragraph("Address: " + order.getOrderDetails().getAddress()));
            document.add(new Paragraph("Region: " + order.getOrderDetails().getRegion()));
            document.add(new Paragraph("Order Date: " + order.getAddedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .setMarginBottom(20));

            // Order Items Table
            document.add(new Paragraph("Order Details").setBold().setFontSize(14));
            
            Table table = new Table(UnitValue.createPercentArray(new float[]{3, 1, 2, 2}))
                    .setWidth(UnitValue.createPercentValue(100));
            
            table.addHeaderCell("Product");
            table.addHeaderCell("Qty");
            table.addHeaderCell("Price");
            table.addHeaderCell("Total");

            double subtotal = 0;
            for (OrderItem item : order.getOrderItems()) {
                Product product = productRepository.findById(item.getItemId()).orElse(null);
                if (product != null) {
                    double itemTotal = product.getPrice() * item.getQuantity();
                    subtotal += itemTotal;
                    
                    table.addCell(product.getProductName());
                    table.addCell(String.valueOf(item.getQuantity()));
                    table.addCell("LKR " + String.format("%.2f", product.getPrice()));
                    table.addCell("LKR " + String.format("%.2f", itemTotal));
                }
            }
            
            document.add(table);

            // Summary
            document.add(new Paragraph("\nOrder Summary").setBold().setFontSize(14).setMarginTop(20));
            document.add(new Paragraph("Subtotal: LKR " + String.format("%.2f", subtotal)));
            document.add(new Paragraph("Shipping: LKR " + String.format("%.2f", order.getShippingCost())));
            document.add(new Paragraph("Total Amount: LKR " + String.format("%.2f", order.getTotalCost()))
                    .setBold().setFontSize(12));

            document.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return baos.toByteArray();
    }
}