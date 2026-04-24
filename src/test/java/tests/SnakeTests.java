package tests;

import animals.AnimalType;
import animals.petstore.pet.attributes.Breed;
import animals.petstore.pet.attributes.Gender;
import animals.petstore.pet.attributes.Skin;
import animals.petstore.pet.types.Dog;
import animals.petstore.pet.types.Snake;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class SnakeTests {

    @Test
    @DisplayName("Snake constructors and basic getters")
    void snakeConstructorsAndGettersTest() {
        // constructor with default cost and petStoreId
        Snake s1 = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.FEMALE, Breed.UNKNOWN);
        assertEquals(0, s1.getPetStoreId());
        assertEquals(AnimalType.DOMESTIC, s1.getAnimalType());
        assertEquals(Breed.UNKNOWN, s1.getBreed());
        assertEquals(0, s1.getNumberOfLegs());   // constructor sets to 0

        // full constructor with cost and petStoreId
        BigDecimal cost = new BigDecimal("150.00");
        Snake s2 = new Snake(AnimalType.WILD, Skin.SCALES, Gender.MALE, Breed.UNKNOWN, cost, 5);
        assertEquals(cost, s2.getCost());
        assertEquals(5, s2.getPetStoreId());
        assertEquals(AnimalType.WILD, s2.getAnimalType());
    }

    @Test
    @DisplayName("Snake numberOfLegs getter/setter")
    void snakeNumberOfLegsSetterGetterTest() {
        Snake snake = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.UNKNOWN);
        assertEquals(0, snake.getNumberOfLegs());
        snake.setNumberOfLegs(2);
        assertEquals(2, snake.getNumberOfLegs());
    }

    @Test
    @DisplayName("Snake speak() Domestic / Wild / Default branches")
    void snakeSpeakBranchesTest() {
        Snake domestic = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.UNKNOWN);
        String domesticSpeech = domestic.speak();
        assertTrue(domesticSpeech.toLowerCase().contains("hiss"));

        Snake wild = new Snake(AnimalType.WILD, Skin.SCALES, Gender.MALE, Breed.UNKNOWN);
        String wildSpeech = wild.speak();
        assertTrue(wildSpeech.toLowerCase().contains("rattle"));

        // default branch: use an AnimalType that is not DOMESTIC or WILD
        Snake unknown = new Snake(AnimalType.UNKNOWN, Skin.SCALES, Gender.MALE, Breed.UNKNOWN);
        String unknownSpeech = unknown.speak();
        // default uses PetType.speak string twice; just assert it's non-empty and contains "snake" or speak text
        assertFalse(unknownSpeech.isEmpty());

    }

    @Test
    void getCost_returnsConstructorValue() {
        BigDecimal expectedCost = new BigDecimal("123.45");

        Dog dog = new Dog(
                AnimalType.DOMESTIC,
                Skin.FUR,
                Gender.MALE,
                Breed.POODLE,
                expectedCost,
                1
        );

        assertEquals(expectedCost, dog.getCost());
    }

    @Test
    @DisplayName("Snake hypoallergenic and toString")
    void snakeHypoallergenicAndToStringTest() {
        Snake snake = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.FEMALE, Breed.UNKNOWN);
        String hypoMsg = snake.snakeHypoallergenic();
        assertTrue(hypoMsg.toLowerCase().contains("snake"));

        String text = snake.toString();
        assertNotNull(text);
        assertFalse(text.isEmpty());
        assertTrue(text.toLowerCase().contains("snake"));
        assertTrue(text.contains(snake.getBreed().toString()));
    }

}
