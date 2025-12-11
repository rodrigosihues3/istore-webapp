package com.istore.appweb.util;

import java.awt.Color;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import com.istore.appweb.entities.Pedidos;
import com.istore.appweb.entities.PedidosItems;

import jakarta.servlet.http.HttpServletResponse;

public class PedidoPDFExporter {
  private Pedidos pedido;

  public PedidoPDFExporter(Pedidos pedido) {
    this.pedido = pedido;
  }

  private void escribirCabeceraTabla(PdfPTable table) {
    PdfPCell cell = new PdfPCell();
    cell.setBackgroundColor(new Color(0, 113, 227)); // Azul iStore
    cell.setPadding(5);

    Font font = FontFactory.getFont(FontFactory.HELVETICA);
    font.setColor(Color.WHITE);

    cell.setPhrase(new Phrase("Producto", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Cant.", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Precio Unit.", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Subtotal", font));
    table.addCell(cell);
  }

  private void escribirDatosTabla(PdfPTable table) {
    for (PedidosItems item : pedido.getItems()) {
      table.addCell(item.getProducto().getNombre());
      table.addCell(String.valueOf(item.getCantidad()));
      table.addCell("S/ " + item.getPrecio());
      table.addCell("S/ " + item.getTotal());
    }
  }

  public void exportar(HttpServletResponse response) throws DocumentException, IOException {
    Document document = new Document(PageSize.A4);
    PdfWriter.getInstance(document, response.getOutputStream());

    document.open();
    Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
    fontTitle.setSize(18);
    fontTitle.setColor(new Color(0, 113, 227));

    Font fontInfo = FontFactory.getFont(FontFactory.HELVETICA);
    fontInfo.setSize(12);

    // 1. Cabecera
    Paragraph pTitle = new Paragraph("iStore Perú - Comprobante de Pago", fontTitle);
    pTitle.setAlignment(Paragraph.ALIGN_CENTER);
    document.add(pTitle);

    Paragraph pSub = new Paragraph("RUC: 20123456789 | Av. San Martín 245, Ica", fontInfo);
    pSub.setAlignment(Paragraph.ALIGN_CENTER);
    pSub.setSpacingAfter(20);
    document.add(pSub);

    // 2. Datos del Pedido y Cliente
    DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    String currentDateTime = dateFormatter.format(java.sql.Timestamp.valueOf(pedido.getFechaCreacion()));

    document.add(new Paragraph("Nro Pedido: #" + pedido.getIdPedido(), fontInfo));
    document.add(new Paragraph("Fecha: " + currentDateTime, fontInfo));

    // Lógica: Si tiene RUC (Factura) mostramos eso, si no, Datos Personales
    String tipoDoc = pedido.getTipoComprobante().getNombre();
    document.add(new Paragraph("Tipo: " + tipoDoc, fontInfo));

    if (pedido.getNumeroDocumento() != null && !pedido.getNumeroDocumento().isEmpty()) {
      document.add(new Paragraph("Cliente/Razón Social: " + pedido.getNombreEntidad(), fontInfo));
      document.add(new Paragraph("Documento: " + pedido.getNumeroDocumento(), fontInfo));
    } else {
      // Fallback si no hay datos de facturación guardados
      document.add(new Paragraph(
          "Cliente: " + pedido.getUsuario().getNombres() + " " + pedido.getUsuario().getApellidos(), fontInfo));
    }

    document.add(new Paragraph(
        "Sede de Recojo: " + (pedido.getDireccionEntrega() != null ? pedido.getDireccionEntrega() : "Sede Central"),
        fontInfo));
    document.add(new Paragraph("\n"));

    // 3. Tabla de Productos
    PdfPTable table = new PdfPTable(4);
    table.setWidthPercentage(100f);
    table.setWidths(new float[] { 4.0f, 1.0f, 2.0f, 2.0f });
    table.setSpacingBefore(10);

    escribirCabeceraTabla(table);
    escribirDatosTabla(table);
    document.add(table);

    // 4. Total
    Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
    fontTotal.setSize(14);
    Paragraph pTotal = new Paragraph("Total Pagado: S/ " + pedido.getTotal(), fontTotal);
    pTotal.setAlignment(Paragraph.ALIGN_RIGHT);
    pTotal.setSpacingBefore(15);
    document.add(pTotal);

    document.close();
  }
}