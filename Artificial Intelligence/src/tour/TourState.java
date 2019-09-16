package tour;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import search.Action;
import search.State;

public class TourState implements State {
	protected final Set<City> visitedCities;
	protected final City currentCity;

	public City getCurrentCity() {
		return currentCity;
	}

	public Set<City> getVisitedCities() {
		return visitedCities;
	}

	public TourState(City startCity) {
		this.visitedCities = Collections.emptySet();
		this.currentCity = startCity;
	}
	public TourState(Set<City> visitedCities, City currentCity) {
		this.visitedCities = visitedCities;
		this.currentCity = currentCity;
	}
	public Set<Road> getApplicableActions() {
		return currentCity.outgoingRoads;
	}
	public State getActionResult(Action action) {
		Road road = (Road)action;
		Set<City> newVisitedCities = new LinkedHashSet<City>(visitedCities);
		newVisitedCities.add(road.targetCity);
		return new TourState(newVisitedCities, road.targetCity);
	}
	
	public boolean equals(Object that) {
		if(this == that) return true;
		if(that == null) return false;
		if (getClass() != that.getClass()) return false;
		final TourState other = (TourState) that;
		if(this.getCurrentCity() != other.getCurrentCity()) return false;
		if(this.getVisitedCities() != this.getVisitedCities()) return false;
		return true;
	}
	public int hashCode() {
		return getCurrentCity().hashCode() + getVisitedCities().hashCode()*31;
	}
}
