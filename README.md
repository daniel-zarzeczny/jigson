# Project JiGSON
JiGSON is a Java library meant to make [querying JSON](#examples) documents more powerful and programmer-friendly at the same time. JiGSON provides its own [expression language](#quick_start) to let you query JSON in a declarative manner. But JiGSON is not only about JSON querying, it's also a [post-processing engine](#examples). JiGSON supplies fluent and functional API capable of consuming query product immediately when it's available.

I would like to thank all contributors of [Jayway JsonPath](https://github.com/json-path/JsonPath) for their great work. JsonPath was (and still is) my inspiration to launch and work on JiGSON.

# Notice :warning:
> With all due respect to your time and work I would like to inform that **JiGSON is still at early development phase and might not be ready for production usage at this moment.** If you decide to give it a chance in your project(s) please be aware of the risks.

# What does the name 'JiGSON' come from?
- `J` because JiGSON is a Java library
- `GSON` because JiGSON strongly relies on [GSON](https://github.com/google/gson) as its abstraction of JSON document

# <a id="query_modes"></a> JiGSON Filtering Modes

When taking advantage of JiGSON you have available out of the box **3 filtering modes**:

| Name | Prefix | Query Example | Description |
| :------- | :----- | :----- | :----- |
| [Fetch Mode](#fetch_mode) | `@` | `@people(firstName=John).age`| **Fetches** `JsonElement` specified by path (respecting applied filters) |
| [Expression Mode](#expression_mode) | `$` | `$people(firstName=John).age.avg() < 20`| Resolves value of **logical expression** |
| [Keep Mode](#keep_mode) | `#` | `#people(firstName=John).age`| Filters JSON document **keeping** only `JsonElements` meeting specified criterion (if any applied). All other elements aren't included in output `JsonElement` |


# <a id="quick_start"></a> Quick Start

Let's assume we have a JSON document as follows:

```json
{
	"people": [{
			"firstName": "John",
			"lastName": "Snow",
			"age": "25",
			"address": {
				"city": "Castle Black"
			}
		},
		{
			"firstName": "Sansa",
			"lastName ": "Stark",
			"age": "20",
			"address": {
				"city ": "Winterfell"
			}
		}, {
			"firstName": "Brandon",
			"lastName ": "Stark",
			"age": "16",
			"address": {
				"city ": "Winterfell"
			}
		}
	]
}
```

## <a id="fetch_mode"></a> Fetch Mode - Query Examples

| JiGSON Query | Result description |
| :------- | :----- |
| `@people` | Gets all `people` |
| `@people.firstName` | Gets `firstName` of all `people` |
| `@people(firstName=John).age` | Gets `age` of `people` with `firstName=John`|
| `@people(firstName=John&&age<25).lastName` | Gets `lastName` of `people` with `firstName=John` and `age<25`|
| `@people(age>=20).address(city=Winterfell)` | Gets `address` of `people` with `age>=20John` and `city=Winterfell` |
| `@people[0].firstName` | Gets `firstName` of person under index `0` of `people` array |
| `@people[2](age=16).firstName` | Gets `firstName` of person under index `2` of `people` array if his/her `age=16` |
| `@people[::2].firstName` | Gets `firstName` of every second person of `people` array |
| `@people[1:3](age<=20).firstName` | Gets `firstName` of `people` between indices `1` and `3` (exclusive) of `people` array with `age<=20` |
| `@people[:3:].address.city` | Gets `city` of every person between indices `0` and `3` (exclusive) of `people` array |
| `@people.age.max()` | Gets `age` of the oldest person of `people` array |
| `@people[1::2].age.avg()` | Gets average `age` of every second person between indices `1` and `3` (exclusive) of `people` array |

## <a id="expression_mode"></a> Expression Mode - Query Examples

| JiGSON Query | Result |
| :------- | :----- |
| `$people.count() = 4` | `false` wrapped in `JsonPrimitive` |
| `$people(firstName=John&&age<100).age.avg() <= 50` | `true` wrapped in `JsonPrimitive`|
| <code>$people(firstName=John&#124;&#124;lastName=Stark).address(city=Winterfell).count() != 3</code> | `true` wrapped in `JsonPrimitive`|
| `$people[2](firstName=Brandon).address.city = Winterfell` | `true` wrapped in `JsonPrimitive`|

### Known limitations :warning:
* the left operand must **ALWAYS** be a JiGSON query
* the right operand must **ALWAYS** be a raw value

## <a id="keep_mode"></a> Keep Mode - Query Examples

Keep Mode is of JiGSON's filtering strategy. It's about keeping in result object only these elements that:

*	are a part of path JiGSON navigates through while interpreting a query, eg. `#people.firstName`
> :bulb:
When there is none person in `people` array or none person in `people` array has a non-null `firstName` then JiGSON returns `JsonNull`. If there are `people` having a non-null `firstName` then JiGSON returns root object (input object) containing only `people` with correct `firstName` attribute. So as the result of running above query against our JSON document we'd have received untouched input object:
```json
{
	"people": [{
			"firstName": "John",
			"lastName": "Snow",
			"age": "25",
			"address": {
				"city": "Castle Black"
			}
		},
		{
			"firstName": "Sansa",
			"lastName ": "Stark",
			"age": "20",
			"address": {
				"city ": "Winterfell"
			}
		}, {
			"firstName": "Brandon",
			"lastName ": "Stark",
			"age": "16",
			"address": {
				"city ": "Winterfell"
			}
		}
	]
}
```

* meet a criterion/criteria (if any given), eg. `#people(age>=20).address(city=Winterfell)`
> :bulb:
This example should be understood same way as the above. The only difference is that
we have some filters. The result there will be the same JSON document as the input one, except elements that don't meet criterion applied to them:
```json
{
	"people": [
		{
			"firstName": "Sansa",
			"lastName ": "Stark",
			"age": "20",
			"address": {
				"city ": "Winterfell"
			}
		}
	]
}
```

More query example:

| JiGSON Query | Result description |
| :------- | :----- |
| `#people` | Gets root `JsonObject` if attribute `people` exists |
| `#people.firstName` | Gets root `JsonObject` keeping only these `people` having any `firstName` |
| `#people(firstName=John).age` | Gets root `JsonObject` keeping only these `people` having `firstName=John` and any `age` |
| `#people(firstName=John&&age<25).lastName` |  Gets root `JsonObject` keeping only these `people` having `firstName=John` and `age<25`|
| `#people(age>=20).address(city=Winterfell)` |  Gets root `JsonObject` keeping `people` having `age>=20` and `city=Winterfell` |

### Known limitations :warning:
* refering to array element(s) using indices or slices is not supported so far

# <a id="examples"></a> Let's get hands dirty!

## Query for an Attribute  - `parse()`

```java
// 'firstNames' is JsonArray underneath
final JsonElement firstNames =
	Jigson.from(peopleObject).parse("@people(lastName=Stark||age<20).firstName");
```

## Query with Aggregate Function - `avg()`
```java
final Context context =
	Context.newContext()
		.filters().arrays().onlyMatching()
		.numbers().withPrecisionAnd(2)
		.withRoundingMode(BigDecimal.ROUND_HALF_UP);

// 'avg' is JsonPrimitive underneath contaning numeric value
final JsonElement avg =
	Jigson.from(peopleObject)
		.withContext(context)
		.parse("@people[::2](age>0).age.avg()");
```

## Filter Result Set - `filter()`

```java
final JsonElement addresses =
	Jigson.from(peopleObject)
		.parseThen("@people(age>10&&age<30).address")
		.filter("city=Winterfell")
		.get().orElse(JsonNull.INSTANCE);
```

## Test Result Set - `match()`

```java
final boolean match =
	Jigson.from(peopleObject)
		.parseThen("@people(age>10&&age<30).address")
		.match("city=Winterfell");
```

## Serialize Result to JSON - `json()`

```java
final String json =
	Jigson.from(peopleObject)
		.parseThen("@people")
		.filter("age>20")
		.json();
```

## Map Result Set - `map()`
```java
public void mapParseResult() {
	final JsonElement address =
		Jigson.from(peopleObject)
			.parseThen("@people")
			.map(this::pickPerson)
			.map(this::getAddress)
			.get()
			.orElse(JsonNull.INSTANCE);
}

public JsonElement pickPerson(final JsonElement people) {
	return people.getAsJsonArray().get(1).getAsJsonObject();
}

public JsonElement getAddress(final JsonElement person) {
	return person.getAsJsonObject().get("address");
}
```
## Mapping to Any Java Bean - `mapToObj()`
```java
class Person {
	String firstName;
	String lastName;
}
```
```java
public void mapParsingResultToPerson() {
	final Person person =
		Jigson.from(peopleObject)
			.parseThen("@people")
			.map(this::pickPerson)
			.mapToObj(this::mapToPerson)
			.get()
			.orElseGet(Person::new);
}
```
```java
public JsonObject pickPerson(final JsonElement people) {
	return people.getAsJsonArray().get(1).getAsJsonObject();
}
```
```java
public Person mapToPerson(final JsonElement jsonElement) {

	final JsonObject personObject = jsonElement.getAsJsonObject();

	final Person person = new Person();
	person.firstName = personObject.get("firstName").getAsString();
	person.lastName = personObject.get("lastName").getAsString();

	return person;
}
```

# <a id="aggregate_functions"></a>Aggregate functions
When running JiGSON aggregate functions it's important to understand their result types and behaviour which depends on provided type of `JsonElement`.

## Result type details

| Function | Description | Result type |
| :------- | :----- | :----- |
| `min()` | Resolves minimal value of `JsonElement` | `BigDecimal` wrapped in `JsonPrimitive`|
| `max()` | Resolves maximum value of `JsonElement` | `BigDecimal` wrapped in `JsonPrimitive`|
| `sum()` | Resolves sum of `JsonElement` | `BigDecimal` wrapped in `JsonPrimitive`|
| `avg()` | Resolves average value of `JsonElement` | `BigDecimal` wrapped in `JsonPrimitive`|
| `count()` | Resolves count of `JsonElement` | `Integer` wrapped in `JsonPrimitive`|
| `size()` | An alias for `count()` | `Integer` wrapped in `JsonPrimitive`|
| `length()` | Resolves length of `JsonElement` | `Integer` wrapped in `JsonPrimitive`|

## Behaviour details

> :exclamation: Be aware that executing aggregate functions on `JsonArray` that is not **homogeneous** may lead to processing errors. Use them carefully.

### Min()

| JsonElement | Result |
| :------- | :----- |
| `null` or `JsonNull` | throws `IllegalJsonElementException`|
| `JsonPrimitive` | Value of `JsonPrimitive` as `BigDecimal` or throws `NumberFormatException` when cannot cast to `BigDecimal` |
| `JsonObject` | throws `IllegalJsonElementException` |
| `JsonArray` | Minimal value of `JsonElement` array |

### Max()

| JsonElement | Result |
| :------- | :----- |
| `null` or `JsonNull` | throws `IllegalJsonElementException`|
| `JsonPrimitive` | Value of `JsonPrimitive` as `BigDecimal` or throws `NumberFormatException` when cannot cast to `BigDecimal` |
| `JsonObject` | throws `IllegalJsonElementException` |
| `JsonArray` | Maximum value of `JsonElement` array |

### Sum()

| JsonElement | Result |
| :------- | :----- |
| `null` or `JsonNull` | throws `IllegalJsonElementException`|
| `JsonPrimitive` | Value of `JsonPrimitive` as `BigDecimal` or throws `NumberFormatException` when cannot cast to `BigDecimal` |
| `JsonObject` | throws `IllegalJsonElementException` |
| `JsonArray` | Sum of all items |

### Avg()

| JsonElement | Result |
| :------- | :----- |
| `null` or `JsonNull` | throws `IllegalJsonElementException`|
| `JsonPrimitive` | Value of `JsonPrimitive` as `BigDecimal` or throws `NumberFormatException` when cannot cast to `BigDecimal` |
| `JsonObject` | throws `IllegalJsonElementException` |
| `JsonArray` | Average value of all items |

### Count()

| JsonElement | Result |
| :------- | :----- |
| `null` or `JsonNull` | Always `0`|
| `JsonPrimitive` | Always `1` |
| `JsonObject` | Always `1` |
| `JsonArray` | Size of array |

### Size()

It's an alias for `count()`.

### Length()

| JsonElement | Result |
| :------- | :----- |
| `null` or `JsonNull` | Always `0`|
| `JsonPrimitive` | Number of characters/digits |
| `JsonObject` | throws `IllegalJsonElementException` |
| `JsonArray` | Equivalent of `count()`; size of `JsonArray` |

# Operators

JiGSON varies **comparison and logical operators**. Comparison operators are used to build filter expression when there is no need for joining more than one condition, eg. `age>20`. Otherwise when more complex expression is a must, then logical operators should be used.

## Comparison operators

| Operator | Description | Example |
| :------- | :----- | :----- |
| `=` | left operand is equal to right | `firstName=John` |
| `!=` | left operand is not equal to right | `firstName!=John` |
| `>` | left operand is greater than right | `age>20` |
| `>=` | left operand is greater than or equal to right | `age>=20` |
| `<` | left operand is less than right | `age<20` |
| `<=` | left operand is less than or equal to right | `age<=20` |

### Known limitations :warning:
* the left operand **ALWAYS** must be the name of `JsonObject` property and the right operand must be a raw value (expressions and `JsonObject` properties are not supported)
* when the right operand is text it must not contain any white space character (all white spaces are removed from JiGSON query while parsing)
* when the right operand is recognised as numeric then it's always treated as a number - there is no any way to force JiGSON to see it as a text
* text values cannot be compared using: `>`, `>=`, `<`, `<=` operators

## Logical operators

| Operator | Description | Example |
| :------- | :----- | :----- |
| `&&` | AND | `firstName=John&&age>20` |
| <code>&#124;&#124;</code> | OR | <code>firstName=John&#124;&#124;age>20</code> |

### Known limitations :warning:
At most one logical operator might be used in a single filter expression. In other words you must use at most one `&&` or `||` in your expressions.

So these are NOT supported expression:
* `age>20||firstName=John||lastName=Snow`
* `firstName=John&&age>20||lastName=Stark||age!=16`

But these are fine:
* `firstName=John&&age>20`
* `firstName=John||age>20`

# Run with Context!

All JiGSON operations are executed with context (if none provided then default context is used). One might need to supply his/her own context to customise JiGSON behaviour. So far it's possible to modify following parameters:
* `JsonArray` filtering strategy
* `BigDecimal` precision
* `BigDecimal` rounding

## Default Context Configuration

By default JiGSON with following configuration:

| Parameter | Value |
| :------- | :----- |
| `JsonArray` filtering strategy | `ALL_IF_ANY_MATCH` |
| `BigDecimal` precision | `2` |
| `BigDecimal` roudning mode | `ROUND_HALF_UP` |

## Json Array Filtering Strategies

### Why do I need strategies?
JSON format assumes that `JsonArray` might not be **homogeneous** (it might have primitives and objects at the same time). Filtering strategies let us to decide what we want to happen when `JsonArray` is not homogeneous and which items we expect in the result set when filtering is done.

### Strategies Explained
As it comes to applying filters on `JsonArray` there are 3 strategies available:
1. `ALL_IF_ANY_MATCHING` (default)
> When at least one of `JsonArray` elements matches criterion,
  then the output is exactly the same `JsonArray` as one passed on the input. Otherwise `JsonNull` is returned.

2. `KEEP_MATCHING_AND_PRIMITIVES`
>	The result `JsonArray` contains only elements
	meeting filter criterion and all primitives regardless their values.
	If none `JsonObject` matches criterion and there is no other elements,
	then `JsonNull` is returned.

3. `ONLY_MATCHING`
>	The result contains only `JsonObject`s meeting filter criterion.
	All primitives (if any) are excluded from the result set.
	If none `JsonObject` matches criterion, then `JsonNull` is returned.

### Building Custom Context

```java
final Context context =
				Context.newContext()
								.filters().arrays().onlyMatching()
								.numbers().withPrecisionAnd(2).withRoundingMode(BigDecimal.ROUND_HALF_UP);
```
# Licence
JiGSON is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).
