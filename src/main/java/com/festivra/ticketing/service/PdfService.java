package com.festivra.ticketing.service;

import com.festivra.ticketing.entity.Reservation;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;




@Service
public class PdfService {

    public ByteArrayInputStream generateTicketPdf(Reservation reservation) {
        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("🎟️ Festivra - Ticket de Reserva", titleFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Nombre: " + reservation.getUser().getFullName(), bodyFont));
            document.add(new Paragraph("Email: " + reservation.getUser().getEmail(), bodyFont));
            document.add(new Paragraph("Evento: " + reservation.getEvent().getName(), bodyFont));
            document.add(new Paragraph("Fecha: " + reservation.getEvent().getDate().toString(), bodyFont));
            document.add(new Paragraph("Ubicación: " + reservation.getEvent().getLocation(), bodyFont));
            document.close();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }
}