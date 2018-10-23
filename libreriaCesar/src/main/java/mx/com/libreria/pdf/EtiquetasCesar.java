package mx.com.libreria.pdf;

import java.awt.Color;

import mx.com.libreria.servlets.GettingPaths;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class EtiquetasCesar {
	
	private Font[] fonts;
	//private Paragraph par;
	/**
	 * Get a barcode in Chunk object
	 *  
	 * @param  writer         The writer where the barcode will be added.
	 * @param  barcodeScale   The scale of the barcode
	 * @param  barcodeHeight  The height of the barcode
	 * @param  code           The code of the barcode
	 * @return Chunk          A Chunk that contain the barcode
	 */
	public static Chunk getLeafBarcodeInChunk(PdfWriter writer, float barcodeScale, float barcodeHeight, String code) {
		PdfContentByte cb = writer.getDirectContent();
		Barcode128 code128 = new Barcode128();
		code128.setFont(null);
		code128.setCode(code);
        
		// Encrease the scale to be compatible with the old BarcodeFacade.
		double scaleFraction = 1.2;
		barcodeScale *= scaleFraction;
        
		// Decrease the barcodeHeigth to be compatible with the old BarcodeFacade.
		double heightFraction = 25.0/45.0;
		barcodeHeight *= heightFraction;            
		code128.setBarHeight(barcodeHeight);
  
		Image image128 = code128.createImageWithBarcode(cb, null, null);
		image128.scalePercent(barcodeScale);           
		Chunk chunk = new Chunk(image128, 0, 0);  
            
		return chunk;      
	} 
	
	public void initializeFonts() { 
		
		if (fonts == null || fonts.length <= 0) { 
		
			FontFactory.register("Arial", 
			    GettingPaths.getRutaWeb() + "fonts\\arial.ttf");
	        FontFactory.register("ArialBlack", 
	            GettingPaths.getRutaWeb() + "fonts\\ariblk.ttf");
	        
	        fonts = new Font[4];
	        
	        fonts[0] = FontFactory.getFont(FontFactory.HELVETICA, 14,
	            Font.BOLD, new Color(0x00, 0x00, 0x00));
	        
	        fonts[1] = FontFactory.getFont("ArialBlack", 8,
	            Font.NORMAL, new Color(0x00, 0x00, 0x00));
	        
	        fonts[2] = FontFactory.getFont("Arial", 8, Font.NORMAL,
	            new Color(0x00, 0x00, 0x00));
	        
	        fonts[3] = FontFactory.getFont("Arial", 5, Font.NORMAL,
		            new Color(0x00, 0x00, 0x00));
		}
	}
	/*
	public String crearEtiquetas(final String ruta,
		final String archivo,
	    final ArrayList<InfoEtiquetasPDF> infoPdf) {
		
		Document document = new Document(PageSize.LETTER, 0, 0, 0, 0);
		document.setMargins(0, 0, 0, 0);
		document.left(0);
		document.right(0);
        try {
            //GettingPaths path = new GettingPaths();

        	Logging.debug(EtiquetasCesar.class, "nombreRuta " + ruta + " " + archivo);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(ruta + archivo));

            document.open();
            
            initializeFonts();

            PdfPTable tableCentral = new PdfPTable(21);
            tableCentral.setWidthPercentage(100F);
            tableCentral.setHorizontalAlignment(Element.ALIGN_CENTER);            
            tableCentral.getDefaultCell().setBorder(1); //com.lowagie.text.Rectangle.NO_BORDER);
                        
            for (int i = 0; i < infoPdf.size(); i = i + 2) {
	       
            	InfoEtiquetasPDF info = (InfoEtiquetasPDF) infoPdf.get(i);
            	InfoEtiquetasPDF info2 = null;
            	if (i + 1 < infoPdf.size()) {
            		info2 = (InfoEtiquetasPDF) infoPdf.get(i+1);
            	}
            	            	
	            par = crearParrafo("Proveedor: " + info.getProv() + " -- " + "Producto: " + info.getCodProd(), fonts[1]);
	            agregarTabla(tableCentral, par);
	            
	            agregarEnter(tableCentral);
	            
	            //significa que existe otro elemento
	            if (info2 != null) {
	            	par = crearParrafo("Proveedor: " + info2.getProv() + " -- " + "Producto: " + info2.getCodProd(), fonts[1]);
	            	agregarTabla(tableCentral, par);		            
	            } else {
	            	par = crearParrafo("", fonts[1]);
	            	agregarTabla(tableCentral, par);
	            }
	            
	            par = crearParrafo("Desc Prod: " + info.getDescProducto(), fonts[1]);
	            agregarTabla(tableCentral, par);
	            
	            agregarEnter(tableCentral);
	            
	            if (info2 != null) {
	            	par = crearParrafo("Desc Prod: " + info2.getDescProducto(), fonts[1]);
	            	agregarTabla(tableCentral, par);
	            } else {
	            	par = crearParrafo("", fonts[1]);
	            	agregarTabla(tableCentral, par);
	            }
	            
	            
	            //par = crearParrafo("Peso" + ": " + info.getEnteros() + "." + info.getDecimales(), fonts[1]);
	            //agregarTabla(tableEje, par, "", fonts[2]);
	            
	            //el codigo se alarga en ocasiones
	            //debido a las letras que tiene que representar
	            //en el codigo de barras
	            //checar aquellos casos del abecedario donde
	            //este compuesto de letras como zzzz, finales del abecedario
	            //para ver como las representa.
	            
	            par = creatingTitleBarCode("Peso" + ": " + info.getEnteros() + "." + info.getDecimales(),  
	                info.getCodBarras(), fonts[1], fonts[2], writer, 65, 50, 50);	            	            
	            
	            agregarTabla(tableCentral, par);
	            
	            agregarEnter(tableCentral);
	            
	            if (info2 != null) {
	            	par = creatingTitleBarCode("Peso" + ": " + info2.getEnteros() + "." + info2.getDecimales(),  
	    	                info2.getCodBarras(), fonts[1], fonts[2], writer, 65, 50, 50);	            	            
	    	            
	            	agregarTabla(tableCentral, par);
	            } else {
	            	par = crearParrafo("", fonts[1]);
	            	agregarTabla(tableCentral, par);
	            }	         
	            par = crearParrafo("", fonts[3]);
	            agregarNewLine(tableCentral);	            
	        }                       
            document.add(tableCentral);                        
        } catch (Exception e) {
        	Logging.error(EtiquetasCesar.class, "Error: " + e.toString());
        } finally { 
        	document.close();
        }
        
        return archivo;
	}*/
	
	public void agregarEnter(PdfPTable tableCentral) {
		//espacio de enmedio
        PdfPCell cell = new PdfPCell();		
        cell.setBorder(0);
        cell.setColspan(1);
        tableCentral.addCell(cell);
	}
	
	public void agregarNewLine(PdfPTable tableCentral) {
		PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(21);
        //cell.setFixedHeight(12f);
        //cell.setFixedHeight(10f);
        cell.setFixedHeight(9f);
        tableCentral.addCell(cell);
	}
	
	public Paragraph crearParrafo(final String msg, final Font font) {
		return new Paragraph(new Chunk(msg, font));        
	}
	
	public void agregarTabla(final PdfPTable tableEje, final Paragraph par) { 				
		PdfPCell cell = new PdfPCell(par);		
        cell.setBorder(0);
        cell.setColspan(10);
        tableEje.addCell(cell);               
	}
	
	public Paragraph creatingTitleBarCode(final String titleText, 
	    final String text, 
	    final Font smallFont, final Font bigFont, 
	    final PdfWriter writer, final float scale, final float height, 
	    final int barcodeAlignment) { 

		Paragraph paragraph = new Paragraph();
        Phrase phrase = new Phrase();
        phrase.add(new Chunk(titleText, smallFont));                       
        phrase.add(new Chunk(" - "+ text + "\n\n", bigFont));               
        paragraph.add(phrase);        
        //paragraph.add("         ");               
        paragraph.add(getLeafBarcodeInChunk(writer, scale, height, text));
        //paragraph.setAlignment(barcodeAlignment);        
        return paragraph;     

	}
	public static void main(String[] args) {
		
		//EtiquetasCesar et = new EtiquetasCesar();
		
		GettingPaths.setRutaWeb("c:\\");
		/*
		ArrayList<InfoEtiquetasPDF> infoPdf = new ArrayList<InfoEtiquetasPDF>();
		InfoEtiquetasPDF pdfInfo = new InfoEtiquetasPDF();
		//maximo 32
		pdfInfo.setCodBarras("01234567890123456789123456789012");
		pdfInfo.setCodProd("123123 fasd fd adsf asf fasd asd fasd fas");
		pdfInfo.setDecimales("12");
		pdfInfo.setDescProducto("Nopales sa fadsf asdf as dfas dfa sdf fsd fas asfd asd fasdf gato");
		pdfInfo.setEnteros("123");
		pdfInfo.setProv("Proveedor");
		pdfInfo.setUnidad("Kilos");
		
		infoPdf.add(pdfInfo);
		infoPdf.add(pdfInfo);
		
		infoPdf.add(pdfInfo);
		infoPdf.add(pdfInfo);
		
		infoPdf.add(pdfInfo);
		infoPdf.add(pdfInfo);
		
		infoPdf.add(pdfInfo);
		infoPdf.add(pdfInfo);
		
		infoPdf.add(pdfInfo);
		infoPdf.add(pdfInfo);
		
		infoPdf.add(pdfInfo);
		infoPdf.add(pdfInfo);
		
		infoPdf.add(pdfInfo);
		infoPdf.add(pdfInfo);
		
		infoPdf.add(pdfInfo);
		infoPdf.add(pdfInfo);
		
		infoPdf.add(pdfInfo);
		infoPdf.add(pdfInfo);
		
		infoPdf.add(pdfInfo);
		infoPdf.add(pdfInfo);
		
		et.crearEtiquetas(
	        GettingPaths.getRutaWeb(), "forma" + System.currentTimeMillis() + ".pdf", 
	        infoPdf);
	      */  
	}
 }
