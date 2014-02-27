package ro.swl.engine.parser.test;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import ro.swl.engine.AbstractTest;
import ro.swl.engine.parser.ASTAction;
import ro.swl.engine.parser.ASTActionParam;
import ro.swl.engine.parser.ASTCrud;
import ro.swl.engine.parser.ASTLogic;
import ro.swl.engine.parser.ASTSearcher;
import ro.swl.engine.parser.ASTService;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;


public class LogicTest extends AbstractTest {


	@Test
	public void serviceWithCrudAndSearcher() throws ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream("logic  {" +
							"		service CV { " +
							"			crud CV { " +
							"				searcher WithFirstName {" +
							"					\"Select j from CV j where j.firstName = :firstName\"" +
							"				}" +
							"				searcher Certifications {"+ 
							"					\"Select cert from Certification cert where cert.name like :name\""+
							"				}" +
							"			}" +
							"			" +
							"			void someAction() {"+
							"			}" +
							"		}" +
							"		service Customer { " +
							"			crud Customer { " +
							"				searcher WithFirstName  {" +
							"					\"Select j from Customer j where j.firstName = :firstName\" " +
							"				}" +
							"				searcher CertificationsName {"+ 
							"					\"Select cert from Customer cert where cert.name like :name\""+
							"				}" +
							"			}" +
							"		" +
							"			void someAction() {"+
							"			}" +
							"		}" +
							"  }"));
		//@formatter:on

		ASTLogic logic = swl.Logic();

		List<ASTService> services = logic.getChildNodesOfType(ASTService.class, true);
		assertEquals(2, services.size());

		ASTService cvService = services.get(0);
		assertEquals("CV", cvService.getImage());

		ASTCrud crud1 = cvService.getFirstChildNodeOfType(ASTCrud.class, true);
		assertEquals("CV", crud1.getEntity());

		ASTSearcher searcher1 = crud1.getFirstChildNodeOfType(ASTSearcher.class, true);
		assertEquals("WithFirstName", searcher1.getSearcherName());
		assertEquals("Select j from CV j where j.firstName = :firstName", searcher1.getQuery());

		ASTSearcher searcher2 = crud1.getChildNodesOfType(ASTSearcher.class, true).get(1);
		assertEquals("Certifications", searcher2.getSearcherName());
		assertEquals("Select cert from Certification cert where cert.name like :name", searcher2.getQuery());

		ASTService cvService1 = services.get(1);
		assertEquals("Customer", cvService1.getImage());

		ASTCrud crud2 = cvService1.getFirstChildNodeOfType(ASTCrud.class, true);
		assertEquals("Customer", crud2.getEntity());

		ASTSearcher searcher3 = crud2.getFirstChildNodeOfType(ASTSearcher.class, true);
		assertEquals("WithFirstName", searcher3.getSearcherName());
		assertEquals("Select j from Customer j where j.firstName = :firstName", searcher3.getQuery());

		ASTSearcher searcher4 = crud2.getChildNodesOfType(ASTSearcher.class, true).get(1);
		assertEquals("CertificationsName", searcher4.getSearcherName());
		assertEquals("Select cert from Customer cert where cert.name like :name", searcher4.getQuery());


		List<ASTCrud> cruds = logic.getChildNodesOfType(ASTCrud.class, true);
		assertEquals(2, cruds.size());
	}


	@Test
	public void actionTest() throws Exception {
		//@formatter:off
		SWL swl = new SWL(createInputStream("logic  {" +
									"		service CV { " +
									"			ReturnClass someAction(Customer c , CV d) {"+
									"			}" +
									
									"			ReturnClass2 anotherAction(SomeClass d , SomeOtherClass2 d) {" +
									"			}"+	
									"		}" +
									"		service Customer { " +
									"			crud Customer { " +
									"			}" +
									"		" +
									"			void someAction() {" +
									"				some random content {} " +
									"				21332!@#$%^&*()[];'"+
									"			}" +
									"		}" +
									"  }"));
		//@formatter:on

		ASTLogic logic = swl.Logic();

		List<ASTService> services = logic.getChildNodesOfType(ASTService.class, true);
		assertEquals(2, services.size());

		ASTAction action1 = services.get(0).getFirstChildNodeOfType(ASTAction.class, true);
		assertEquals("ReturnClass", action1.getReturnType());
		ASTActionParam actionP1 = action1.getActionParams().get(0);
		ASTActionParam actionP2 = action1.getActionParams().get(1);

		assertEquals("Customer", actionP1.getType());
		assertEquals("c", actionP1.getName());

		assertEquals("CV", actionP2.getType());
		assertEquals("d", actionP2.getName());


		ASTAction action3 = services.get(1).getFirstChildNodeOfType(ASTAction.class, true);
		assertEquals("void", action3.getReturnType());
		assertEquals(0, action3.getActionParams().size());

	}
}
