package ro.swl.engine.generator.javaee.factory;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.ResourceFactory;
import ro.swl.engine.generator.javaee.exception.DuplicateEntityException;
import ro.swl.engine.generator.javaee.exception.DuplicateFieldNameException;
import ro.swl.engine.generator.javaee.exception.InvalidPackageException;
import ro.swl.engine.generator.javaee.exception.NoModuleException;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.generator.javaee.model.ModuleResource;
import ro.swl.engine.generator.javaee.model.PackageResource;
import ro.swl.engine.generator.javaee.model.PersistenceXml;
import ro.swl.engine.generator.javaee.model.ServiceBeanResource;
import ro.swl.engine.generator.javaee.model.ServiceResource;
import ro.swl.engine.generator.javaee.model.WebXml;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTDomain;
import ro.swl.engine.parser.ASTEntity;
import ro.swl.engine.parser.ASTModule;
import ro.swl.engine.parser.ASTProperty;
import ro.swl.engine.parser.ASTSwdlApp;


public class JavaEEResourceFactory extends ResourceFactory {


	public JavaEEResourceFactory(ASTSwdlApp appmodel, GenerationContext generationContext) {
		super(appmodel, generationContext);
	}


	@Override
	public List<? extends Resource> createResource(Resource parent, File templateFile) throws GenerateException {

		if (isWebXml(templateFile)) {
			return asList(new WebXml(parent, templateFile));

		} else if (isModuleTemplate(templateFile)) {
			return createModuleResources(parent, templateFile);

		} else if (isServiceBeanTemplate(templateFile)) {
			return createServiceBeanResources(parent, templateFile);

		} else if (isServiceTemplate(templateFile)) {
			return createServiceResource(parent, templateFile);

		} else if (isPersistenceXml(templateFile)) {
			return asList(new PersistenceXml(parent, templateFile));

		} else if (isEntityTemplate(templateFile)) {
			return createEntityResources(parent, templateFile);

		} else if (isPackageTemplate(templateFile)) {
			return asList(new PackageResource(parent, templateFile));

		}

		return super.createResource(parent, templateFile);

	}



	private List<EntityResource> createEntityResources(Resource parent, File templateFile) throws GenerateException {
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
			File template) throws GenerateException {
		String currentPackage = getCtxt().getCurrentPackage();
		if (isEmpty(currentPackage)) {
			currentPackage = generateParentPackage(parent);
		}
		EntityResource res = new EntityResource(parent, template, currentPackage);


		res.setName(entity.getName());
		for (ASTProperty prop : entity.getFields()) {
			checkFieldUnique(entity, prop.getName());
			EntityField field = new EntityField(prop, res.getPackage());
			res.addEntityProperty(field);
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


	private String generateParentPackage(Resource parent) throws GenerateException {
		if (!getCtxt().isAutoDetectPackage()) {
			throw new InvalidPackageException("Entity must be in a parent  __package__ if auto detect is false");
		}
		String first = parent.getTemplateFile().getName();
		Resource secondParent = parent.getParent();
		if (secondParent == null) {
			return first;
		}
		String second = secondParent.getTemplateFile().getName();
		Resource thirdParent = secondParent.getParent();
		if (thirdParent == null) {
			return second + "." + first;
		}
		String third = thirdParent.getTemplateFile().getName();
		Resource forthParent = thirdParent.getParent();
		if (forthParent == null) {
			return third + "." + second + "." + first;
		}
		String forth = forthParent.getTemplateFile().getName();

		return forth + "." + third + "." + second + "." + first;
	}


	private List<ServiceResource> createServiceResource(Resource parent, File templateFile) {
		// iterate through module's services and generate a service resource
		return asList(new ServiceResource(parent, templateFile));
	}


	private List<ServiceBeanResource> createServiceBeanResources(Resource parent, File templateFile) {
		// iterate through module's services and generate a service bean resource
		return asList(new ServiceBeanResource(parent, templateFile));
	}


	private List<ModuleResource> createModuleResources(Resource parent, File templateFile) {
		List<ModuleResource> newResources = new ArrayList<ModuleResource>();

		for (ASTModule module : getAppModel().getModules()) {
			ModuleResource moduleResource = new ModuleResource(parent, templateFile);
			moduleResource.setOutputFileName(module.getName());
			moduleResource.setModuleName(module.getName());
			newResources.add(moduleResource);
		}

		return newResources;
	}


	private boolean isModuleTemplate(File f) {
		String name = f.getName();
		return "__module__".equals(name) && f.isDirectory();

	}


	private boolean isEntityTemplate(File f) {
		return "__entity__.java".equals(f.getName());
	}


	private boolean isPackageTemplate(File f) {
		return "__package__".equals(f.getName());
	}



	private boolean isServiceBeanTemplate(File f) {
		boolean isServiceBeanMeta = "__serviceName__ServiceBean.java".equals(f.getName());
		if (isServiceBeanMeta && hasParent(f, "__module__")) {
			return true;
		}

		return false;
	}


	private boolean isServiceTemplate(File f) {
		boolean isServiceMeta = "__serviceName__Service.java".equals(f.getName());
		if (isServiceMeta && hasParent(f, "__module__")) {
			return true;
		}

		return false;
	}


	private boolean hasParent(File f, String parentName) {

		if (f == null) {
			return false;
		}

		if (f.getParentFile() == null) {
			return false;
		}


		if (parentName.equals(f.getParentFile().getName())) {
			return true;
		} else {
			return hasParent(f.getParentFile(), parentName);
		}
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