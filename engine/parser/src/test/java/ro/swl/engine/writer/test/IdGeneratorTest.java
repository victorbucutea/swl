package ro.swl.engine.writer.test;

import static junit.framework.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import ro.swl.engine.AbstractTest;
import ro.swl.engine.parser.ASTCheckbox;
import ro.swl.engine.parser.ASTHorizontalLayout;
import ro.swl.engine.parser.ASTInput;
import ro.swl.engine.parser.ASTLayoutRow;
import ro.swl.engine.parser.ASTRadio;
import ro.swl.engine.parser.ASTVerticalLayout;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.IdGenerator;
import ro.swl.engine.writer.TagWriter;
import ro.swl.engine.writer.WriteException;


public class IdGeneratorTest extends AbstractTest {

	protected TagWriter writer = new TagWriter();


	@After
	public void resetKeyRegistry() {
		IdGenerator.keys = new HashSet<String>();
	}


	@Test
	public void simpleKeys() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" vertical_layout(\"vertical-css-class; some-css: someValue;\") {"+
						                    "row() { text(\"Course Materials\",\"font-size:90%;\")}"+
						                    "row() { radio(materials,\"value1\",\"css1;\")}"+
						                    "row() { radio(materials,\"value2\",\"css2;\")}"+
						                    "row() { checkbox(materials,\"css3;\") } "+ 
						                    "row() { input(modelValue,\"css3;\") }" +
						                    "}"));
		//@formatter:on

		ASTVerticalLayout verticalLayout = swl.VerticalLayout();
		verticalLayout.render(writer);

		assertEquals("vertical_layout", verticalLayout.getId());

		List<ASTLayoutRow> rows = verticalLayout.getRows();

		assertEquals("vertical_layout.row.1.text", rows.get(0).getChildComponents().get(0).getId());
		assertEquals("vertical_layout.row.1", rows.get(0).getId());

		ASTRadio radio = (ASTRadio) rows.get(1).getChildComponents().get(0);

		assertEquals("vertical_layout.row.2.materials", radio.getId());
		assertEquals("vertical_layout.row.2.materials.label", radio.getValueLabel().getId());
		assertEquals("vertical_layout.row.2", rows.get(1).getId());

		radio = (ASTRadio) rows.get(2).getChildComponents().get(0);

		assertEquals("vertical_layout.row.3.materials.label", radio.getValueLabel().getId());
		assertEquals("vertical_layout.row.3.materials", radio.getId());
		assertEquals("vertical_layout.row.3", rows.get(2).getId());

		ASTCheckbox checkbox = (ASTCheckbox) rows.get(3).getChildComponents().get(0);

		assertEquals("vertical_layout.row.4.materials", checkbox.getId());
		assertEquals("vertical_layout.row.4.materials.label", checkbox.getValueLabel().getId());
		assertEquals("vertical_layout.row.4", rows.get(3).getId());

		ASTInput input = (ASTInput) rows.get(4).getChildComponents().get(0);

		assertEquals("vertical_layout.row.5.modelValue.label", input.getLabel().getId());
		assertEquals("vertical_layout.row.5.modelValue", input.getId());
		assertEquals("vertical_layout.row.5", rows.get(4).getId());
	}


	@Test
	public void multipleComponentsInARow() throws Exception {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" vertical_layout(\"vertical-css-class; some-css: someValue;\") {"+
                									"row() {radio(materials,\"value1\") radio(materials,'value2')}" +
                							  "}"));
		//@formatter:on

		ASTVerticalLayout verticalLayout = swl.VerticalLayout();
		verticalLayout.render(writer);

		List<ASTLayoutRow> rows = verticalLayout.getRows();
		ASTRadio radio = (ASTRadio) rows.get(0).getChildComponents().get(0);

		assertEquals("vertical_layout.row.materials.1", radio.getId());
		assertEquals("vertical_layout.row.materials.1.label", radio.getValueLabel().getId());
		assertEquals("vertical_layout.row", rows.get(0).getId());

		radio = (ASTRadio) rows.get(0).getChildComponents().get(1);

		assertEquals("vertical_layout.row.materials.2", radio.getId());
		assertEquals("vertical_layout.row.materials.2.label", radio.getValueLabel().getId());
	}


	@Test
	public void complexMultipleItemsInARow() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" vertical_layout(\"vertical-css-class; some-css: someValue;\") {"+
						                    "row() { horizontal_layout () { " +
						                    							  "column(1) { input(modelValue,\"css3;\") }" +
						                    							  "column(3) { input(modelValue)}" +
						                    							  "}" +
						                           " selectbox(selectValue, selectOptions)" +
						                           "}" +
						                    "}"));
		//@formatter:on
		ASTVerticalLayout verticalLayout = swl.VerticalLayout();
		verticalLayout.render(writer);

		List<ASTLayoutRow> rows = verticalLayout.getRows();
		ASTHorizontalLayout layout = (ASTHorizontalLayout) rows.get(0).getChildComponents().get(0);
		List<ASTInput> childInputs = layout.getChildNodesOfType(ASTInput.class, true);

		assertEquals("vertical_layout.row.horizontal_layout.column.1.modelValue", childInputs.get(0).getId());
		assertEquals("vertical_layout.row.horizontal_layout.column.2.modelValue", childInputs.get(1).getId());
	}


	@Test
	public void horizontalFormId() {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form(\"vertical-css-class;) {"+
						                    "row() { horizontal_layout () { " +
						                    							  "column(1) { input(modelValue,\"css3;\") }" +
						                    							  "column(3) { input(modelValue)}" +
						                    							  "}" +
						                           " selectbox(selectValue, selectOptions)" +
						                           "}" +
						                    "}"));
		//@formatter:on
	}


	@Test
	public void horizontalLayoutId() {

	}


	@Test
	public void verticalLayoutId() {

	}


	@Test
	public void leafComponentIdx() {

	}


	@Test
	public void inputCompKey() {

	}


	public void assertKeyPresent(String key) {
	}


	public void assertValuePresent(String key, String value) {
	}

}
