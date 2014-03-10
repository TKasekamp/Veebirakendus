package data;

public class CodeItem {
	private Integer id;
	private String name;
	private String text;
	private String language;
	private String privacy; // 0 public, 1 private
	private String address; // for Juhan to think about

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

	public CodeItem(String name, String text, String language, String privacy) {
		super();
		this.name = name;
		this.text = text;
		this.language = language;
		this.privacy = privacy;
	}

//	public CodeItem(String name, String text, String language, String privacy) {
//		super();
//		this.name = name;
//		this.text = text;
//		this.language = language;
//		if (privacy.equalsIgnoreCase("public")) {
//			this.privacy = 0;
//		}
//		else {
//			this.privacy = 1;
//		}
//	}

	public CodeItem() {
		// TODO Auto-generated constructor stub
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

}
