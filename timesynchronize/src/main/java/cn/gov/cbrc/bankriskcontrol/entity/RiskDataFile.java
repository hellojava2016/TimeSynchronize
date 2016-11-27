package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the risk_data_file database table.
 * 
 */
@Entity
@Table(name="risk_data_file")
@NamedQuery(name="RiskDataFile.findAll", query="SELECT r FROM RiskDataFile r")
public class RiskDataFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="file_id")
	private long fileId;

	@Column(name="file_name")
	private String fileName;

	@Column(name="file_size")
	private int fileSize;

	@Column(name="source_file_name")
	private String sourceFileName;

	@Column(name="upload_time")
	private Date uploadTime;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public RiskDataFile() {
	}

	public long getFileId() {
		return this.fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getSourceFileName() {
		return this.sourceFileName;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	public Date getUploadTime() {
		return this.uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}