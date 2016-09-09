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
