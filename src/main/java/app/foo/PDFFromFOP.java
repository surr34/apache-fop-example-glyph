package app.foo;

import java.io.*;
import java.net.URI;

import javax.xml.transform.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.*;

import org.apache.fop.apps.*;
import org.xml.sax.SAXException;

public class PDFFromFOP {
	public static void main(String[] args) {
		try {
			File xmlfile = new File("resources\\organization.xml");
			File xsltfile = new File("resources\\organization.xsl");
			File pdfDir = new File("./Test");
			pdfDir.mkdirs();
			File pdfFile = new File(pdfDir, "organization.pdf");
			System.out.println(pdfFile.getAbsolutePath());
			
			// configure fopFactory as desired
			final FopFactory fopFactory = FopFactory.newInstance(new File("resources\\fop.xconf"));
			FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
				
			// configure foUserAgent as desired
			// Setup output
			OutputStream out = new FileOutputStream(pdfFile);
			out = new java.io.BufferedOutputStream(out);
			
			try {
				// Construct fop with desired output format
				Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
			
				// Setup XSLT
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));
				
				// Setup input for XSLT transformation
				Source src = new StreamSource(xmlfile);
				
				// Resulting SAX events (the generated FO) must be piped through to FOP
				Result res = new SAXResult(fop.getDefaultHandler());
				
				// Start XSLT transformation and FOP processing
				transformer.transform(src, res);
			} catch (FOPException | TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				out.close();
			}
		} catch (IOException exp) {
			exp.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
}