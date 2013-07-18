package ro.sft.recruiter.cv;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ro.sft.recruiter.base.dao.UriSearchInfo;
import ro.sft.recruiter.interview.model.CV;

@Path("/cvs")
@ApplicationPath("/rest")
public class CvRestFacade extends Application {

	@EJB
	CvService cvService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<CV> getAll() {
		return cvService.getAll();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public CV getCv(@PathParam("id") Long id) {
		return cvService.find(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CV saveCv(CV cv) {
		return cvService.saveCV(cv);
	}

	@GET
	@Path("search_{searcherId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CV> search(@PathParam("searcherId") String searcherId, @Context UriInfo uriInfo) {
		return cvService.search(new UriSearchInfo(searcherId, uriInfo));
	}

}
