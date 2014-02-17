#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.base.model.ser;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import ${package}.${rootArtifactId}.base.util.PersistenceUtil;

public class NoLazyLoadingSerializer extends JsonSerializer<Object> { //extends SerializerBase<Object> { //

	@Override
	public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
			JsonGenerationException {

		if (!PersistenceUtil.isinitialized(value)) {
			jgen.writeNull();
		} else {
			provider.defaultSerializeValue(value, jgen);
		}

	}

}
