package ro.swl.engine.generator.javaee.factory;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static ro.swl.engine.generator.GlobalContext.getGlobalCtxt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.CreationContext;
import ro.swl.engine.generator.ResourceFactory;
import ro.swl.engine.generator.java.model.PackageResource;
import ro.swl.engine.generator.javaee.exception.DuplicateEntityException;
import ro.swl.engine.generator.javaee.exception.DuplicateFieldNameException;
import ro.swl.engine.generator.javaee.exception.InvalidPackageException;
import ro.swl.engine.generator.javaee.exception.NoModuleException;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.generator.javaee.model.ModuleResource;
import ro.swl.engine.generator.javaee.model.PersistenceXml;
import ro.swl.engine.generator.javaee.model.ServiceResource;
import ro.swl.engine.generator.javaee.model.WebXml;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTDomain;
import ro.swl.engine.parser.ASTEntity;
import ro.swl.engine.parser.ASTLogic;
import ro.swl.engine.parser.ASTModule;
import ro.swl.engine.parser.ASTProperty;
import ro.swl.engine.parser.ASTService;
import ro.swl.engine.parser.ASTSwdlApp;


public class JavaEEResourceFactory extends ResourceFactory {


	public JavaEEResourceFactory(ASTSwdlApp appmodel, CreationContext generationContext) {
		super(appmodel, generationContext);
	}


	@Override
	public List<? extends Resource> createResource(Resource parent, File templateFile) throws CreateException {

		if (isWebXml(templateFile)) {
			return createWebXmlResource(parent, templateFile);

		} else if (isModuleTemplate(templateFile)) {
			return createModuleResources(parent, templateFile);

		} else if (isServiceTemplate(templateFile)) {
			return createServiceResource(parent, templateFile);

		} else if (isPersistenceXml(templateFile)) {
			return createPersistenceXmlResource(parent, templateFile);

		} else if (isEntityTemplate(templateFile)) {
			return createEntityResources(parent, templateFile);

		} else if (isPackageTemplate(templateFile)) {
			return createPackageResource(parent, templateFile);

		}

		return super.createResource(parent, templateFile);

	}


	private List<WebXml> createWebXmlResource(Resource parent, File templateFile) {
		return asList(new WebXml(parent, templateFile));
	}


	private List<PackageResource> createPackageResource(Resource parent, File templateFile) {
		return asList(new PackageResource(parent, templateFile, getGlobalCtxt().getDefaultPackage()));
	}


	private List<PersistenceXml> createPersistenceXmlResource(Resource parent, File templateFile) {
		return asList(new PersistenceXml(parent, templateFile));
	}



	private List<EntityResource> createEntityResources(Resource parent, File templateFile) throws CreateException {
		List<EntityResource> newResources = new ArrayList<EntityResource>();
		List<ASTModule> modules = getAppModel().getModules();

		for (ASTModule module : modules) {
			ASTDomain domain = module.getDomain();

			if (isEmpty(getCtxt().getCurrentModule()))
				throw new NoModuleException();

			if (domain == null) {
				continue;
			}


			if (getCtxt().getCurrentModule().equals(module.getName())) {

				List<ASTEntity> entities = domain.getEntities();
				for (ASTEntity entity : entities) {
					checkEntityUnique(entity.getName());
					createEntityResource(newResources, entity, parent, templateFile);
				}
			}
		}

		return newResources;
	}


	private void checkEntityUnique(String name) throws DuplicateEntityException {
		List<ASTModule> modules = getAppModel().getModules();
		int count = 0;
		for (ASTModule module : modules) {
			count += module.countEntities(name);
		}

		if (count >= 2) {
			throw new DuplicateEntityException(name);
		}
	}


	private void createEntityResource(List<EntityResource> newResources, ASTEntity entity, Resource parent,
			File template) throws CreateException {
		String currentPackage = getCtxt().getCurrentPackage();
		if (isEmpty(currentPackage)) {
			currentPackage = generateParentPackage(template.getParentFile());
		}
		EntityResource res = new EntityResource(parent, template, currentPackage);


		res.setName(entity.getName());
		for (ASTProperty prop : entity.getFields()) {
			checkFieldUnique(entity, prop.getName());
			EntityField field = new EntityField(prop, res.getPackage());
			res.addProperty(field);
		}
		newResources.add(res);
	}


	private void checkFieldUnique(ASTEntity entity, String name) throws DuplicateFieldNameException {
		int count = 0;
		for (ASTProperty prop : entity.getFields()) {
			if (prop.getName().equals(name)) {
				count++;
			}
		}

		if (count >= 2) {
			throw new DuplicateFieldNameException(entity.getName(), name);
		}
	}


	private String generateParentPackage(File parent) throws CreateException {
		if (!getGlobalCtxt().isAutoDetectPackage()) {
			throw new InvalidPackageException("Entity must be in a parent  __package__ if auto detect package is false");
		}
		String firstName = parent.getName();
		File secondParent = parent.getParentFile();
		if (secondParent == null) {
			return firstName;
		}
		String secondName = secondParent.getName();
		File thirdParent = secondParent.getParentFile();
		if (thirdParent == null) {
			return secondName + "." + firstName;
		}
		String thirdName = thirdParent.getName();
		File forthParent = thirdParent.getParentFile();
		if (forthParent == null) {
			return thirdName + "." + secondName + "." + firstName;
		}
		String forthName = forthParent.getName();

		return forthName + "." + thirdName + "." + secondName + "." + firstName;
	}


	/**
	 * /Iterate through module's services and generate a service bean resource
	 * 
	 * @param parent
	 * @param templateFile
	 * @return
	 * @throws CreateException
	 */
	private List<ServiceResource> createServiceResource(Resource parent, File templateFile) throws CreateException {

		if (isEmpty(getCtxt().getCurrentModule()))
			throw new NoModuleException();

		// iterate through module's services and generate a service resource
		List<ServiceResource> serviceRes = newArrayList();
		String currentPackage = getCtxt().getCurrentPackage();
		String currentModule = getCtxt().getCurrentModule();


		if (isEmpty(currentModule))
			throw new NoModuleException();

		ASTLogic logic = getCurrentModule().getLogic();

		if (logic == null) {
			return new ArrayList<ServiceResource>();
		}

		List<ASTService> services = logic.getServices();

		for (ASTService modelService : services) {
			ServiceResource serviceResource = new ServiceResource(parent, templateFile, currentPackage);
			serviceResource.setName(modelService.getImage());
			serviceRes.add(serviceResource);
		}

		return serviceRes;
	}


	private ASTModule getCurrentModule() throws CreateException {
		String currentModule = getCtxt().getCurrentModule();
		List<ASTModule> modules = getAppModel().getModules();

		for (ASTModule module : modules) {
			if (currentModule.equals(module.getName())) {
				return module;
			}
		}
		throw new NoModuleException("Current module " + currentModule + " does not match any of the modules(" + modules
				+ ")");
	}


	private List<ModuleResource> createModuleResources(Resource parent, File templateFile) {
		List<ModuleResource> newResources = new ArrayList<ModuleResource>();

		for (ASTModule module : getAppModel().getModules()) {
			ModuleResource moduleResource = new ModuleResource(parent, templateFile);
			moduleResource.setModuleName(module.getName());
			newResources.add(moduleResource);
		}

		return newResources;
	}


	private boolean isModuleTemplate(File f) {
		String name = f.getName();
		return name.contains("__module__") && f.isDirectory();

	}


	private boolean isEntityTemplate(File f) {
		return "__entity__.java".equals(f.getName());
	}


	private boolean isPackageTemplate(File f) {
		return "__package__".equals(f.getName());
	}


	private boolean isServiceTemplate(File f) {
		boolean isServiceMeta = f.getName().contains("__service__");
		if (isServiceMeta) {
			return true;
		}

		return false;
	}


	private boolean isPersistenceXml(File f) {

		if (f.getParentFile() == null) {
			return false;
		}

		boolean correctName = "persistence.xml".equalsIgnoreCase(f.getName());
		boolean metaInfIsParent = "META-INF".equals(f.getParentFile().getName());

		return correctName && metaInfIsParent;
	}


	protected boolean isWebXml(File f) {

		if (f.getParentFile() == null)
			return false;

		boolean correctName = "web.xml".equalsIgnoreCase(f.getName());
		boolean webInfIsParent = "WEB-INF".equals(f.getParentFile().getName());

		return correctName && webInfIsParent;
	}



}
