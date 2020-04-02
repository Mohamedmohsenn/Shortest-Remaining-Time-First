public class Process implements Cloneable {
	String name;
	int arrivalTime;
	int burstTime;
	int start;
	int end;
	int waitingTime;
	int turnAroundTime;
	
	protected Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
}
