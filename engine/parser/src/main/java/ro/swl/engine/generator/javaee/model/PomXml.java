package ro.swl.engine.generator.javaee.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.generator.model.Resource;


public class PomXml extends Resource {


	private List<Dependency> dependencies;
	private List<Dependency> dependencyManagement;
	private List<Repository> repositories;


	public PomXml(Resource parent, File template) {
		super(parent, template);
		this.dependencies = new ArrayList<PomXml.Dependency>();
		this.repositories = new ArrayList<PomXml.Repository>();
		this.dependencyManagement = new ArrayList<PomXml.Dependency>();
	}


	public void addDependency(String name, Dependency dep) {
		this.dependencies.add(dep);
	}


	public void addRepository(String string, Repository rep) {
		this.repositories.add(rep);
	}


	public void addDependencyManagement(List<Dependency> depMngmt) {
		dependencyManagement.addAll(depMngmt);
	}

	public static class Repository {

		private String id;
		private String name;
		private String url;


		public Repository(String id, String name, String url) {
			this.id = id;
			this.name = name;
			this.url = url;
		}


		public String getId() {
			return id;
		}


		public void setId(String id) {
			this.id = id;
		}


		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public String getUrl() {
			return url;
		}


		public void setUrl(String url) {
			this.url = url;
		}


	}

	public static class Dependency {

		private String groupId;
		private String artifactId;
		private String version;
		private String scope;

		private List<Exclusion> exclusions = new ArrayList<Exclusion>();

		public static class Exclusion {

			private String groupId;
			private String artifactId;


			public Exclusion(String groupId, String artifactId) {
				this.groupId = groupId;
				this.artifactId = artifactId;
			}


			public String getGroupId() {
				return groupId;
			}


			public void setGroupId(String groupId) {
				this.groupId = groupId;
			}


			public String getArtifactId() {
				return artifactId;
			}


			public void setArtifactId(String artifactId) {
				this.artifactId = artifactId;
			}


		}


		public Dependency(String groupId, String artifactId, String version) {
			this.groupId = groupId;
			this.artifactId = artifactId;
			this.version = version;
		}



		public String getGroupId() {
			return groupId;
		}



		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}



		public String getArtifactId() {
			return artifactId;
		}



		public void setArtifactId(String artifactId) {
			this.artifactId = artifactId;
		}



		public String getVersion() {
			return version;
		}



		public void setVersion(String version) {
			this.version = version;
		}



		public String getScope() {
			return scope;
		}



		public void setScope(String scope) {
			this.scope = scope;
		}


		public void addExclusion(String groupId, String artifactId) {
			exclusions.add(new Exclusion(groupId, artifactId));
		}

	}

}
