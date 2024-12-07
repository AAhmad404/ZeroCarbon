package customDataStructures;

import androidx.annotation.NonNull;

/**
 * This class represents a node containing information about a specific type of emission
 * along with an emission amount.
 */
public class EmissionNode {
    /**
     * The type of emission (e.g., "Energy", "Food", "Transportation", ...).
     */
    private final String emissionType;

    /**
     * The amount of the emission in a kilograms.
     */
    private final float emissionAmount;

    /**
     * EmissionsNode constructor method.
     *
     * @param emissionType  the type of emission (e.g., "Energy", "Food", "Transportation", ...).
     * @param emissionAmount the amount of the emission in a kilograms.
     */
    public EmissionNode(String emissionType, float emissionAmount) {
        this.emissionType = emissionType;
        this.emissionAmount = emissionAmount;
    }

    /**
     * Returns the type of emission.
     *
     * @return the emission type as a string (e.g., "Energy", "Food", "Transportation", ...).
     */
    public String getEmissionType() {
        return emissionType;
    }

    /**
     * Returns the amount of the emission in kilograms.
     *
     * @return the emission amount as a float.
     */
    public float getEmissionsAmount() {
        return emissionAmount;
    }

    /**
     * Returns a string representation of the EmissionNode.
     *
     * @return a string representation of the EmissionNode that includes
     * the emission type and the emission amount.
     */
    @NonNull
    @Override
    public String toString() {
        return "Emission Type: " + emissionType + ", Emissions Amount: " + emissionAmount;
    }
}
