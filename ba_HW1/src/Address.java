/**
 * @HW 1
 * @author Itai
 */
public class Address {

	private String name;
	private String street;
	private String city;
	private String state;
	private int zip;

	public Address(String name, String street, String city, String state, String zip) {
		this.name = name;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = Integer.parseInt(zip.trim());
	}

	public String getName() {
		return name;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public int getZip() {
		return zip;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public void setZip(String zip) {
		this.zip = Integer.parseInt(zip.trim());
	}

	@Override
	public String toString() {
		return String.format("%s , %s , %s , %s , %d", name, street, city, state, zip);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((name == null) ? 0 : name.toLowerCase().hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Address other = (Address) obj;
		return (name.toLowerCase().equals(other.name.toLowerCase()) && street.equals(other.street)
				&& city.equals(other.city) && state.equals(other.state));
	}
}
