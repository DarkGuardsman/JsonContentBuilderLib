## JsonContentBuilderLib
Collection of useful tools and functions for turning JSON data into functional objects. The idea of this lib is not to replace existing libs. After all it uses GSON which contains some of this functionality already. The purpose is to create a simple system for locationing, loading, and processing JSON so that it can be turned into objects. Then linking and registering those objects to there respective systems.


##Plan
Anything after this is a plan of how the system will be implemented. Once completed this will be reviewed and changed to match the actual description of the implemented systems.

### Core Features
Following features are suggested or possible features. Most will need to be added or completed before marked as contained.

* File Locatators -> logic that runs to find JSON files in the file system, in compressed files, remotely, in datebases, or other methods
* PreProcessors -> logic that runs to cleanup JSON files and break them down into objects ready for loading
* DataMappers -> logic that runs to take the JSON data and turn it into objects. Including the ability to make several objects and process sub-objects as needed. On top of allowing complex logic to be run such as for-loops, foreach-loops, conditionals, and while loops to generate data as needed.
* Dependency Injection -> ability to link objects to other generate objects using data provided without storing keys to that data on the objects themselves.
* Object Generation -> Converstion of data to objects
* Listeners -> Ability to listen for data creation and events to perform additional logic
* Exclusion/Filters -> Ability to block usage of data or files as needed
* Data Dependency -> Ability to define that data is depend on other data sets
* Data inhertance -> Ability to define that data sets extend other data sets. Allows for prefabs, templates, etc

### Process

#### Init
First step is to init the system. This will involve creating the main handler, registering handlers, data mappers, file loaders, etc. Once done the system can then be triggered to run.

#### Phase(1): File Load
When the system runs the first thing it will do is locate all files. It will then load these in and pass them onto the pre-processors. The first processor will convert the JSON raw data to JSON objects via GSON. The next processor will strip out any comments and clean up the file. It will then run an filters or exclusions on the data. This will ensure the data used is clean and usable. The final processor will run basic validation before passing to any user define processors.

#### Phase(2): Mapping
The result of the file load phase will be JsonData objects. These will define the loaded data, data over-all type, file it was pulled from, etc. The next step is to take this data and map it to something useful. This is were the data mappers come into the process. 

The first step is to map all the objects to handlers, Map<String, JsonData> with string being the key to the data mapper. Once this is done everything will be sorted to ensure loading order is maintained. Then objects will be passed into the data mappers generating objects. Each mapper will have the option to include nested mappers to handle data inside of the object. This is useful for dealing with common data types and sub-objects. 

The second step is to take the generate objects and map them to handlers. These handlers will deal with any regristration and wiring of the data.

#### Phase(3): Dependency Mapping
Once all objects are generate the next step is to wire everything together. This step will be repeated a few times depending on what is needed. However, will come down to simple parsing of objects to check for annotations then injecting references based on data provided. This data will depend on the layout of the object. With the option to either select data from the object or from the JSON data still referenced.

#### Phase(4): Regristry
With mapping done the next step is to register all the objects to their systems. This will depend on the implementation of the program. As well could involve many different calls, events, or phases to complete. 

#### Phase(5): Cleanup
Once everything is registered then cleanup is started. This first involves validating all objects, running last dependency mapping checks, and anything else on the data itself. However, once this is completed then all data not used is dumped. 

Data dumping involves wiping out the load JSON data and JSONData objects. The only thing that will be kept is location of files to generat objects. As well a little bit of data for error handling. Though even these are up to the implementating system.

#### Data Format
Format is up to the implementing system. However, the loader is designed to make the process as easy as possibe. This is done by allow JSON files to contain more than 1 data entry. The following is a recommend format for multi-entries.

##### Multi-Entry Format
{
[
{
	"type": "uniqueTypeID",
	"data" :
	{
	}
},
{
	//second object
},
{
	//third object
}

]

}
##### Single Entry
{
	"type": "uniqueTypeID",
	"data" :
	{
	}
}

##### Legacy
{
	"uniqueTypeID": {
	},
	"uniqueTypeID:1": {
	},
	"uniqueTypeID:n": {
	}
}

#### Classes

##### API
###### IJsonDataComponent
-getUniqueID() : String - unique id for the component
-getOwnerID(): String - name of system or plugin that registered the component

//Will be used to ID this component and user uniqueID doesn't overlap in registries
default getComponentID()
{
	return getOwnerID() + ":" + getUniqueID();
}

###### IFileLoader extends IJsonDataComponent
-loadFiles(PreProcessorPipeLine, List JsonDataObjects, ExclusionFilter, AccessList)

###### IPreProcessor extends IJsonDataComponent
-handle(JsonDataObject)
-getLoadOrder: String - "after:processorID;before:processorID2"

###### IDataMapper extends IJsonDataComponent
-handle(JsonObject) : IDataObject
-getContentTypeID() : String

###### IDataHandler extends IJsonDataComponent
-getContentTypeID() : String
-addObject(IDataObject)

###### IDataObject extends IJsonDataComponent
-getContentTypeID() : String
-isValid():boolean


##### Core
###### JsonContentLoader
-preProcessorPipeLine : PreProcessorPipeLine
-fileLoader : FileLoader
-dataMappers : Map<String, IDataMapper> -> key to mapper
-dataHandlers: Map<String, IDataHandler> -> key to handler
-jsonDataObjects : Map<String, List<JsonDataObject> -> type to data

-registerPreProcessor(IPreProcessor)
-registerDataMapper(IDataMapper)
-registerDataHandler(IDataHandler)

-startLoading() -> triggers the process

###### ExclusionFilter
-excludedFiles : List
-ecludedFolders : List
-excludedExtentions : List
-filters : List<Function> - expression based explusion, ex: */fileName/

-excludeFolder(filePath)
-excludeFolder(filePaths...)
-excludeFile(filePath)
-excludeFiles(filePaths....)
-addExclusionRule(Function) -> adds to filters

###### FileLoader
-exclusionFilter : ExclusionFilter
-accessList : AccessList

-loadFiles(PreProcessorPipeLine, List JsonDataObjects) -> foreach of file loader passing in data needed and passing back through data in JsonDataObjects list

###### PreProcessorPipeLine
-preProcessors : List<IPreProcessor> - sorted list of processors
-currentProcessor : IPreProcessor

###### AccessList
Object to track what files have been access already by the file loaders

