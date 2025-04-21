package Entity;

public class Broodmother extends Enemy {
    // Flag to ensure the spawn is only triggered once.
    private boolean spawnTriggered = false;
    // Record the time of death.
    private long deathTime;

    public Broodmother(int startX, int startY) {
        super(400, 0.7f, startX, startY, "art/Bugs/Broodmother.png");
    }

    @Override
    public int getPointValue() {
        return 300;
    }

    // Override takeDamage to trigger onDeath() when health reaches 0.
    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (!isAlive() && !spawnTriggered) {
            onDeath();
        }
    }

    // This method is called when the Broodmother dies.
    public void onDeath() {
        spawnTriggered = true;
        deathTime = System.currentTimeMillis();
        System.out.println("Broodmother died. Will spawn 3 Basic Bugs after 5 seconds.");
        // In a real game, you might schedule a timer or flag an event here.
    }


    // Update method checks if the delay has passed to spawn new bugs.
    @Override
    public void update() {
        super.update();
        // If onDeath has been triggered and 5 seconds have elapsed.
        if (spawnTriggered && (System.currentTimeMillis() - deathTime) >= 5000) {
            spawnBasicBugs();
            // Prevent further spawns (or handle removal of the corpse).
            spawnTriggered = false;
        }
    }
    
    // Placeholder method for spawning bugs.
    private void spawnBasicBugs() {
        System.out.println("Spawning 3 Basic Bugs from Broodmother's corpse.");
        // TODO: Integrate with your enemy/spawn manager to actually create 3 BasicBug instances.
    }
}
