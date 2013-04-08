package model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity(name = "tags")
public class Tag extends Model {

	public static Finder<Long, Tag> find = new Finder<Long, Tag>(Long.class, Tag.class); 	

	@Id 
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_tags")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	public Tag() {
		// TODO Auto-generated constructor stub
	}
	
	public Tag(String name) {
		this.name = name;
	}
	
	@ManyToMany(mappedBy="tags", cascade=CascadeType.ALL)
	private Set<Message> message;

	
	public Set<Message> getMessage() {
		return message;
	}
	
	
	public void setMessage(Set<Message> message) {
		this.message = message;
	}
	
	private Date timestamp = new Date(System.currentTimeMillis());
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	
	public Long getId() {
		return id;
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Tag [id=" + id + ", name=" + name + ", message=" + message + ", timestamp=" + timestamp + "]";
	}

}