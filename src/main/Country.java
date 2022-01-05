package main;

public class Country {
    private int countryId;
    private String countryName;

    /**
     * The constructor for the Country class.
     */
    public Country(int id, String name){
        this.countryId = id;
        this.countryName = name;
    }

    /**
     * @return the country ID.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * @param countryId the updated country ID.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * @return the country name.
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * @param countryName the updated country name.
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
