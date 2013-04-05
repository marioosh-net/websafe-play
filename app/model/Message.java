package model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity(name = "messages")
public class Message extends Model {

	public static Finder<Long, Message> find = new Finder<Long, Message>(Long.class, Message.class); 	

	@Id
	private Long id;
	
	private Date timestamp = new Date(System.currentTimeMillis());
	
	@Column(length=1024)
	String url;
	
	String contentType;
	String contentEncoding;

	private byte[] data;
	
	@ManyToOne
	private Message parent;
	
	@OneToMany(mappedBy="parent")
	List<Message> dependencies;

	public Message() {
		// TODO Auto-generated constructor stub
	}
	
	public Message(String url, String contentType, byte[] data) {
		super();
		this.url = url;
		this.contentType = contentType;
		this.data = data;
	}



	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	
	public Date getTimestamp() {
		return timestamp;
	}

	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	
	public String getUrl() {
		return url;
	}

	
	public void setUrl(String url) {
		this.url = url;
	}

	
	public String getContentType() {
		return contentType;
	}

	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	
	public byte[] getData() {
		return data;
	}

	
	public void setData(byte[] data) {
		this.data = data;
	}

	
	public Message getParent() {
		return parent;
	}

	
	public void setParent(Message parent) {
		this.parent = parent;
	}

	
	public List<Message> getDependencies() {
		return dependencies;
	}

	
	public void setDependencies(List<Message> dependencies) {
		this.dependencies = dependencies;
	}
	
	public String getContentEncoding() {
		return contentEncoding;
	}
	
	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public String validate() {
		if(url.isEmpty() || (!url.startsWith("http://") && !url.startsWith("file://") && !url.startsWith("https://") && !url.startsWith("ftp://"))) {
			return "URL not valid";
		}
		return null;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", timestamp=" + timestamp + ", url=" + url + ", contentType=" + contentType + ", data=byte[]" + ", parent=" + parent + ", dependencies=" + dependencies + "]";
	}

	private transient Map<String,List<String>> headerFields;
	public Map<String, List<String>> getHeaderFields() {
		return headerFields;
	}
	
	public void setHeaderFields(Map<String, List<String>> headerFields) {
		this.headerFields = headerFields;
	}
	
}