package at.jku.swe.simcomp.azureadapter.service.HelperClasses;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;

@Fluent
public final class DeleteDigitalTwinOptions {
    /*
     * Only perform the operation if the entity's etag matches one of the etags
     * provided or * is provided.
     */
    @JsonProperty(value = "If-Match")
    private String ifMatch;

    /**
     * Get the ifMatch property: Only perform the operation if the entity's etag matches one of the etags provided or *
     * is provided.
     *
     * @return the ifMatch value.
     */
    public String getIfMatch() {
        return this.ifMatch;
    }

    /**
     * Set the ifMatch property: Only perform the operation if the entity's etag matches one of the etags provided or *
     * is provided.
     *
     * @param ifMatch the ifMatch value to set.
     * @return the at.jku.swe.simcomp.azureadapter.HelperClasses.DeleteDigitalTwinOptions object itself.
     */
    public DeleteDigitalTwinOptions setIfMatch(String ifMatch) {
        this.ifMatch = ifMatch;
        return this;
    }
}