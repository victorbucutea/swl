#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
#set( $entity = ${service.crudEntity} )
#set( $entityRef = ${service.crudEntity.toLowerCase()})
package ${package}.${rootArtifactId}.cv;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ${package}.${rootArtifactId}.base.dao.CrudDao;
import ${package}.${rootArtifactId}.base.dao.SearchInfo;
import ${package}.${rootArtifactId}.interview.model.CV;

@Stateless
public class ${service.name}ServiceBean implements CvService {

	@EJB
	private CrudDao<${entity}> dao;

	@Override
	public ${entity} save(${entity} ${entityRef} ) {
		/*
		 * Incoming entity is unmanaged so we cannot simply merge() it into the
		 * context. Child entities which are not present will be assumed by the
		 * entity manager as 'deleted', and what happened in fact was just that
		 * the UI ( or whatever client ) did not have that information to start
		 * with.
		 * Management is done on reference level so the only solution to map an
		 * incoming information into a managed entity is to copy deserialized
		 * data into 'managed data'.
		 * 
		 * The copy operation has no effect in case the entity which was passed
		 * in is already a managed entity. It will simply copy the new values
		 * (if there were any updates) over the existing ones.
		 */
		${entity} managed${entity} = find(${entityRef}.getId());
		managed${entity}.merge(${entityRef});
		return dao.save(managed${entity});
	}

	@Override
	public List<${entity}> getAll() {
		return dao.findByNamedQuery(${entity}.ALL);
	}

	@Override
	public ${entity} find(Long id) {
		return dao.find(id, ${entity}.class);
	}

	@Override
	public List<${entity}> search(SearchInfo searcher) {
		return dao.findByNamedQuery(searcher.getSearcherId(), searcher.getParamMap());
	}

	#foreach($action in  $service.actions)
          $action {}
    #end

}
