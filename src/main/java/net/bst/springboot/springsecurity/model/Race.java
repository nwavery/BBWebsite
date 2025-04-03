package net.bst.springboot.springsecurity.model;

// Add more races as needed
public enum Race {
    HUMAN("Human"),
    ORC("Orc"),
    DWARF("Dwarf"),
    SKAVEN("Skaven"),
    HIGH_ELF("High Elf"),
    DARK_ELF("Dark Elf"),
    WOOD_ELF("Wood Elf"),
    CHAOS_CHOSEN("Chaos Chosen"),
    UNDEAD("Undead"),
    LIZARDMEN("Lizardmen"),
    NORSE("Norse"),
    AMAZON("Amazon"), // Example of adding more
    HALFLING("Halfling"),
    GOBLIN("Goblin");

    private final String displayName;

    Race(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
} 