package app.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * QR Code generator utility
 */
public class QRCodeGenerator {
    private static final int QR_CODE_SIZE = 200;
    private static final String QR_CODE_DIRECTORY = "qr-codes";
    
    /**
     * Generate QR code image and return as byte array
     * 
     * @param content Content to encode in QR
     * @return Byte array of PNG image
     * @throws WriterException If QR generation fails
     * @throws IOException If image conversion fails
     */
    public static byte[] generateQRCodeBytes(String content) throws WriterException, IOException {
        BufferedImage qrImage = generateQRCodeImage(content);
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(qrImage, "PNG", baos);
            return baos.toByteArray();
        }
    }
    
    /**
     * Generate QR code and save to file system (backup)
     * 
     * @param content Content to encode
     * @param ticketId Ticket ID for filename
     * @return Path to saved file
     * @throws WriterException If QR generation fails
     * @throws IOException If file save fails
     */
    public static String saveQRCodeToFile(String content, int ticketId) throws WriterException, IOException {
        // Create directory if not exists
        Path directory = Paths.get(QR_CODE_DIRECTORY);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        
        // Generate QR image
        BufferedImage qrImage = generateQRCodeImage(content);
        
        // Save to file
        String filename = String.format("ticket_%d.png", ticketId);
        File outputFile = new File(directory.toFile(), filename);
        ImageIO.write(qrImage, "PNG", outputFile);
        
        return outputFile.getAbsolutePath();
    }
    
    /**
     * Generate QR code as BufferedImage
     * 
     * @param content Content to encode
     * @return BufferedImage of QR code
     * @throws WriterException If QR generation fails
     */
    public static BufferedImage generateQRCodeImage(String content) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        // Configure QR code parameters
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // High error correction
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1); // Margin around QR code
        
        // Generate QR code matrix
        BitMatrix bitMatrix = qrCodeWriter.encode(
            content, 
            BarcodeFormat.QR_CODE, 
            QR_CODE_SIZE, 
            QR_CODE_SIZE, 
            hints
        );
        
        // Convert to BufferedImage
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
    
    /**
     * Load QR code image from byte array
     * 
     * @param qrBytes Byte array of QR image
     * @return BufferedImage
     * @throws IOException If loading fails
     */
    public static BufferedImage loadQRCodeFromBytes(byte[] qrBytes) throws IOException {
        if (qrBytes == null || qrBytes.length == 0) {
            return null;
        }
        
        try (java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(qrBytes)) {
            return ImageIO.read(bais);
        }
    }
    
    /**
     * Format QR content for ticket
     * 
     * @param ticketId Ticket ID
     * @param licensePlate Vehicle license plate
     * @param timestamp Unix timestamp
     * @return Formatted string for QR code
     */
    public static String formatTicketQRContent(int ticketId, String licensePlate, long timestamp) {
        return String.format("TICKET:%d|PLATE:%s|DATE:%d", ticketId, licensePlate, timestamp);
    }
}
