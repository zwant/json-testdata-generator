Generation Options
------------------
Examples
--------
Examples can be created by adding a <example>[your-example-here]</example> tag in the description of the json schema.
An example can be used to get more deterministic behaviour of tests.
If GeneratorOptions.USE_EXAMPLES is set, any examples provided in the JSON schema will be used for that field when generating JSON. 

*Warning*
When `USE_EXAMPLES` is used, the generator disregards any other requirements set by the JSON schema (for example, you could provide an example string that does not fulfill the min and max length restrictions of the JSON schema).
That means that if your examples are incorrect, it will produce a JSON that does not validate properly against the schema.

If `GeneratorOptions.NO_EXAMPLES` is set, the JSON will be populated with a random value that fulfills the requirements set by the JSON Schema (for example min and max length and patterns).
With this option set, the JSON generator ensures that the produced JSON will validate properly against the schema.

Optionals
----------
You can control the behaviour of generating optional fields by setting GeneratorOptions.INCLUDE_OPTIONALS, GeneratorOptions.EXCLUDE_OPTIONALS or GeneratorOptions.RANDOM_OPTIONALS.
`INCLUDE_OPTIONALS` will make sure that _all_ non-required fields in the schema will always be generated. This is great for validating that your code can parse all optional fields.
`EXCLUDE_OPTIONALS` will make sure that _no_ non-required fields in the schema will ever be generated. This is great for validating that your code works fine without all optional fields.
`RANDOM_OPTIONALS` will make the generator randomly generate non-required fields in the schema. This can make unit tests in-deterministic, as it will trigger different code paths every time.
However, `RANDOM_OPTIONALS` is great when fuzzing your parsing code, as it will help you test different permutations of combining optional fields.
 

Supported Things
----------------

- Objects
    - required attributes (80% chance that non-required attributes are included)
    
- Strings - Defaults to alphanums
    - minLength
    - maxLength
    - pattern

- Integers
    - minimum
    - maximum
    - multipleOf (kind of, could generate overflowing numbers if no maximum is set)

- Numbers (doubles)
    - minimum
    - maximum
    - multipleOf (kind of, could generate overflowing numbers if no maximum is set)
   
- Arrays
    - minItems
    - maxItems
    - uniqueItems (could in rare cases sometimes generate a list that is less than maxItems long)
    - Single schema set for items

Known Not supported
-------------------
- References
- Arrays with multiple schemas
- "format" parameter to string schemas

Example Usage
-------------
JUnit rule
-----------
``` java
@Rule
public JsonGeneratorRule jsonGeneratorIncludeOptionals = new JsonGeneratorRule("example-schema.json",
GeneratorOptions.of(GeneratorOptions.OptionalsBehaviour.INCLUDE_OPTIONALS, 
                    GeneratorOptions.ExamplesBehaviour.USE_EXAMPLES));
```
or if you want the default behaviour for options (random optionals and examples enabled):
``` java
@Rule
public JsonGeneratorRule jsonGeneratorIncludeOptionals = new JsonGeneratorRule("example-schema.json");
``` 
`JsonGeneratorRule` has many variants on it's constructors, feel free to explore them.

When using the `JsonGeneratorRule` you also have access to the schema (as both a `org.everit.json.schema.Schema` object, and as a string) and the generated JSON (Both as a Jackson JsonNode and as a string):
``` java
@Rule
public JsonGeneratorRule jsonGenerator = new JsonGeneratorRule("example-schema.json");

@Test
public void myTest() {
    jsonGenerator.getSchema().validate(new JSONObject(jsonGenerator.getJsonString()));
    JsonNode jsonObject = jsonGenerator.getJsonNode();
    Assert.assertEquals(jsonGenerator.getJsonNode().get("some_string").asText(),
                        jsonObject.get("some_string").asText());
}
```
Beware tho, that if you are using the default options, or do not have GeneratorOptions.OptionalsBehaviour.INCLUDE_OPTIONALS set,
there is a risk that any optional field in the schema will not be included and will cause your test to appear flaky.

Manually using RootGenerator
----------------------------
``` java
URL schemaFile = Resources.getResource("example-schema.json");
String schemaData = Resources.toString(schemaFile, Charset.defaultCharset());
JSONObject schemaObject = new JSONObject(new JSONTokener(schemaData));
Schema schema = SchemaLoader.load(schemaObject);
RootGenerator.setOptions(GeneratorOptions.of(GeneratorOptions.ExamplesBehaviour.USE_EXAMPLES));
JsonNode generatedNode = RootGenerator.build(schema);
```
