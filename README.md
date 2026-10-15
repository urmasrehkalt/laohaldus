# Mini laohaldusrakendus

Java konsoolirakendus väikese poe laoseisu haldamiseks (TAK25 koolitöö).

## Nõuded

- Java 21
- Maven 3.9 või uuem

## Käivitamine

```
mvn -q compile
java -cp target/classes laohaldus.Main
```

Rakendus alustab nelja näidistootega. Menüüst saab tooteid vaadata, lisada, koguseid muuta,
nime järgi otsida ja laoseisu koguväärtust näha.

## Testid

```
mvn test
```

## Ülesehitus

| Klass | Roll |
|---|---|
| `Product` | abstraktne toode: nimi, hind, kogus (kapseldatud), abstraktne `getExtraInfo()` |
| `FoodProduct` | toit, lisaväli kõlblikkusaeg |
| `ElectronicProduct` | elektroonika, lisaväli garantii kuudes |
| `Inventory` | hoiab tooteid `LinkedHashMap<String, Product>` struktuuris, võti on nimi väikeste tähtedega |
| `ConsoleMenu` | konsoolimenüü ja kasutaja sisendi töötlus |
| `SampleData` | näidistooted |
| `Main` | paneb osad kokku ja käivitab menüü |
