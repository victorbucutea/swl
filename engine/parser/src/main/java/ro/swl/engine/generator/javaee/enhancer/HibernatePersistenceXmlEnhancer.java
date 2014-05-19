package ro.swl.engine.generator.javaee.enhancer;

import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.javaee.model.PersistenceXml;
import ro.swl.engine.parser.ASTSwdlApp;


public class HibernatePersistenceXmlEnhancer extends Enhancer<PersistenceXml> {

	@Override
	public void enhance(ASTSwdlApp appModel, PersistenceXml r) throws CreateException {

		r.setPersistenceProvider("org.hibernate.ejb.HibernatePersistence");

		/*
		 * <property name="hibernate.dialect"
		 * value="org.hibernate.dialect.MySQLDialect" />
		 * <property name="hibernate.show_sql" value="true" />
		 * <property name="hibernate.format_sql" value="true" />
		 * <property name="hibernate.use_sql_comments" value="false" />
		 * <property name="hibernate.hbm2ddl.auto" value="update" />
		 */


		r.addPersistenceProperty("hibernate.show_sql", "true");
	}
}
