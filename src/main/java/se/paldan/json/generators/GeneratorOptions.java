package se.paldan.json.generators;

public class GeneratorOptions {
    public enum OptionalsBehaviour {
        INCLUDE_OPTIONALS,
        EXCLUDE_OPTIONALS,
        RANDOM_OPTIONALS
    }

    public enum ExamplesBehaviour {
        USE_EXAMPLES,
        NO_EXAMPLES
    }

    private OptionalsBehaviour optionalsBehaviour = OptionalsBehaviour.RANDOM_OPTIONALS;
    private ExamplesBehaviour examplesBehaviour = ExamplesBehaviour.USE_EXAMPLES;

    private GeneratorOptions() {}

    private GeneratorOptions(OptionalsBehaviour optionals) {
        this.optionalsBehaviour = optionals;
    }
    private GeneratorOptions(ExamplesBehaviour examples) {
        this.examplesBehaviour = examples;
    }
    private GeneratorOptions(OptionalsBehaviour optionals, ExamplesBehaviour examples) {
        optionalsBehaviour = optionals;
        examplesBehaviour = examples;
    }

    public OptionalsBehaviour getOptionalsBehaviour() {
        return optionalsBehaviour;
    }
    public ExamplesBehaviour getExamplesBehaviour() {
        return examplesBehaviour;
    }

    public static GeneratorOptions of(OptionalsBehaviour optionals) {
        return new GeneratorOptions(optionals);
    }

    public static GeneratorOptions of(ExamplesBehaviour examples) {
        return new GeneratorOptions(examples);
    }

    public static GeneratorOptions of(OptionalsBehaviour optionals, ExamplesBehaviour examples) {
        return new GeneratorOptions(optionals, examples);
    }

    public static GeneratorOptions withDefaults() {
        return new GeneratorOptions();
    }
}
