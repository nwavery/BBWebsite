package net.bst.springboot.springsecurity.model;

// Add/adjust positions as needed for different races/rules
public enum Position {
    LINEMAN("Lineman"),
    BLITZER("Blitzer"),
    THROWER("Thrower"),
    CATCHER("Catcher"),
    BLOCKER("Blocker"), // e.g., Black Orc, Saurus
    RUNNER("Runner"),   // e.g., Gutter Runner
    BIG_GUY("Big Guy"), // e.g., Troll, Ogre, Treeman
    STAR_PLAYER("Star Player"); // Generic for stars

    private final String displayName;

    Position(String displayName) {
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