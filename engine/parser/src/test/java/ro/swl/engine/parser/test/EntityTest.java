package ro.swl.engine.parser.test;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import ro.swl.engine.AbstractTest;
import ro.swl.engine.parser.ASTDomain;
import ro.swl.engine.parser.ASTEntity;
import ro.swl.engine.parser.ASTEnum;
import ro.swl.engine.parser.ASTEnumLiteral;
import ro.swl.engine.parser.ASTProperty;
import ro.swl.engine.parser.ASTSwdlApp;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;


public class EntityTest extends AbstractTest {

	@Test
	public void entityType() throws ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
				" module CV {" +
							"  ui     {} " +
							"  logic  {}" +
							"  domain {" +
							"		enum CompanyType {"+
							"			IT_SOFTWARE,"+
							"			IT_HARDWARE"+
							"		}"+
							""+	
							"	    Experience {"+
							"			startDate Date,"+
							"			endDate   Date,"+
							"			companyType CompanyType,"+
							"			responsabilities String,"+
							"			projects Set<Project>,"+
							" 			otherProjects List<Project>"+
							"		} " +
							"  }" +
				"}"));
		//@formatter:on

		ASTSwdlApp app = swl.SwdlApp();

		ASTDomain cvDomain = app.getModules().get(0).getDomain();

		List<ASTEntity> entities = cvDomain.getEntities();

		assertEquals(1, entities.size());
		ASTEntity entity = entities.get(0);
		assertEquals("Experience", entity.getName());

		List<ASTProperty> properties = entity.getFields();
		assertEquals(6, properties.size());
		assertEquals("startDate", properties.get(0).getName());
		assertEquals("Date", properties.get(0).getType());
		assertEquals("endDate", properties.get(1).getName());
		assertEquals("Date", properties.get(1).getType());
		assertEquals("companyType", properties.get(2).getName());
		assertEquals("CompanyType", properties.get(2).getType());
		assertEquals("responsabilities", properties.get(3).getName());
		assertEquals("String", properties.get(3).getType());
		assertEquals("projects", properties.get(4).getName());
		assertEquals("Set", properties.get(4).getType());
		assertEquals("Project", properties.get(4).getCollectionType());

		List<ASTEnum> enums = cvDomain.getEnums();
		assertEquals(1, enums.size());
		assertEquals("CompanyType", enums.get(0).getName());

		List<ASTEnumLiteral> literals = enums.get(0).getLiterals();
		assertEquals(2, literals.size());

		String enumLit1 = literals.get(0).getName();
		String enumLit2 = literals.get(1).getName();

		assertEquals("IT_SOFTWARE", enumLit1);
		assertEquals("IT_HARDWARE", enumLit2);
	}


	@Test
	public void simple() throws ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
				" module CV {" +
							"  ui     {} " +
							"  logic  {}" +
							"  domain {" + 
							"	    Experience {"+
							"			startDate Date" +
							"		} " +
							"  }" +
				"}"));
		//@formatter:on

		ASTSwdlApp app = swl.SwdlApp();

		ASTEntity astEntity = app.getModules().get(0).getDomain().getEntities().get(0);
		assertEquals("Experience", astEntity.getName());
	}


}
