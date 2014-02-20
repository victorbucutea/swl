package ro.swl.engine.parser.test;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import ro.swl.engine.AbstractTest;
import ro.swl.engine.parser.ASTCrud;
import ro.swl.engine.parser.ASTLogic;
import ro.swl.engine.parser.ASTService;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;


public class LogicTest extends AbstractTest {


	@Test
	public void entityType() throws ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream("logic  {" +
							"		service CV { " +
							"			crud CV { " +
							"				searcher CV.WithFirstName ( firstName ) {" +
							"					\"Select j from CV j where j.firstName = :firstName\"" +
							"				}" +
							"				searcher CV.Certifications(name) {"+ 
							"					\"Select cert from Certification cert where cert.name like :name\""+
							"				}" +
							"			}" +
							"			" +
							"			someAction() {"+
							"			}" +
							"		}" +
							"		service Customer { " +
							"			crud Customer { " +
							"				searcher CV.WithFirstName ( firstName ) {" +
							"					\"Select j from CV j where j.firstName = :firstName\" " +
							"				}" +
							"				searcher CV.Certifications(name) {"+ 
							"					\"Select cert from Certification cert where cert.name like :name\""+
							"				}" +
							"			}" +
							"		" +
							"			someAction() {"+
							"			}" +
							"		}" +
							"  }"));
		//@formatter:on

		ASTLogic logic = swl.Logic();

		List<ASTService> services = logic.getChildNodesOfType(ASTService.class, true);
		assertEquals(2, services.size());

		List<ASTCrud> cruds = logic.getChildNodesOfType(ASTCrud.class, true);
		assertEquals(2, cruds.size());



	}
}
