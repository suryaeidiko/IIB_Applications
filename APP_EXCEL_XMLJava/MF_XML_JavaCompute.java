

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;
import com.ibm.broker.plugin.MbXMLNSC;

public class MF_XML_JavaCompute extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		// MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
			// create new message as a copy of the input
			MbMessage outMessage = new MbMessage(inMessage);
			outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			
			// ----------------------------------------------------------
			// Add user code below
			// get InputBody
			MbElement inputBlob = inAssembly.getMessage().getRootElement().getLastChild();
			byte[] originalMsgByteArray = (byte[])inputBlob.getLastChild().getValue();
			InputStream stream = new ByteArrayInputStream(originalMsgByteArray);
			
			parseXLSX ( stream, outMessage);
			
					
			// End of user code
			// ----------------------------------------------------------
		} catch (MbException e) {
			// Re-throw to allow Broker handling of MbException
			throw e;
		} catch (RuntimeException e) {
			// Re-throw to allow Broker handling of RuntimeException
			throw e;
		} catch (Exception e) {
			// Consider replacing Exception with type(s) thrown by user code
			// Example handling ensures all exceptions are re-thrown to be handled in the flow
			throw new MbUserException(this, "evaluate()", "", "", e.toString(),
					null);
		}
		// The following should only be changed
		// if not propagating message to the 'out' terminal
		out.propagate(outAssembly);

	}

	/**
	 * onPreSetupValidation() is called during the construction of the node
	 * to allow the node configuration to be validated.  Updating the node
	 * configuration or connecting to external resources should be avoided.
	 *
	 * @throws MbException
	 */

	
public void parseXLSX ( InputStream fis, MbMessage outMessage){
		
		try {

			MbElement outRoot = outMessage.getRootElement();
			
			// create XMNLSC parser
			MbElement outBody = outRoot.createElementAsLastChild(MbXMLNSC.PARSER_NAME);
			
			// Create root element.
			MbElement excelRoot = outBody.createElementAsLastChild(MbElement.TYPE_NAME, "excel", null);
			
			DataFormatter formatter = new DataFormatter();
			
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet1 = wb.getSheetAt(0);
			
			for ( org.apache.poi.ss.usermodel.Row row : sheet1) {
				// create a row element for current excel row.
     		MbElement rowMsgElement = excelRoot.createElementAsLastChild(MbElement.TYPE_NAME, "row", null);
				
			    for (Cell cell :row) {
			        // get cell value as text
			        String text = formatter.formatCellValue(cell);	        
			        
			        // create an element called cell in output message with value as cell value
			        rowMsgElement.createElementAsLastChild(MbElement.TYPE_NAME,"cell",text);
			    }
			}
			
		} catch (Exception e) {
	
			e.printStackTrace();
		}
}
}