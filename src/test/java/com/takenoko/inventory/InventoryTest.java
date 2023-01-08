package com.takenoko.inventory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class InventoryTest {
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    @Test
    @DisplayName("should return the bamboo stack")
    void shouldReturnTheBambooStack() {
        InventoryBambooStack bambooStack = new InventoryBambooStack(5);
        Inventory inventory = new Inventory(bambooStack);
        assertThat(inventory.getBambooStack()).isEqualTo(bambooStack);
    }

    @Nested
    @DisplayName("Method equals")
    class TestEquals {
        @Test
        @DisplayName("should return true when the two objects are the same")
        void equals_shouldReturnTrueWhenSameObject() {
            assertThat(inventory).isEqualTo(inventory);
        }

        @Test
        @DisplayName("should return true when the two objects are equal")
        void equals_shouldReturnTrueWhenEqual() {
            Inventory other = new Inventory();
            assertThat(inventory).isEqualTo(other);
        }

        @Test
        @DisplayName("should return false when the two objects are not equal")
        void equals_shouldReturnFalseWhenNotEqual() {
            Inventory other = new Inventory();
            other.getBambooStack().collectBamboo();
            assertThat(inventory).isNotEqualTo(other);
        }

        @Test
        @DisplayName("should return false when the other object is null")
        void equals_shouldReturnFalseWhenOtherIsNull() {
            assertThat(inventory).isNotEqualTo(null);
        }

        @Test
        @DisplayName("should return false when the other object is not a Inventory")
        void equals_shouldReturnFalseWhenOtherIsNotInventory() {
            assertThat(inventory).isNotEqualTo(new Object());
        }
    }

    @Nested
    @DisplayName("Method hashCode")
    class TestHashCode {
        @Test
        @DisplayName("should return the same hash code when the two objects are equal")
        void hashCode_shouldReturnSameHashCodeWhenEqual() {
            Inventory other = new Inventory();
            assertThat(inventory).hasSameHashCodeAs(other);
        }

        @Test
        @DisplayName("should return a different hash code when the two objects are not equal")
        void hashCode_shouldReturnDifferentHashCodeWhenNotEqual() {
            Inventory other = new Inventory();
            other.getBambooStack().collectBamboo();
            assertThat(inventory).doesNotHaveSameHashCodeAs(other);
        }
    }

    @Nested
    @DisplayName("Method copy()")
    class TestCopy {
        @Test
        @DisplayName("should return a copy of the object")
        void copy_shouldReturnCopyOfObject() {
            Inventory copy = inventory.copy();
            assertThat(copy).isEqualTo(inventory).isNotSameAs(inventory);
        }
    }
}
