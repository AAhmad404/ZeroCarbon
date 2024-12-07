package customDataStructures;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * This class represents a collection of different emission categories along with their amounts.
 * @see EmissionNode
 */
public class EmissionNodeCollection {

    /**
     * The date of the emissions.
     */
    private final String date;

    /**
     * A list of {@link EmissionNode} objects representing the emission data.
     */
    private final ArrayList<EmissionNode> data;

    /**
     * EmissionsNodeCollection constructor method.
     *
     * @param date the date associated with the emissions data.
     */
    public EmissionNodeCollection(String date) {
        this.date = date;
        this.data = new ArrayList<>();
    }

    /**
     * Returns the date of the emissions.
     *
     * @return the date of the emissions.
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns a list of {@link EmissionNode} objects representing the emission data.
     *
     * @return a list of {@link EmissionNode} objects
     */
    public ArrayList<EmissionNode> getData() {
        return data;
    }

    /**
     * Adds a new emission node to the collection. If an {@link EmissionNode} of the same type
     * already exists, it updates the existing {@link EmissionNode} by adding the new emission amount
     * to the existing amount.
     *
     * @param emissionNode the {@link EmissionNode} to be added.
     */
    public void addData(EmissionNode emissionNode) {

        for (EmissionNode node : data) {
            if (node.getEmissionType().equals(emissionNode.getEmissionType())) {
                float newAmount = node.getEmissionsAmount() + emissionNode.getEmissionsAmount();
                data.remove(node);
                data.add(new EmissionNode(emissionNode.getEmissionType(), newAmount));
                return;
            }
        }

        data.add(emissionNode);
    }

    /**
     * Returns a string representation of the EmissionNodeCollection.
     *
     * @return a string representation of the EmissionNodeCollection that includes
     * the emission date of the emissions and collection itself.
     */
    @NonNull
    @Override
    public String toString() {
        return "Date: " + date + ", Data: " + data;
    }
}
