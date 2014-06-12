play-sprig
==========

Sprig is a module for adding seed data to your Play applications.

Sprig is based on the Ruby Sprig project (see [http://vigetlabs.github.io/sprig/about.html](http://vigetlabs.github.io/sprig/about.html))


##Usage
In your application's startup code, such as Global.java, use Sprig to load data for your models:
 
    Sprig.load(User.class, Address.class);

Sprig will look for matching JSON files in conf/sprig

       User.json
       Address.json
    
!If you use different data for development, test, and production, pass a directory name:

    Sprig.load("prod", User.class, Address.class);

Sprig will load files from conf/sprig/prod

### JSON files
#####User.json
    {
    "records": [
         {"firstName":"Sam", "lastName":"Baker", "age":35},
         {"firstName":"Denise", "lastName":"Carlson", "age":33},
         ]
    }
      
### Relational Data
If one model has a reference to another model, use a *sprig_id*.  Sprig will use these during loading to resolve references.

#####User.json

    .... "address":"<% sprig_record(Address,2)%>"

#####Address.json

    ....
    {"sprig_id":2,....

*sprig_id*s can be integers or strings.  They must be unique within each model.

#### Ordering
Sprig manages dependencies between models automatically.  In our example models, User has an Address property. Sprig will load Address before User, so that Address's database-genereated *id* is available when user.address is set.

Sprig will raise an exception if a circular dependency is detected.

### Loaders
Sprig's default loader will load data into models with getters and setters.  It uses Spring's BeanWrapperImpl.

If you have public fields, define a custom loader and override the *parse* method. 

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

Use *setLoader* to install a custom loader. 

    Sprig.load(MyCustomUserLoader(), Address.class, ...);

### Idempotent Loading
The default loader inserts each parsed model object into the database. This can lead to duplicate records if you restart an application without clearing the database.

An idempotent loader will only insert records if they do not already exist.     

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

### Database execution