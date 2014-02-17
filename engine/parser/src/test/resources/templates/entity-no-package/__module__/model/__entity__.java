#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * 
 */
package ${package}.${rootArtifactId}.interview.model;

import static com.google.common.collect.Sets.newHashSet;
import static ${package}.${rootArtifactId}.base.util.PersistenceUtil.isinitialized;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonProperty;

import ${package}.${rootArtifactId}.base.model.AuditedEntity;

@SuppressWarnings("serial")
@javax.persistence.Entity
// @formatter:off
@NamedQueries(value = {
	@NamedQuery(name = ${entity}.ALL, query = "Select j from $entity j") ,
	#foreach($query in  $entity.namedQueries)
         @NamedQuery(name = $query.name, query = "${query.hqlString}"),
    #end
	})
// @formatter:on
@Table(name = "${entity.tableName}")
public class $entity extends AuditedEntity {

	public static final String ALL = "${$entity}_All";

	#foreach($query in  $entity.namedQueries)
	public static final String $query.name = "$query.identifier";
    #end
	
	#foreach($attribute in $entity.attributes)
	@Column(name = "$attribute.columnName")
	$attribute.persistenceAnnotation()
	private $attribute.classStr $attribute;
	
	#if($attribute.isEnum())
	@Transient
	public $attribute.classStr ${attribute}s = ${attribute.classStr}.values();
	#end
	
	#end

	#foreach($oneToManyAttr in $entity.oneToManyAttributes)
	@JsonIgnore
	@OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
	private $oneToManyAttr.classStr ${oneToManyAttr};
	#end
	
	// one-to-one mappings
	#foreach($oneToOneAttr in $entity.oneToOneAttributes)
	@OneToOne(cascade = { CascadeType.ALL })
	@JsonManagedReference
	private $oneToOneAttr.classStr $oneToOneAttr;
	#end

	// many-to-one mappings
	#foreach($manyToOneAttr in $entity.manyToOneAttributes)
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "manyToOneAttr.referencedIdColumn")
	private $manyToOneAttr.classStr	$manyToOneAttr.reference;
	#end
	
	// one-to-many attributes adder methods, serialize methods and getters and setters
	#foreach(oneToManyAttr in $entity.oneToManyAttributes)
	public void add${oneToManyAttr.classStr}(${oneToManyAttr.classStr}... new${oneToManyAttr}){
		if ( ${oneToManyAttr} == null ) {
			return;
		}
		
		if (${oneToManyAttr} == null) {
			${oneToManyAttr} = newHashSet();
		}

		for (${oneToManyAttr.classStr} rel : new${oneToManyAttr}) {
			rel.${oneToManyAttr.relationSetter()}(this);
			${oneToManyAttr}.add(rel);
		}
	
	}
	
	@JsonProperty("${oneToManyAttr}")
	@JsonManagedReference
	public $oneToManyAttr.classStr serialize${oneToManyAttr.classStr}() {
	
		if(${oneToManyAttr} == null ) {
			return newHashSet();
		}
			
		if (isinitialized(${oneToManyAttr})) {
			return ${oneToManyAttr};
		}

		return newHashSet();
		
		
	}
	
	@JsonProperty("${oneToManyAttr}")
	public void set${oneToManyAttr.classStr}s(${oneToManyAttr.classStr} attr) {
		this.${oneToManyAttr} = attr;
	}
	
	public ${oneToManyAttr.classStr} get${oneToManyAttr.getterName()}s() {
		return ${oneToManyAttr};
	}
	#end
	
	
	// attribute getters and setters 
	#foreach($attribute in $entity.attributes)
	public void set${attribute.setterName()} ($attribute.classStr attr) {
		this.$attribute = attr;
	}
	
	public ${attribute.classStr} get${attribute.getterName()}() {
		return $attribute;
	}
	
	#end

	
	// many-to-one getters and setters TODO
	
	
	// one-to-one getters and setters TODO
}