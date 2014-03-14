package data;

public class CodeItem {
	private Integer id;
	private String name;
	private String text;
	private String language;
	private String privacy; // 0 public, 1 private
	private String address; // for Juhan to think about
	private Integer UserId; // who it belongs to

	public CodeItem(Integer id, String name, String text) {
		super();
		this.name = name;
		this.text = text;
		this.id = id;
	}

	public CodeItem(String name, String text) {
		super();
		this.name = name;
		this.text = text;
	}

	public CodeItem(Integer id, String name, String text, String language,
			String privacy) {
		super();
		this.id = id;
		this.name = name;
		this.text = text;
		this.language = language;
		this.privacy = privacy;
	}

	public CodeItem(String name, String text, String language, String privacy) {
		super();
		this.name = name;
		this.text = text;
		this.language = language.toLowerCase();
		this.privacy = privacy;
	}

	public CodeItem() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Generates a string with id. It can be done with a costum serializer, but
	 * this is easier.
	 * 
	 * @return id in Json format
	 */
	public String JsonID() {
		return "{\"id\":\"" + Integer.toString(id) + "\"}";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "CodeItem [id=" + id + ", name=" + name + ", text=" + text
				+ ", language=" + language + ", privacy=" + privacy
				+ ", address=" + address + "]";
	}

	public String getLanguage() {
		return language;
	}

	public String getPrivacy() {
		return privacy;
	}

	public Integer getUserId() {
		return UserId;
	}

}
