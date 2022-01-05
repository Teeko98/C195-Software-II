package main;

import java.sql.Date;

public class FirstLevelDivision {
    private int divisionId;
    private String division;
    private Date createDate;
    private String createdBy;
    private Date lastUpdated;
    private String lastUpdatedBy;
    private int countryId;

    /**
     * The constructor for the FirstLevelDivision class.
     */
    public FirstLevelDivision(int divisionId, String division, Date createDate, String createdBy, Date lastUpdated, String lastUpdatedBy, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }

    /**
     * @return the division ID.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * @param divisionId the updated division ID.
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * @return the division name.
     */
    public String getDivision() {
        return division;
    }

    /**
     * @param division the updated division name.
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * @return the date created.
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the updated create date.
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return name of the user who created the division.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy updates the creator.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the last updated date.
     */
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated the latest update date.
     */
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * @return the user who last updated the division.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @param lastUpdatedBy the latest user to update the division.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @return the country ID that corresponds with the division.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * @param countryId updates the country id.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
