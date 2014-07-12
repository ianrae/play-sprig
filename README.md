play-sprig
==========

Sprig is a module for adding seed data to your Play 2.2 applications.

Sprig is based on the Ruby Sprig project (see [http://vigetlabs.github.io/sprig/about.html](http://vigetlabs.github.io/sprig/about.html))

##Installing
In build.sbt add play-sprig to dependencies:

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "play-sprig" % "play-sprig_2.10" % "0.1-SNAPSHOT"	//** add this line**
)     

And add a resolver:

resolvers ++= Seq(
Resolver.url("play-sprig", new URL("http://ianrae.github.io/snapshot/"))(Resolver.ivyStylePatterns)
//)

##Usage
In your application's startup code, such as Global.java, use Sprig to load data for your models:
 
    Sprig.load(User.class, Address.class);

Sprig looks for matching JSON files in conf/sprig

       User.json
       Address.json
    
If you use different data for development, test, and production, pass a directory name:

    Sprig.load("prod", User.class, Address.class);

Sprig loads files from conf/sprig/prod

### JSON files
#####User.json
    {
    "records": [
         {"firstName":"Sam", "lastName":"Baker", "age":35},
         {"firstName":"Denise", "lastName":"Carlson", "age":33},
         ]
    }
      
### Relational Data
When a model references another model, use a *sprig_id*.  Sprig uses these during loading to resolve references.
These are synthetic ids that only exist during the loading; they are not persisted to the database.

#####User.json

    .... "address":"<% sprig_record(Address,2)%>"

#####Address.json

    ....
    {"sprig_id":2,....

*sprig_id*s can be integers or strings.  They must be unique within each model. 

#### Ordering
Sprig manages dependencies between models automatically.  In our example, the User model references Address. Sprig loads Address before User, so that Address's database-genereated *id* is available when user.address is set.

Sprig raises an exception if a circular dependency is detected.

### Loaders
Sprig's default loader loads data into models with getters and setters.  It uses Spring's BeanWrapperImpl.

If your model has publics fields instead of getters and setters, then create a custom loader.
Override the *parse* method like this:

    @Override
    public void parse(User obj, Map<String,Object> map)
    {
        if (map.containsKey("firstName"))
        {
            obj.firstName = (String)map.get("firstName");
        }

        ...




### Custom Loaders
The arguments to Sprig.load must either be a class, such as User.class, or a custom loader that implements SprigLoader.

    Sprig.load(MyCustomUserLoader(), Address.class, ...);

### Idempotent Loading
The default loader inserts each parsed model object into the database. This can lead to duplicate records if you restart an application without clearing the database.

An idempotent loader only insert records if they do not already exist.     

Create a custom loader and override the *save* method.

    @Override
    public void save(User model) 
    {
        User existing = User.find_by_name(model.getName());
        if (existing == null)
        {
            model.save();
        }
    }


### EBean
Sprig's default loader uses Model.save. If your model is not derived from Model then you must create
a custom loader and implement save.

	public void save(Object obj) 
	{
		if (obj instanceof Model)
		{
			Model m = (Model)obj;
			m.save();
		}
		else
		{
			log("Error: not an play.Model object. you must use a custom loader.");
		}
	}


### Database execution
Sprig is generally used in Global.java.  For JPA persistence you need to use JPA.withTransaction or other similar mechanism.

SprigLoader has a close method that can be used to close any database connections. The close method is called after all loading has completed.
