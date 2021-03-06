package kh.com.kshrd.ipcam.entity.camera;
import com.fasterxml.jackson.annotation.JsonProperty;
import kh.com.kshrd.ipcam.entity.camera.base.BaseEntity;
import kh.com.kshrd.ipcam.entity.user.User;

public class IPCam {

	@JsonProperty("CAMERA_ID")
	private long camera_id;

	@JsonProperty("NAME")
	private String name;

	@JsonProperty("SERIAL_NUMBER")
	private String serial_number;
	
	@JsonProperty("IP_ADDRESS")
	private String ip_address;
	
	@JsonProperty("WEB_PORT")
	private int web_port;
	
	@JsonProperty("RTSP_PORT")
	private int rtsp_port;
	
	@JsonProperty("USERNAME")
	private String username;
	
	@JsonProperty("PASSWORD")
	private String password;

	@JsonProperty("MODEL")
	private Model model;
	
	@JsonProperty("USER")
	private User user;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCamera_id() {
		return camera_id;
	}

	public void setCamera_id(long camera_id) {
		this.camera_id = camera_id;
	}

	public String getSerial_number() {
		return serial_number;
	}
	
	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}
	
	public String getIp_address() {
		return ip_address;
	}
	
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	
	public int getWeb_port() {
		return web_port;
	}
	
	public void setWeb_port(int web_port) {
		this.web_port = web_port;
	}
	
	public int getRtsp_port() {
		return rtsp_port;
	}
	
	public void setRtsp_port(int rtsp_port) {
		this.rtsp_port = rtsp_port;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public Model getModel() {
		return model;
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

}
