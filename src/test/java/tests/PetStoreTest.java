package tests;

import animals.petstore.pet.attributes.PetType;
import org.example.Main;
import org.junit.jupiter.api.Test;
import animals.AnimalType;
import animals.petstore.pet.Pet;
import animals.petstore.pet.attributes.Breed;
import animals.petstore.pet.attributes.Gender;
import animals.petstore.pet.attributes.Skin;
import animals.petstore.pet.types.Cat;
import animals.petstore.pet.types.Dog;
import animals.petstore.pet.types.Snake;
import animals.petstore.store.DuplicatePetStoreRecordException;
import animals.petstore.store.PetNotFoundSaleException;
import animals.petstore.store.PetStore;
import number.Numbers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class PetStoreTest
{
    private static PetStore petStore;

    @BeforeEach
    public void loadThePetStoreInventory()
    {
        petStore = new PetStore();
        petStore.init();
    }
    private Pet buildPetWithId(int id) {
        return new Pet(PetType.DOG, new BigDecimal("25.50"), Gender.MALE, id) {
            @Override public String getName() { return "TestPet"; }
            @Override public int getAge() { return 3; }
            @Override public String speak() { return "bark"; }
        };
    }

    @Test
    @DisplayName("Pet constructors, getters and setter")
    void petConstructorsGettersSetter() {
        // 3‑arg constructor (id defaults to 0)
        Pet p1 = new Pet(PetType.CAT, new BigDecimal("10.00"), Gender.FEMALE) {
            @Override public String getName() { return "Cat"; }
            @Override public int getAge() { return 2; }
            @Override public String speak() { return "meow"; }
        };

        assertEquals(PetType.CAT, p1.getPetType());
        assertEquals(new BigDecimal("10.00"), p1.getCost());
        assertEquals(Gender.FEMALE, p1.getGender());
        assertEquals(0, p1.getPetStoreId());

        // 4‑arg constructor + setPetStoreId
        Pet p2 = buildPetWithId(5);
        assertEquals(5, p2.getPetStoreId());
        p2.setPetStoreId(7);
        assertEquals(7, p2.getPetStoreId());
    }



    @Test
    @DisplayName("Inventory Count Test")
    public void validateInventory()
    {
        // 5 original pets + 2 snakes added in PetStore.init()
        assertEquals(7, petStore.getPetsForSale().size(),"Inventory counts are off!");
    }

    @Test
    @DisplayName("Print Inventory Test")
    public void printInventoryTest()
    {
        petStore.printInventory();
    }

    @Test
    @DisplayName("Sale of Poodle Remove Item Test")
    public void poodleSoldTest() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int inventorySize = petStore.getPetsForSale().size() - 1;
        Dog poodle = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1);

        // Validation
        petStore.soldPetItem(poodle);
        assertEquals(inventorySize, petStore.getPetsForSale().size(), "Expected inventory does not match actual");
    }

    @Test
    @DisplayName("Poodle Duplicate Record Exception Test")
    public void poodleDupRecordExceptionTest() {
        petStore.addPetInventoryItem(new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1));
        Dog poodle = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1);

        // Validation
        String expectedMessage = "Duplicate Dog record store id [1]";
        Exception exception = assertThrows(DuplicatePetStoreRecordException.class, () ->{
            petStore.soldPetItem(poodle);});
        assertEquals(expectedMessage, exception.getMessage(), "DuplicateRecordExceptionTest was NOT encountered!");

    }

    @Test
    @DisplayName("Sale of Sphynx Remove Item Test")
    public void sphynxSoldTest() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int inventorySize = petStore.getPetsForSale().size() - 1;

        Cat sphynx = new Cat(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SPHYNX,
                new BigDecimal("100.00"), 2) {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public int getAge() {
                return 0;
            }
        };
        Cat removedItem = (Cat) petStore.soldPetItem(sphynx);

        // Validation
        assertEquals(inventorySize, petStore.getPetsForSale().size(), "Expected inventory does not match actual");
        assertEquals(sphynx.getPetStoreId(), removedItem.getPetStoreId(), "The cat items are identical");
    }

    @Test
    void dogAllMethodsSmokeTest() {
        Dog dog = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE);

        // getters / setters
        assertEquals(4, dog.getNumberOfLegs());
        dog.setNumberOfLegs(5);
        assertEquals(5, dog.getNumberOfLegs());
        assertEquals(Breed.POODLE, dog.getBreed());
        assertEquals(AnimalType.DOMESTIC, dog.getAnimalType());

        // behavior
        assertFalse(dog.dogHypoallergenic().isEmpty());
        assertFalse(dog.speak().isEmpty());
        assertFalse(dog.toString().isEmpty());
    }

    @Test
    void catAllMethodsSmokeTest() {
        Cat cat = new Cat(AnimalType.DOMESTIC, Skin.FUR, Gender.FEMALE, Breed.BURMESE) {
            @Override public String getName() { return "Kitty"; }
            @Override public int getAge() { return 2; }
        };

        assertEquals(4, cat.getNumberOfLegs());
        cat.setNumberOfLegs(3);
        assertEquals(3, cat.getNumberOfLegs());
        assertEquals(Breed.BURMESE, cat.getBreed());
        assertEquals(AnimalType.DOMESTIC, cat.getAnimalType());
        assertTrue(cat.typeOfPet().contains("CAT"));

        assertFalse(cat.catHypoallergenic().isEmpty());
        assertFalse(cat.speak().isEmpty());
        assertFalse(cat.toString().isEmpty());
    }

    @Test
    void snakeAllMethodsSmokeTest() {
        Snake snake = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.UNKNOWN);

        assertEquals(0, snake.getNumberOfLegs());
        snake.setNumberOfLegs(1);
        assertEquals(1, snake.getNumberOfLegs());
        assertEquals(Breed.UNKNOWN, snake.getBreed());
        assertEquals(AnimalType.DOMESTIC, snake.getAnimalType());

        assertFalse(snake.snakeHypoallergenic().isEmpty());
        assertFalse(snake.speak().isEmpty());
        assertFalse(snake.toString().isEmpty());
    }




    /**
     * Limitations to test factory as it does not instantiate before all
     * @return list of {@link DynamicNode} that contains the test results
     * @throws DuplicatePetStoreRecordException if duplicate pet record is found
     * @throws PetNotFoundSaleException if pet is not found
     */
    @TestFactory
    @DisplayName("Sale of Sphynx Remove Item Test2")
    public Stream<DynamicNode> sphynxSoldTest2() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int inventorySize = petStore.getPetsForSale().size() - 1;

        Cat sphynx = new Cat(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SPHYNX,
                new BigDecimal("100.00"), 2) {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public int getAge() {
                return 0;
            }
        };
        Cat removedItem = (Cat) petStore.soldPetItem(sphynx);

        // Validation
        List<DynamicNode> nodes = new ArrayList<>();
        List<DynamicTest> dynamicTests = Arrays.asList(
                dynamicTest("Inventory Check Size Test ", () -> assertEquals(inventorySize,
                        petStore.getPetsForSale().size())),
                dynamicTest("The cat objects match ", () -> assertEquals(sphynx.toString(),
                        removedItem.toString()))
        );
        nodes.add(dynamicContainer("Cat Item 2 Test", dynamicTests));//dynamicNode("", dynamicContainers);

        return nodes.stream();
    }


    @Test
    @DisplayName("Sale of Snake Remove Item Test")
    public void snakeSoldTest() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int inventorySize = petStore.getPetsForSale().size() - 1;

        // One of the snakes defined in PetStore.init(), ids 4 or 5
        Snake snake = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.UNKNOWN,
                new BigDecimal("120.00"), 4);

        Snake removedItem = (Snake) petStore.soldPetItem(snake);

        assertEquals(inventorySize, petStore.getPetsForSale().size(), "Expected inventory does not match actual");
        assertEquals(snake.getPetStoreId(), removedItem.getPetStoreId(), "The snake items are identical");
    }


    @Test
    @DisplayName("Snake Duplicate Record Exception Test")
    public void snakeDupRecordExceptionTest() {
        // Duplicate an existing snake record (id 4)
        petStore.addPetInventoryItem(new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.UNKNOWN,
                new BigDecimal("120.00"), 4));
        Snake snake = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.UNKNOWN,
                new BigDecimal("120.00"), 4);

        String expectedMessage = "Duplicate Snake record store id [4]";
        Exception exception = assertThrows(DuplicatePetStoreRecordException.class, () -> {
            petStore.soldPetItem(snake);
        });
        assertEquals(expectedMessage, exception.getMessage(), "Duplicate Snake exception was NOT encountered!");
    }


    @Test
    @DisplayName("Pet Not Found Sale Exception Test (id 0)")
    public void petNotFoundSaleExceptionTest() {
        Dog strayDog = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.MALTESE,
                new BigDecimal("500.00"), 0);

        assertThrows(PetNotFoundSaleException.class, () -> petStore.soldPetItem(strayDog),
                "Expected PetNotFoundSaleException for petStoreId 0");
    }

    @Test
    @DisplayName("Unsupported Pet Type Test")
    public void unsupportedPetTypeTest() {
        // Anonymous Pet implementation that is not Dog, Cat, or Snake
        Pet unknownPet = new Pet(null, BigDecimal.ZERO, Gender.MALE, 99) {
            @Override
            public String getName() {
                return "Unknown";
            }

            @Override
            public int getAge() {
                return 1;
            }

            @Override
            public String speak() {
                return "";
            }
        };

        assertThrows(PetNotFoundSaleException.class, () -> petStore.soldPetItem(unknownPet),
                "Expected PetNotFoundSaleException for unsupported pet type");
    }

    @Test
    void runMain() {
        Main.main(new String[0]);
    }

    @Test
    @DisplayName("initAddDuplicateItem adds one extra pet")
    void initAddDuplicateItemTest() {
        int originalSize = petStore.getPetsForSale().size();

        Dog extraDog = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.MALTESE,
                new BigDecimal("500.00"), 99);

        PetStore ps2 = new PetStore();
        ps2.initAddDuplicateItem(extraDog);

        assertEquals(originalSize + 1, ps2.getPetsForSale().size());
        assertTrue(ps2.getPetsForSale().contains(extraDog));
    }

    /**
     * Example of parameterized test
     * @param number to be tested
     */
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6, -10, 128, Integer.MIN_VALUE}) // six numbers
    void isNumberEven(int number)
    {
        assertTrue(Numbers.isEven(number));
    }

}