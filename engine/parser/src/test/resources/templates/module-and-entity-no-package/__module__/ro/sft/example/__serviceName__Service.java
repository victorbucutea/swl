#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
#set( $entity = ${service.crudEntity} )
#set( $entityRef = ${service.crudEntity.toLowerCase()})
package ${package}.recruiter.cv;

import java.util.List;

import javax.ejb.Local;

import ${package}.${rootArtifactId}.base.dao.SearchInfo;
import ${package}.${rootArtifactId}.interview.model.CV;

@Local
public interface ${entity}Service {

	public $entity save($entity $entityRef);

	public List<$entity> getAll();

	public $entity find(Long id);

	public List<$entity> search(SearchInfo searcher);

	#foreach($action in  $service.actions)
          $action ;
    #end

}