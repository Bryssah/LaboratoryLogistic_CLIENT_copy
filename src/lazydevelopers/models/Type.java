package lazydevelopers.models;

/**
 * Utensils types por Utensil instances: container, tool, ppe or machine type.
 * @author Garikoitz
 */
public enum Type {
    /**
     * Type of utensil used by Users, the ones that can store liquids or others.
     */
    CONTAINER,
    /**
     * Type of utensil used by Users used to hold, filter or grasp things.
     */
    TOOL,
    /**
     * Type of utensil used by Users to maintain our individual protection.
     */
    PPE,
    /**
     * Type of utensil used by Users used to remove, analyze or alter substances.
     */
    MACHINE;
}
