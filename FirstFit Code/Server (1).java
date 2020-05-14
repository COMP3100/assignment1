import java.util.ArrayList;
import java.util.List;

public class Server {
	String type;
	int limit;
	int bootupTime;
	double rate;
	int coreCount;
	int memory;
	int disk;
	int ID;
	int state;
	int availableTime;
	
	//Getters
	public String getType() {
		return this.type;
	}
	
	public int getLimit() {
		return this.limit;
	}
	
	public int getBootupTime() {
		return this.bootupTime;
	}
	
	public double getRate() {
		return this.rate;
	}
	
	public int getCoreCount() {
		return this.coreCount;
	}
	
	public int getMemory() {
		return this.memory;
	}
	
	public int getDisk() {
		return this.disk;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public int getState() {
		return this.state;
	}
	
	public int getAvailableTime() {
		return this.availableTime;
	}
	
	//Setters
	public void setType(String type) {
		this.type = type;
	}
	
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public void setBootupTime(int bootupTime) {
		this.bootupTime = bootupTime;
	}
	
	public void setRate(double rate) {
		this.rate = rate;
	}
	
	public void setCoreCount(int coreCount) {
		this.coreCount = coreCount;
	}
	
	public void setMemory(int memory) {
		this.memory = memory;
	}
	
	public void setDisk(int disk) {
		this.disk = disk;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public void setAvailableTime(int availableTime) {
		this.availableTime = availableTime;
	}
	
	public Server(String type, int limit, int bootupTime, double rate, int coreCount, int memory, int disk) {
		setType(type);
		setLimit(limit);
		setBootupTime(bootupTime);
		setRate(rate);
		setCoreCount(coreCount);
		setMemory(memory);
		setDisk(disk);
	}
}
