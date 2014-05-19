package ro.swl.engine.generator.javaee.model;

import ro.swl.engine.generator.model.FileResource;
import ro.swl.engine.generator.model.Resource;

import java.io.File;
import java.util.*;

import static org.apache.commons.lang.StringUtils.isNotEmpty;


public class PomXml extends FileResource {

    public static String ID = "pom.xml";

    private Map<String, Dependencies> dependencies = new HashMap<String, Dependencies>();
    private Map<String, Dependencies> dependenciesManagement = new HashMap<String, Dependencies>();

    public PomXml(Resource parent) {
        super(parent, ID, false);
    }

    public PomXml(Resource parent, File template) {
        super(parent, template);
    }


    public void addDependency(String name, Dependency... dep) {
        Dependencies deps = dependencies.get(name);
        if (deps == null) {
            deps = new Dependencies();
        }
        deps.addAll(Arrays.asList(dep));
        dependencies.put(name, deps);
    }

    public void addDependencyManagement(String name, Dependencies depMngmt) {
        dependenciesManagement.put(name, depMngmt);
    }

    public Dependencies getDependencies(String name) {
        return dependencies.get(name);
    }

    public Dependencies getDependenciesManagement(String name) {
        return dependenciesManagement.get(name);
    }


    public static class Dependencies extends HashSet<Dependency> {
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (Dependency dep : this) {
                builder.append(dep.toXmlRepresentation());
            }
            return builder.toString();
        }
    }

    public static class Dependency {

        private String groupId;
        private String artifactId;
        private String version;
        private String scope;

        private List<Exclusion> exclusions = new ArrayList<Exclusion>();

        public Dependency(String groupId, String artifactId, String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }


        public void addExclusion(String groupId, String artifactId) {
            exclusions.add(new Exclusion(groupId, artifactId));
        }

        public String toXmlRepresentation() {

            StringBuilder builder = new StringBuilder();
            builder.append("<dependency>\n");
            builder.append("\t\t\t<groupId>");
            builder.append(groupId);
            builder.append("</groupId>\n");
            builder.append("\t\t\t<artifactId>");
            builder.append(artifactId);
            builder.append("</artifactId>\n");
            if (isNotEmpty(version)) {
                builder.append("\t\t\t<version>");
                builder.append(version);
                builder.append("</version>\n");
            }
            if (isNotEmpty(scope)) {
                builder.append("\t\t\t<scope>");
                builder.append(scope);
                builder.append("</scope>\n");
            }
            if (!exclusions.isEmpty()) {
                builder.append("\t\t\t<exclusions>\n");
                for (Exclusion exc : exclusions) {
                    builder.append("\t\t\t\t<exclusion>\n");
                    builder.append("\t\t\t\t\t<groupId>");
                    builder.append(exc.getGroupId());
                    builder.append("</groupId>\n");
                    builder.append("\t\t\t\t\t<artifactId>");
                    builder.append(exc.getArtifactId());
                    builder.append("</artifactId>\n");
                    builder.append("\t\t\t\t</exclusion>\n");
                }
                builder.append("\t\t\t</exclusions>\n");
            }
            builder.append("\t\t</dependency>\n");
            return builder.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Dependency that = (Dependency) o;

            if (!artifactId.equals(that.artifactId)) return false;
            if (!groupId.equals(that.groupId)) return false;
            if (!version.equals(that.version)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = groupId.hashCode();
            result = 31 * result + artifactId.hashCode();
            result = 31 * result + version.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return toXmlRepresentation();
        }

        //<editor-fold desc="Getters and setters" />
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

        //</editor-fold>

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
    }

}
