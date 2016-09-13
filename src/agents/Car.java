package agents;

import graph.MyNode;

public class Car {

	private final double tankFuel; // Liters
	private final double fuelEconomy; // Liters/100km
	private final double maxDrivingRange; // Kilometers
	private final double maxDailyDrivingTime; // Minutes
	private final double fuelPrice; // €

	private MyNode position; // Starting node
	private MyNode destiny; // Goal node
	private double drivenMinutes; // Minutes driven in current day
	private double drivingRange; // Kilometers

	public Car(MyNode position, double tankFuel, double fuelEconomy,
			double fuelPrice) {
		this.maxDailyDrivingTime = 8.0 * 60.0;
		this.tankFuel = tankFuel;
		this.fuelEconomy = fuelEconomy;
		this.fuelPrice = fuelPrice;
		this.maxDrivingRange = (this.tankFuel * 100) / this.fuelEconomy;
		this.drivenMinutes = 0.0;
		this.drivingRange = this.maxDrivingRange;
		this.position = position;
	}

	public Car(Car car) {
		this.maxDailyDrivingTime = 8.0 * 60.0;
		this.tankFuel = car.getTankFuel();
		this.fuelEconomy = car.getFuelEconomy();
		this.fuelPrice = car.getFuelPrice();
		this.maxDrivingRange = (this.tankFuel * 100) / this.fuelEconomy;
		this.drivenMinutes = car.getDrivenMinutes();
		this.drivingRange = car.getDrivingRange();
		this.position = car.getPosition();
		this.destiny = car.getDestiny();
	}

	public double getDailyMaxDrivingTime() {
		return maxDailyDrivingTime;
	}

	public double getFuelPrice() {
		return fuelPrice;
	}

	public double getMaxDriving() {
		return this.maxDailyDrivingTime;
	}

	public MyNode getPosition() {
		return position;
	}

	public double getTankFuel() {
		return tankFuel;
	}

	public double getFuelEconomy() {
		return fuelEconomy;
	}

	public double getDrivenMinutes() {
		return drivenMinutes;
	}

	public double getDrivingRange() {
		return drivingRange;
	}

	public double getMaxDrivingRange() {
		return maxDrivingRange;
	}

	public double getMaxDailyDrivingTime() {
		return maxDailyDrivingTime;
	}

	public void setPosition(MyNode position) {
		this.position = position;
	}

	public MyNode getDestiny() {
		return destiny;
	}

	public void setDestiny(MyNode destiny) {
		this.destiny = destiny;
	}

	public boolean isDistanceDrivable(double distance) {
		return (this.maxDrivingRange - distance) >= 0;
	}

	public boolean isTimeDrivable(double time) {
		return (0 + time) <= this.maxDailyDrivingTime;
	}

	public void addTotalDrivenMinutes(double minutes) {
		this.drivenMinutes += minutes;
	}

	public void resetDrivenMinutesOnDay() {
		this.drivenMinutes = 0.0;
	}

	public void resetDrivingRange() {
		this.drivingRange = this.maxDrivingRange;
	}

	public void subtractDrivingRange(double range) {
		this.drivingRange -= range;
	}
}
