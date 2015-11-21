===
SWL - Simple Web Language
===




Project aims to simplify development process by defining a 'language' to describe the 3 layers of the applications. The entity model, the service and the UI. Multiple code generators and MDA projects have tried to address the 'repeatability' of coding JEE platforms, but they lacked in terms of flexibility. They just provide a skeleton They don't give the developer enough choice to build its own model or services with the frameworks they want. 

We want to take code generation one level further and describe the entity model, service and UI using a specific language - a DSL . This DSL together with a skeleton can be a powerful tool to 

* Describe the entity model ( use JPA to relational DB,  use whatever JPA implementation and DAO strategy, Hibernate, Toplink, OpenJPA, Spring data, your call)
* Describe the code for the services ( and just configure the underlying container - Spring, EJB,...)
* Describe the model that backs the UI ( if you want to support an additional model besides the persistence model to feed the UI )
* Describe the UI ( and configure whether you would use JAX-RS with rich front-end like Angular or Backbone, or simple Servlets or HTTP ) 


Generating for whichever model is possible if it is described via a 'language' and generated via a 'skeleton'. 
