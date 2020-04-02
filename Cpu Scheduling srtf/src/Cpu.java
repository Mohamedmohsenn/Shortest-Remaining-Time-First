import java.util.ArrayList;
import java.util.Scanner;

public class Cpu {
	ArrayList<Process> processes = new ArrayList<Process>();
	ArrayList<Process> done = new ArrayList<Process>();
	ArrayList<Process> processes2 = new ArrayList<Process>();
	ArrayList<Process> processes3 = new ArrayList<Process>();
	ArrayList<Process> arrived2 = new ArrayList<Process>();

	public Cpu() {
	}

	public void getInput() throws CloneNotSupportedException {
		System.out.println("Enter number of processes");
		int NumberOfProcess;
		Scanner in = new Scanner(System.in);
		NumberOfProcess = in.nextInt();
		for (int i = 0; i < NumberOfProcess; i++) {
			Process p = new Process();
			System.out.println("Enter process Name : ");
			p.name = in.next();
			System.out.println("Enter process Arrival time : ");
			p.arrivalTime = in.nextInt();
			System.out.println("Enter process Burst time : ");
			p.burstTime = in.nextInt();
			processes.add(p);
			Process d = (Process) p.clone();
			processes2.add(d);
			Process p2 = (Process) p.clone();
			processes3.add(p2);
		}
	}

	void IncreamentTime(int value) {
		for (int j = 0; j < value; j++) {
			for (int i = 0; i < processes.size(); i++) {
				if(processes.get(i).arrivalTime == j && !arrived2.contains(processes.get(i)))
					arrived2.add(processes.get(i));
			}
		}
	}

	private Process findMin(ArrayList<Process> processes4) {
		int mi = processes4.get(0).burstTime;
		int j = 0;
		for (int i = 1; i < processes4.size(); i++) {
			if (processes4.get(i).burstTime < mi) {
				mi = processes4.get(i).burstTime;
				j = i;
			} else if (processes4.get(i).burstTime == mi) {
				if (processes4.get(i).arrivalTime < processes4.get(j).arrivalTime)
					j = i;
			}
		}
		return processes4.get(j);
	}

	public void shortestJobFirst() throws CloneNotSupportedException {
		int min = getMinArrivalTime();
		Process p = new Process();
		for (int time = min; processes.size() != 0 ; time++) {
			for (int i = 0; i < processes.size(); i++) {
				if (processes.get(i).arrivalTime == time) {
					arrived2.add(processes.get(i));
				}
			}
			if (arrived2.size() != 0) {
				p = findMin(arrived2);
			}
			if (done.size() == 0) {
				p.start = min;
				p.end = p.burstTime;
				p.waitingTime = 0;
				p.turnAroundTime = p.burstTime;
				Process temp = (Process) p.clone();
				done.add(temp);
				arrived2.remove(p);
				processes.remove(p);
				IncreamentTime(p.burstTime);
				time+=p.burstTime-1;
			} else {
				p.start = done.get(done.size() - 1).end;
				p.end = p.start + p.burstTime;
				p.waitingTime = p.start - p.arrivalTime;
				p.turnAroundTime = p.end - p.arrivalTime;
				Process temp = (Process) p.clone();
				done.add(temp);
				arrived2.remove(p);
				processes.remove(p);
				IncreamentTime(p.end);
				time+=p.end-1;
			}
		}

		for (int i = 0; i < done.size(); i++) {
			System.out.println("process name : " + done.get(i).name);
			System.out.println("process start time : " + done.get(i).start);
			System.out.println("process end time : " + done.get(i).end);
			System.out.println("process waiting time : "
					+ done.get(i).waitingTime);
			System.out.println("process turnaround time : "
					+ done.get(i).turnAroundTime);
			System.out.println("-----------------------");
		}
		float averageWaitingTime = 0;
		float averageturnAroundTime = 0;
		for (int i = 0; i < done.size(); i++) {
			averageWaitingTime += done.get(i).waitingTime;
			averageturnAroundTime += done.get(i).turnAroundTime;
		}
		System.out.println("Average waiting time is : " + averageWaitingTime
				/ done.size());
		System.out.println("Average turn around time is : "
				+ averageturnAroundTime / done.size());
		done.clear();
	}

	private int getArrivalTime(Process p) {
		int temp = 0;
		for (int i = 0; i < processes3.size(); i++) {
			if (p.name.equals(processes3.get(i).name)) {
				temp = processes3.get(i).arrivalTime;
				break;
			}
		}
		return temp;
	}

	private int getBurstTime(Process p) {
		int x = 0;
		for (int i = 0; i < processes3.size(); i++) {
			if (p.name.equals(processes3.get(i).name)) {
				x = processes3.get(i).burstTime;
				break;
			}
		}
		return x;
	}

	private int getMinArrivalTime() {
		int min = processes.get(0).arrivalTime;
		for (int i = 1; i < processes.size(); i++) {
			if (processes.get(i).arrivalTime < min) {
				min = processes.get(i).arrivalTime;
			}
		}
		return min;
	}

	public void SRTF() throws CloneNotSupportedException {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the context switch : ");
		int contextSwitch = in.nextInt();
		int min = getMinArrivalTime();
		ArrayList<Process> arrived = new ArrayList<Process>();
		processes2.clear();
		Process p = new Process();
		boolean flag = false;
		boolean check = false;
		for (int i = min; processes2.size() != processes.size() ; i++) {
			for (int j = 0; j < processes.size(); j++) {
				if (processes.get(j).arrivalTime == i) {
					arrived.add(processes.get(j));
					flag = true;
				}
			}
			if (flag) {
				if (i != min && findMin(arrived) != p) {
					for (int m = i + 1; m <= i + contextSwitch; m++) {
						for (int z = 0; z < processes.size(); z++) {
							if (processes.get(z).arrivalTime == m) {
								arrived.add(processes.get(z));
							}
						}
					}
				}
				Process temp = (Process) p.clone();
				if (i != min && check == false && findMin(arrived) != p) {
					temp.end = i;
					if (done.size() == 0) {
						temp.start = min;
					}
					done.add(temp);
				}
				check = false;
				if (findMin(arrived) != p) {
					p = findMin(arrived);
					if (done.size() != 0) {
						p.start = i + contextSwitch;
					}
					if (i != min) {
						i += contextSwitch;
					}
				}
				flag = false;
			}
			p.burstTime--;
			while (true) {
				if (p.burstTime == 0) {
					i++;
					p.end = i;
					p.turnAroundTime = p.end - getArrivalTime(p);
					p.waitingTime = p.turnAroundTime - getBurstTime(p);
					Process temp3 = (Process) p.clone();
					processes2.add(temp3);
					check = true;
					arrived.remove(p);
					for (int m = i; m <= i + contextSwitch; m++) {
						for (int z = 0; z < processes.size(); z++) {
							if (processes.get(z).arrivalTime == m) {
								arrived.add(processes.get(z));
							}
						}
					}
					Process temp2 = (Process) p.clone();
					if (done.size() == 0)
						temp2.start = min;
					done.add(temp2);
					if (arrived.size() != 0) {
						p = findMin(arrived);
						p.start = i + contextSwitch;
						p.burstTime--;
					} else {
						break;
					}
					i += contextSwitch;
					if (p.burstTime != 0) {
						break;
					}
				} else {
					break;
				}
			}
		}
	}

	public void getData() {
		for (int i = 0; i < done.size(); i++) {
			System.out.println("process name : " + done.get(i).name);
			System.out.println("process start time : " + done.get(i).start);
			System.out.println("process end time : " + done.get(i).end);
			System.out
					.println("---------------------------------------------------------------------------");
		}
		System.out.println("\nWaiting and Turn around for each process : \n");
		float averageWaitingTime = 0;
		float averageTurnAroundTime = 0;
		for (int i = 0; i < processes2.size(); i++) {
			System.out.println("process Name : " + processes2.get(i).name);
			System.out.println("Turn around Time : "
					+ processes2.get(i).turnAroundTime);
			averageTurnAroundTime += processes2.get(i).turnAroundTime;
			System.out.println("Waiting Time : "
					+ processes2.get(i).waitingTime);
			averageWaitingTime += processes2.get(i).waitingTime;
			System.out.println("********************************************");
		}
		averageTurnAroundTime = averageTurnAroundTime / processes2.size();
		averageWaitingTime = averageWaitingTime / processes2.size();
		System.out.println("average turn around time : "
				+ averageTurnAroundTime);
		System.out.println("average waiting time : " + averageWaitingTime);
	}
}
