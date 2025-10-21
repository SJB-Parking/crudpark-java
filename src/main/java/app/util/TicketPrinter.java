package app.util;

import app.model.Ticket;
import app.service.ParkingService;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for printing tickets
 */
public class TicketPrinter {
    
    /**
     * Shows a printer selection dialog and prints an entry ticket
     */
    public static void printEntryTicket(Ticket ticket) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        
        // Set up page format for thermal printer (58mm width)
        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = pageFormat.getPaper();
        
        // 58mm = ~165 points (1mm ≈ 2.83 points)
        // Set paper size for 58mm thermal printer
        double width = 165;  // 58mm in points
        double height = 842; // Allow long receipt (A4 height as max)
        paper.setSize(width, height);
        
        // Set margins (very small for thermal printers)
        double margin = 5;
        paper.setImageableArea(margin, margin, width - 2 * margin, height - 2 * margin);
        
        pageFormat.setPaper(paper);
        
        // Create the printable content
        printerJob.setPrintable(new EntryTicketPrintable(ticket), pageFormat);
        
        // Show print dialog to let user select printer
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
                JOptionPane.showMessageDialog(null,
                    "¡Ticket enviado a impresora exitosamente!",
                    "Impresión Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(null,
                    "Error al imprimir ticket: " + ex.getMessage(),
                    "Error de Impresión",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Shows a printer selection dialog and prints an exit ticket
     */
    public static void printExitTicket(ParkingService.ExitResult exitResult) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        
        // Set up page format for thermal printer (58mm width)
        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = pageFormat.getPaper();
        
        // 58mm = ~165 points (1mm ≈ 2.83 points)
        // Set paper size for 58mm thermal printer
        double width = 165;  // 58mm in points
        double height = 842; // Allow long receipt (A4 height as max)
        paper.setSize(width, height);
        
        // Set margins (very small for thermal printers)
        double margin = 5;
        paper.setImageableArea(margin, margin, width - 2 * margin, height - 2 * margin);
        
        pageFormat.setPaper(paper);
        
        // Create the printable content
        printerJob.setPrintable(new ExitTicketPrintable(exitResult), pageFormat);
        
        // Show print dialog to let user select printer
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
                JOptionPane.showMessageDialog(null,
                    "¡Recibo enviado a impresora exitosamente!",
                    "Impresión Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(null,
                    "Error al imprimir recibo: " + ex.getMessage(),
                    "Error de Impresión",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Printable class for entry tickets
     */
    private static class EntryTicketPrintable implements Printable {
        private final Ticket ticket;
        
        public EntryTicketPrintable(Ticket ticket) {
            this.ticket = ticket;
        }
        
        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }
            
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            
            // Set up fonts for thermal printer (smaller sizes)
            Font titleFont = new Font("Monospaced", Font.BOLD, 10);
            Font headerFont = new Font("Monospaced", Font.BOLD, 8);
            Font normalFont = new Font("Monospaced", Font.PLAIN, 7);
            Font boldFont = new Font("Monospaced", Font.BOLD, 7);
            
            int y = 10;
            int leftMargin = 5;
            int lineHeight = 12;
            int availableWidth = (int)pageFormat.getImageableWidth();
            
            // Title - centered
            g2d.setFont(titleFont);
            String title = "CRUDPARK";
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g2d.drawString(title, leftMargin + (availableWidth - titleWidth) / 2, y);
            y += 15;
            
            // Subtitle - centered
            g2d.setFont(headerFont);
            String subtitle = "TICKET DE ENTRADA";
            fm = g2d.getFontMetrics();
            int subtitleWidth = fm.stringWidth(subtitle);
            g2d.drawString(subtitle, leftMargin + (availableWidth - subtitleWidth) / 2, y);
            y += 12;
            
            // Separator line
            g2d.drawLine(leftMargin, y, leftMargin + availableWidth, y);
            y += 10;
            
            // Format entry datetime
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            String formattedEntryTime = dateFormat.format(new Date(ticket.getEntryDatetime().getTime()));
            
            // Ticket information (compact format)
            g2d.setFont(normalFont);
            
            g2d.drawString("Ticket: " + String.format("%06d", ticket.getId()), leftMargin, y);
            y += lineHeight;
            
            g2d.drawString("Folio: " + ticket.getFolio(), leftMargin, y);
            y += lineHeight;
            
            // License plate - wrap if too long
            String plateText = "Placa: " + ticket.getLicensePlate();
            g2d.setFont(boldFont);
            g2d.drawString(plateText, leftMargin, y);
            y += lineHeight;
            
            g2d.setFont(normalFont);
            g2d.drawString("Tipo: " + VehicleTypeTranslator.toSpanishShort(ticket.getVehicleType()), leftMargin, y);
            y += lineHeight;
            
            g2d.drawString("Clase: " + ticket.getTicketType(), leftMargin, y);
            y += lineHeight;
            
            // Entry time - may need to wrap
            g2d.drawString("Entrada:", leftMargin, y);
            y += lineHeight;
            g2d.drawString(formattedEntryTime, leftMargin, y);
            y += lineHeight + 5;
            
            // Separator line
            g2d.drawLine(leftMargin, y, leftMargin + availableWidth, y);
            y += 10;
            
            // QR Code - smaller for thermal printer
            try {
                BufferedImage qrImage = QRCodeGenerator.generateQRCodeImage(ticket.getQrCodeData());
                int qrSize = Math.min(100, availableWidth - 10); // Max 100px or available width
                int qrX = leftMargin + (availableWidth - qrSize) / 2; // Center QR
                g2d.drawImage(qrImage, qrX, y, qrSize, qrSize, null);
                y += qrSize + 8;
            } catch (Exception e) {
                g2d.setFont(normalFont);
                g2d.drawString("QR generation", leftMargin, y);
                y += lineHeight;
                g2d.drawString("failed", leftMargin, y);
                y += lineHeight;
            }
            
            // Footer
            g2d.drawLine(leftMargin, y, leftMargin + availableWidth, y);
            y += 8;
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 6));
            
            // Word wrap for footer text
            String footer1 = "Conserve el ticket";
            String footer2 = "¡Gracias!";
            
            fm = g2d.getFontMetrics();
            int footer1Width = fm.stringWidth(footer1);
            g2d.drawString(footer1, leftMargin + (availableWidth - footer1Width) / 2, y);
            y += 10;
            
            int footer2Width = fm.stringWidth(footer2);
            g2d.drawString(footer2, leftMargin + (availableWidth - footer2Width) / 2, y);
            
            return PAGE_EXISTS;
        }
    }
    
    /**
     * Printable class for exit tickets (receipts)
     */
    private static class ExitTicketPrintable implements Printable {
        private final ParkingService.ExitResult exitResult;
        
        public ExitTicketPrintable(ParkingService.ExitResult exitResult) {
            this.exitResult = exitResult;
        }
        
        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }
            
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            
            // Set up fonts for thermal printer (smaller sizes)
            Font titleFont = new Font("Monospaced", Font.BOLD, 10);
            Font headerFont = new Font("Monospaced", Font.BOLD, 8);
            Font normalFont = new Font("Monospaced", Font.PLAIN, 7);
            Font boldFont = new Font("Monospaced", Font.BOLD, 7);
            Font amountFont = new Font("Monospaced", Font.BOLD, 10);
            
            int y = 10;
            int leftMargin = 5;
            int lineHeight = 12;
            int availableWidth = (int)pageFormat.getImageableWidth();
            
            Ticket ticket = exitResult.getTicket();
            
            // Title - centered
            g2d.setFont(titleFont);
            String title = "CRUDPARK";
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g2d.drawString(title, leftMargin + (availableWidth - titleWidth) / 2, y);
            y += 15;
            
            // Subtitle - centered
            g2d.setFont(headerFont);
            String subtitle = "RECIBO DE PAGO";
            fm = g2d.getFontMetrics();
            int subtitleWidth = fm.stringWidth(subtitle);
            g2d.drawString(subtitle, leftMargin + (availableWidth - subtitleWidth) / 2, y);
            y += 12;
            
            // Separator line
            g2d.drawLine(leftMargin, y, leftMargin + availableWidth, y);
            y += 10;
            
            // Format entry and exit datetime
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            String formattedEntryTime = dateFormat.format(new Date(ticket.getEntryDatetime().getTime()));
            String formattedExitTime = dateFormat.format(new Date(exitResult.getExitTime().getTime()));
            
            // Calculate duration
            long hours = exitResult.getDurationMinutes() / 60;
            long minutes = exitResult.getDurationMinutes() % 60;
            
            // Ticket information (compact format)
            g2d.setFont(normalFont);
            
            g2d.drawString("Ticket: " + String.format("%06d", ticket.getId()), leftMargin, y);
            y += lineHeight;
            
            g2d.drawString("Folio: " + ticket.getFolio(), leftMargin, y);
            y += lineHeight;
            
            // License plate
            String plateText = "Placa: " + ticket.getLicensePlate();
            g2d.setFont(boldFont);
            g2d.drawString(plateText, leftMargin, y);
            y += lineHeight;
            
            g2d.setFont(normalFont);
            g2d.drawString("Tipo: " + VehicleTypeTranslator.toSpanishShort(ticket.getVehicleType()), leftMargin, y);
            y += lineHeight;
            
            g2d.drawString("Clase: " + ticket.getTicketType(), leftMargin, y);
            y += lineHeight + 5;
            
            // Separator line
            g2d.drawLine(leftMargin, y, leftMargin + availableWidth, y);
            y += 10;
            
            // Time information
            g2d.drawString("Entrada:", leftMargin, y);
            y += lineHeight;
            g2d.drawString(formattedEntryTime, leftMargin, y);
            y += lineHeight;
            
            g2d.drawString("Salida:", leftMargin, y);
            y += lineHeight;
            g2d.drawString(formattedExitTime, leftMargin, y);
            y += lineHeight;
            
            g2d.drawString("Duracion:", leftMargin, y);
            y += lineHeight;
            g2d.drawString(String.format("%dh %dm", hours, minutes), leftMargin, y);
            y += lineHeight + 5;
            
            // Separator line
            g2d.drawLine(leftMargin, y, leftMargin + availableWidth, y);
            y += 12;
            
            // Payment amount - centered and prominent
            g2d.setFont(boldFont);
            String payLabel = "TOTAL A PAGAR:";
            fm = g2d.getFontMetrics();
            int payLabelWidth = fm.stringWidth(payLabel);
            g2d.drawString(payLabel, leftMargin + (availableWidth - payLabelWidth) / 2, y);
            y += 15;
            
            g2d.setFont(amountFont);
            String amountText;
            if (exitResult.isFree()) {
                amountText = "GRATIS";
                g2d.drawString(amountText, leftMargin + (availableWidth - fm.stringWidth(amountText)) / 2, y);
                y += 12;
                g2d.setFont(new Font("Monospaced", Font.PLAIN, 6));
                String reason = "(" + exitResult.getFreeReason() + ")";
                fm = g2d.getFontMetrics();
                g2d.drawString(reason, leftMargin + (availableWidth - fm.stringWidth(reason)) / 2, y);
            } else {
                amountText = String.format("$%.2f", exitResult.getAmount());
                fm = g2d.getFontMetrics();
                g2d.drawString(amountText, leftMargin + (availableWidth - fm.stringWidth(amountText)) / 2, y);
            }
            y += 15;
            
            // Separator line
            g2d.drawLine(leftMargin, y, leftMargin + availableWidth, y);
            y += 8;
            
            // Footer
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 6));
            SimpleDateFormat footerDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String printTime = "Impreso: " + footerDateFormat.format(new Date());
            fm = g2d.getFontMetrics();
            g2d.drawString(printTime, leftMargin + (availableWidth - fm.stringWidth(printTime)) / 2, y);
            y += 10;
            
            String thanks = "¡Gracias!";
            fm = g2d.getFontMetrics();
            g2d.drawString(thanks, leftMargin + (availableWidth - fm.stringWidth(thanks)) / 2, y);
            y += 10;
            
            String safety = "¡Conduce seguro!";
            fm = g2d.getFontMetrics();
            g2d.drawString(safety, leftMargin + (availableWidth - fm.stringWidth(safety)) / 2, y);
            
            return PAGE_EXISTS;
        }
    }
}
